package com.dsp.cga.assurance.extensionrequest.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dsp.cga.assurance.dto.StatusDTO;
import com.dsp.cga.assurance.dto.UserDetailsDTO;
import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.assurance.entity.Departments;
import com.dsp.cga.assurance.entity.Ministries;
import com.dsp.cga.assurance.extensionrequest.dto.ExtensionRequestDTO;
import com.dsp.cga.assurance.extensionrequest.dto.ExtensionRequestResponseDTO;
import com.dsp.cga.assurance.extensionrequest.entity.ExtensionRequest;
import com.dsp.cga.assurance.extensionrequest.repository.ExtensionRequestRepository;
//import com.dsp.cga.assurance.extensionrequest.repository.ExtensionRequestRepository;
import com.dsp.cga.assurance.extensionrequest.service.ExtensionRequestService;
import com.dsp.cga.assurance.implementationrequest.dto.ImplementationRequestDTO;
import com.dsp.cga.assurance.implementationrequest.entity.ImplementationRequest;
import com.dsp.cga.assurance.repository.AssuranceRepository;
import com.dsp.cga.assurance.repository.CodeRepository;
import com.dsp.cga.assurance.repository.DepartmentRepository;
import com.dsp.cga.assurance.repository.MasterLSSRepository;
import com.dsp.cga.assurance.repository.MastersRSSRepository;
import com.dsp.cga.assurance.repository.MinistryRepository;
import com.dsp.cga.assurance.repository.PersonalDetailsRepository;
import com.dsp.cga.mapper.ExtensionRequestMapper;
import com.dsp.cga.mapper.ImplementationRequestMapper;
import com.dsp.cga.response.GenericResponse;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

@Service
public class ExtensionRequestServiceImpl implements ExtensionRequestService {

	private static final Logger log = LoggerFactory.getLogger(ExtensionRequestServiceImpl.class);
	
	@Value("${master.codeServiceUrl}")
	private String codeServiceUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	

	@Autowired
	private ExtensionRequestRepository extenionRequestRepository;
	
	@Autowired
	private AssuranceRepository assuranceRepository;
	
	@Autowired
	private MinistryRepository ministryRepository;
	
	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;
	
	@Autowired
	private MasterLSSRepository lssRepo;
	
	@Autowired
	private MastersRSSRepository rssRepo;
	
	@Autowired
	private CodeRepository codeRepository;
	


	@Override
	public ExtensionRequestDTO createExtensionRequest(ExtensionRequestDTO extensionRequestDTO) {
		ExtensionRequest extensionRequest = ExtensionRequestMapper.toEntity(extensionRequestDTO);
        ExtensionRequest saved = extenionRequestRepository.save(extensionRequest);
        return ExtensionRequestMapper.toDTO(saved);
    }

	@Override
	public List<ExtensionRequestDTO> getExtensionRequestByAssuranceId(Long id) {
	    // Fetch the list of ExtensionRequest entities by Assurance ID
	    List<ExtensionRequest> extensionRequests = extenionRequestRepository.findByAssuranceId(id);

	    // If no records are found, log the message and return an empty list
	    if (extensionRequests.isEmpty()) {
	        log.debug("No ExtensionRequest found for Assurance ID: {}", id);
	        return Collections.emptyList(); // Return an empty list
	    }

	    // Fetch Assurance to get the assuranceNo
	    Assurance assurance = assuranceRepository.findById(id).orElse(null);
	    String assuranceNo = (assurance != null) ? assurance.getAssuranceNo() : null;

	    // Map the list of entities to a list of DTOs and set the assuranceNo in each DTO
	    List<ExtensionRequestDTO> dtos = extensionRequests.stream()
	            .map(request -> {
	                ExtensionRequestDTO dto = ExtensionRequestMapper.toDTO(request);
	                dto.setAssuranceNo(assuranceNo);  // Set the assuranceNo in each DTO
	                
	                if (request.getStatusId() != null) {
	                    StatusDTO statusDTO = fetchStatusData(request.getStatusId());
	                    dto.setStatusDTO(statusDTO); // Add StatusDTO to the ExtensionRequestDTO
	                }
	                return dto;
	            })
	            .collect(Collectors.toList());

	    log.debug("Returning ExtensionRequestDTOs: {}", dtos);
	    return dtos;
	}



	

	@Override
	public GenericResponse<List<ExtensionRequestDTO>> createOrUpdateExtensionRequests(
	        List<ExtensionRequestDTO> extensionRequestDTOList) {

	    List<ExtensionRequestDTO> savedDTOs = new ArrayList<>();

	    for (ExtensionRequestDTO dto : extensionRequestDTOList) {
	        if (dto == null || dto.getAssuranceId() == null || dto.getExtensionNo() == null) {
	            throw new IllegalArgumentException("Invalid input: AssuranceId and ExtensionNo cannot be null.");
	        }

	        ExtensionRequest savedRequest;

	        if (dto.getId() != null) {
	            // If the ID is provided, try to find the existing record by ID
	            Optional<ExtensionRequest> existingRequestOpt = extenionRequestRepository.findById(dto.getId());

	            if (existingRequestOpt.isPresent()) {
	                // Update the existing entity
	                ExtensionRequest existingRequest = existingRequestOpt.get();
	                ExtensionRequestMapper.updateEntityFromDTO(dto, existingRequest);

	                // Save the updated entity
	                savedRequest = extenionRequestRepository.save(existingRequest);
	            } else {
	                throw new IllegalArgumentException("No ExtensionRequest found with the provided ID: " + dto.getId());
	            }

	        } else {
	            // If no ID is provided, check if an entry exists for assuranceId + extensionNo
	            Optional<ExtensionRequest> existingRequestOpt = extenionRequestRepository
	                    .findByAssuranceIdAndExtensionNo(dto.getAssuranceId(), dto.getExtensionNo());

	            if (existingRequestOpt.isPresent()) {
	                // Update the existing entity
	                ExtensionRequest existingRequest = existingRequestOpt.get();
	                ExtensionRequestMapper.updateEntityFromDTO(dto, existingRequest);

	                // Save the updated entity
	                savedRequest = extenionRequestRepository.save(existingRequest);
	            } else {
	                // Create a new entity
	                ExtensionRequest newRequest = ExtensionRequestMapper.toEntity(dto);

	                // Save the new entity
	                savedRequest = extenionRequestRepository.save(newRequest);
	            }
	        }

	        // Convert the saved entity to DTO and add to the response list
	        ExtensionRequestDTO savedDTO = ExtensionRequestMapper.toDTO(savedRequest);
	        savedDTOs.add(savedDTO);
	    }

	    // Return the response wrapped in GenericResponse
	    return new GenericResponse<>(
	            HttpStatus.CREATED.value(),
	            true,
	            "ExtensionRequests created or updated successfully",
	            savedDTOs
	    );
	}
	private StatusDTO fetchStatusData(Long statusId) {
		String statusServiceUrl = codeServiceUrl + statusId;
		log.debug("Calling master service URL for Status: {}", statusServiceUrl);

		ResponseEntity<GenericResponse<StatusDTO>> response = restTemplate.exchange(statusServiceUrl, HttpMethod.GET,
				null, new ParameterizedTypeReference<GenericResponse<StatusDTO>>() {
				});
		System.out.println(response);

		if (!response.getStatusCode().is2xxSuccessful()) {
			log.error("Failed to fetch data from master service for Status: {} - {}", response.getStatusCode(),
					response.getBody());
			throw new RuntimeException(
					"Failed to fetch data from master service for Status. Status: " + response.getStatusCode());
		}

		StatusDTO statusDTO = response.getBody().getData();
		log.debug("Fetched StatusDTO: {}", statusDTO);

		return statusDTO;
	}

	@Override
	public List<ExtensionRequestResponseDTO> getFilteredExtensions(Long sessionNo, Long statusId,
	        LocalDate extRequestedTillDate, LocalDate extGrantedTillDate, LocalDate ministryRefDate,
	        Integer ministryCode) {

	    // 🔹 Get ministryId using ministryCode
	    final Long ministryId;
	    if (ministryCode != null) {
	        Ministries ministry = ministryRepository.findByMinistryCode(ministryCode);
	        if (ministry != null) {
	            ministryId = ministry.getId();
	        } else {
	            return List.of(); // Invalid ministryCode
	        }
	    } else {
	        ministryId = null;
	    }

	    // 🔹 Determine houseId (Lok Sabha = 2, Rajya Sabha = 1)
	    Long houseId = getHouseId(); // You should have this implemented

	    if (sessionNo == null) {
	        sessionNo = (houseId == 1)
	            ? rssRepo.findLatestByCreatedAt()
	            : lssRepo.findLatestByCreatedAt();
	    }

	    Long finalSessionNo = sessionNo;

	    // 🔹 Specification without join
	    Specification<ExtensionRequest> specification = (root, query, cb) -> {
	        List<Predicate> predicates = new ArrayList<>();

	        if (statusId != null) {
	            predicates.add(cb.equal(root.get("statusId"), statusId));
	        }

	        if (extRequestedTillDate != null) {
	            predicates.add(cb.equal(root.get("extRequestedTillDate"), extRequestedTillDate));
	        }

	        if (extGrantedTillDate != null) {
	            predicates.add(cb.equal(root.get("extGrantedTillDate"), extGrantedTillDate));
	        }

	        if (ministryRefDate != null) {
	            predicates.add(cb.equal(root.get("ministryRefDate"), ministryRefDate));
	        }

	        return cb.and(predicates.toArray(new Predicate[0]));
	    };

	    //  Fetch and filter manually using assuranceId
	    List<ExtensionRequest> filteredList = extenionRequestRepository.findAll(specification).stream()
	        .filter(er -> {
	        	 if (er.getAssuranceId() == null) return false;
	            Assurance assurance = assuranceRepository.findById(er.getAssuranceId()).orElse(null);
	            if (assurance == null) return false;

	            if (houseId == 1) {
	                if (assurance.getRajyaSabhaSessionNo() == null) return false;
	                if (finalSessionNo != null && !finalSessionNo.equals(assurance.getRajyaSabhaSessionNo())) return false;
	            } else if (houseId == 2) {
	                if (assurance.getLokSabhaSessionNo() == null || assurance.getLokSabhaNo() == null) return false;
	                if (finalSessionNo != null && !finalSessionNo.equals(assurance.getLokSabhaSessionNo())) return false;
	            }

	            if (ministryId != null && !ministryId.equals(assurance.getMinistryId())) return false;

	            return true;
	        })
	        .collect(Collectors.toList());

	    //  Convert to DTO
	    return filteredList.stream().map(er -> {
	        Assurance assurance = assuranceRepository.findById(er.getAssuranceId()).orElse(null);
	        return ExtensionRequestResponseDTO.builder()
	                .assuranceNo(assurance != null ? assurance.getAssuranceNo() : null)
	                .questionNo(assurance.getQuestionNo())
	                .extensionNo(er.getExtensionNo())
	                .status(codeRepository.findCodeById(
	                        assurance != null ? assurance.getStatusId() : null))
	                .lokSabhaSessionNo(assurance != null
	                        ? lssRepo.findSessionNumberById(assurance.getLokSabhaSessionNo())
	                        : null)
	                .lokSabhaNo(assurance != null
                    ? lssRepo.findLokSabhaNumberById(assurance.getLokSabhaNo())
                    : null)
	                .rajyaSabhaSessionNumber(assurance != null
                    ? rssRepo.findSessionNumberById(assurance.getRajyaSabhaSessionNo())
                    : null)
	                .remarks(er.getRemarks())
	                .extGrantedTillDate(er.getExtGrantedTillDate())
	                .id(er.getId())
	                .extRequestedTillDate(er.getExtRequestedTillDate())
	                .ministryRefDate(er.getMinistryRefDate())
	                .ministryRefNo(er.getMinistryRefNo())
	                .build();
	    }).collect(Collectors.toList());
	}


	
	private Long getHouseId() {
		Long houseId = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetailsDTO) {
			houseId = ((UserDetailsDTO) principal).getHouseId();
		}
		return houseId;
	}

	@Override
	public ExtensionRequestResponseDTO getExtensionRequestById(Long id) {
	    ExtensionRequest er = extenionRequestRepository.findById(id).orElse(null);
	    if (er == null) return null;

	    Assurance assurance = null;
	    String memberName = null;
	    String departmentName = null;
	    String ministryName = null;
	    if (er.getAssuranceId() != null) {
	        assurance = assuranceRepository.findById(er.getAssuranceId()).orElse(null);
	        if (assurance != null && assurance.getMemberId() != null) {
	            memberName = personalDetailsRepository.findMemberNameByMemberId(assurance.getMemberId());
	            
	        }
	        if (assurance.getDepartmentId() != null) {
                departmentName = departmentRepository.findById(assurance.getDepartmentId())
                    .map(Departments::getDepartmentName)
                    .orElse(null);
            }
	        if (assurance.getMinistryId() != null) {
                ministryName = ministryRepository.findById(assurance.getDepartmentId())
                    .map(Ministries::getMinistryName)
                    .orElse(null);
            }
	    }

	    return ExtensionRequestResponseDTO.builder()
	            .assuranceNo(assurance != null ? assurance.getAssuranceNo() : null)
	            .extensionNo(er.getExtensionNo())
	            .questionNo(assurance.getQuestionNo())
	            .subject(assurance.getSubject())
	            .questionNo(assurance.getQuestionNo())
	            .status(codeRepository.findCodeById(assurance != null ? assurance.getStatusId() : null))
	            .lokSabhaSessionNo(assurance != null
	                    ? lssRepo.findSessionNumberById(assurance.getLokSabhaSessionNo())
	                    : null)
	            .lokSabhaNo(assurance != null
	                    ? lssRepo.findLokSabhaNumberById(assurance.getLokSabhaNo())
	                    : null)
	            .rajyaSabhaSessionNumber(assurance != null
	                    ? rssRepo.findSessionNumberById(assurance.getRajyaSabhaSessionNo())
	                    : null)
	            .remarks(er.getRemarks())
	            .extGrantedTillDate(er.getExtGrantedTillDate())
	            .extRequestedTillDate(er.getExtRequestedTillDate())
	            .ministryRefDate(er.getMinistryRefDate())
	            .memberName(memberName)
	            .departmentName(departmentName)
	            .ministryName(ministryName)
	            .id(er.getId())
	            .ministryRefNo(er.getMinistryRefNo())
	            .build();
	}

	@Override
	public ExtensionRequestResponseDTO updateExtensionRequest(Long id, ExtensionRequestDTO requestDTO) {
		 ExtensionRequest er = extenionRequestRepository.findById(id).orElse(null);
		    if (er == null) return null;

		    Assurance assurance = null;
		    if (er.getAssuranceId() != null) {
		        assurance = assuranceRepository.findById(er.getAssuranceId()).orElse(null);
		    }

		    // Update ExtensionRequest fields only if non-null
		    if (requestDTO.getExtensionNo() != null) {
		        er.setExtensionNo(requestDTO.getExtensionNo());
		    }
		    if (requestDTO.getRemarks() != null) {
		        er.setRemarks(requestDTO.getRemarks());
		    }
		    if (requestDTO.getExtGrantedTillDate() != null) {
		        er.setExtGrantedTillDate(requestDTO.getExtGrantedTillDate());
		    }
		    if (requestDTO.getExtRequestedTillDate() != null) {
		        er.setExtRequestedTillDate(requestDTO.getExtRequestedTillDate());
		    }
		    if (requestDTO.getMinistryRefDate() != null) {
		        er.setMinistryRefDate(requestDTO.getMinistryRefDate());
		    }
		    if (requestDTO.getMinistryRefNo() != null) {
		        er.setMinistryRefNo(requestDTO.getMinistryRefNo());
		    }

		    // Optionally update status in Assurance
		    if (assurance != null && requestDTO.getStatusId() != null) {
		        assurance.setStatusId(requestDTO.getStatusId());
		        assuranceRepository.save(assurance);
		    }

		    extenionRequestRepository.save(er);

		    return getExtensionRequestById(id); // Reuse existing method to return DTO
		}


}
