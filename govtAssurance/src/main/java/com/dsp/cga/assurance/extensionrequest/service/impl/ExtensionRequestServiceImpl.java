package com.dsp.cga.assurance.extensionrequest.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
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

import com.dsp.cga.assurance.clubbedassurance.repository.ClubbedAssuranceRepository;
import com.dsp.cga.assurance.droppingrequest.repository.DroppingRequestRepositiry;
import com.dsp.cga.assurance.dto.CommonRequestResponseDTO;
import com.dsp.cga.assurance.dto.MemberDTO;
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
import com.dsp.cga.assurance.implementationrequest.repository.ImplementationRequestRepository;
import com.dsp.cga.assurance.otherRequests.repository.OtherRequestRepository;
import com.dsp.cga.assurance.repository.AssuranceRepository;
import com.dsp.cga.assurance.repository.CodeRepository;
import com.dsp.cga.assurance.repository.DepartmentRepository;
import com.dsp.cga.assurance.repository.MasterLSSRepository;
import com.dsp.cga.assurance.repository.MastersRSSRepository;
import com.dsp.cga.assurance.repository.MinistryRepository;
import com.dsp.cga.assurance.repository.PersonalDetailsRepository;
import com.dsp.cga.assurance.supplementry.entity.ClubbedMemberInfo;
import com.dsp.cga.assurance.supplementry.entity.Supplementary;
import com.dsp.cga.exception.ResourceNotFoundException;
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
	
	@Value("${member.memberServiceUrl}")
	private String memberServiceUrl;

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
	
    @Autowired
    private OtherRequestRepository otherRequestRepository;
    
    @Autowired
    private ImplementationRequestRepository implementationRequestRepository;

@Autowired
private DroppingRequestRepositiry droppingRequestRepository;

@Autowired
private ClubbedAssuranceRepository clubbedAssuranceRepository;

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
	public List<ExtensionRequestResponseDTO> getFilteredExtensions(Long sessionNo, String statusForMinistry,
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

	        if (statusForMinistry != null) {
	            predicates.add(cb.equal(root.get("statusForMinistry"), statusForMinistry));
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
	    return filteredList.stream()
	    	    .sorted(Comparator.comparing(ExtensionRequest::getCreatedAt).reversed()) // sort before mapping
.map(er -> {
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
	                .subject(assurance.getSubject())
	                .remarks(er.getRemarks())
	                .extGrantedTillDate(er.getExtGrantedTillDate())
	                .id(er.getId())
	                .extRequestedTillDate(er.getExtRequestedTillDate())
	                .ministryRefDate(er.getMinistryRefDate())
	                .ministryRefNo(er.getMinistryRefNo())
	                .statusForMinistry(er.getStatusForMinistry())
	                .build();
	    })
	    		.collect(Collectors.toList());
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

	    List<MemberDTO> clubbedMembersData = new ArrayList<>();
	    List<MemberDTO> supplementaryClubbedMembersData = new ArrayList<>();

	    if (er.getAssuranceId() != null) {
	        assurance = assuranceRepository.findById(er.getAssuranceId()).orElse(null);
	        if (assurance != null) {
	            if (assurance.getMemberId() != null) {
	                memberName = personalDetailsRepository.findMemberNameByMemberId(assurance.getMemberId());
	            }

	            if (assurance.getDepartmentId() != null) {
	                departmentName = departmentRepository.findById(assurance.getDepartmentId())
	                        .map(Departments::getDepartmentName).orElse(null);
	            }

	            if (assurance.getMinistryId() != null) {
	                ministryName = ministryRepository.findById(assurance.getMinistryId())
	                        .map(Ministries::getMinistryName).orElse(null);
	            }

	            // ✅ Clubbed members from Assurance
	            if (assurance.getClubbedMembers() != null) {
	                clubbedMembersData = fetchClubbedMembersData(assurance.getClubbedMembers());
	            }

	            // ✅ Supplementary clubbed members
	            if (assurance.getSupplementary() != null && !assurance.getSupplementary().isEmpty()) {
	                List<Long> supplementaryMemberIds = new ArrayList<>();
	                for (Supplementary supp : assurance.getSupplementary()) {
	                    if (supp.getClubbedMembers() != null) {
	                        for (ClubbedMemberInfo info : supp.getClubbedMembers()) {
	                        	if (info != null && info.getClubbedMember() != null) {
	                        	    supplementaryMemberIds.add(info.getClubbedMember());
	                        	}

	                        }
	                    }
	                }
	                supplementaryClubbedMembersData = fetchClubbedMembersData(supplementaryMemberIds);
	            }
	        }
	    }

	    return ExtensionRequestResponseDTO.builder()
	            .assuranceNo(assurance != null ? assurance.getAssuranceNo() : null)
	            .extensionNo(er.getExtensionNo())
	            .questionNo(assurance != null ? assurance.getQuestionNo() : null)
	            .subject(assurance != null ? assurance.getSubject() : null)
	            .status(codeRepository.findCodeById(assurance != null ? assurance.getStatusId() : null))
	            .lokSabhaSessionNo(assurance != null ? lssRepo.findSessionNumberById(assurance.getLokSabhaSessionNo()) : null)
	            .lokSabhaNo(assurance != null ? lssRepo.findLokSabhaNumberById(assurance.getLokSabhaNo()) : null)
	            .rajyaSabhaSessionNumber(assurance != null ? rssRepo.findSessionNumberById(assurance.getRajyaSabhaSessionNo()) : null)
	            .remarks(er.getRemarks())
	            .extGrantedTillDate(er.getExtGrantedTillDate())
	            .extRequestedTillDate(er.getExtRequestedTillDate())
	            .ministryRefDate(er.getMinistryRefDate())
	            .memberName(memberName)
	            .departmentName(departmentName)
	            .ministryName(ministryName)
	            .id(er.getId())
	            .ministryRefNo(er.getMinistryRefNo())
	            .extensionFiles(er.getExtensionFiles())
	            .clubbedMembersData(clubbedMembersData)
	            .supplementaryClubbedMembersData(supplementaryClubbedMembersData) // ✅ Added
	            .build();
	}


	private List<MemberDTO> fetchClubbedMembersData(List<Long> clubbedMembers) {
		// Check if the list is not empty
		if (clubbedMembers == null || clubbedMembers.isEmpty()) {
			return new ArrayList<>(); // Return an empty list if no members
		}

		List<MemberDTO> memberDTOs = new ArrayList<>();

		// Iterate over each member ID in the clubbedMembers list
		for (Long memberId : clubbedMembers) {
			// Fetch the MemberDTO for each memberId
			MemberDTO memberDTO = fetchMemberData(memberId);
			if (memberDTO != null) {
				 memberDTO.generateMemberName();
				memberDTOs.add(memberDTO); // Add the memberDTO to the list
			}
		}

		return memberDTOs; // Return the list of MemberDTOs
	}
	private MemberDTO fetchMemberData(Long memberId) {
		String url = memberServiceUrl + memberId;
		log.debug("Fetching Member by memberId from URL: {}", url);

		try {
			ResponseEntity<GenericResponse<MemberDTO>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<GenericResponse<MemberDTO>>() {
					});

			if (response == null || response.getBody() == null || response.getBody().getData() == null) {
				log.error("Response or body is null when fetching Member for memberId: {}", memberId);
				throw new RuntimeException("Failed to fetch Member, response is null");
			}

			MemberDTO memberDTO = response.getBody().getData();
			memberDTO.generateMemberName();
			log.debug("Fetched MemberDTO: {}", memberDTO);
			return memberDTO;

		} catch (ResourceNotFoundException e) {
			log.error("Resource not found for memberId: {}", memberId);
			throw e;
		} catch (Exception e) {
			log.error("Error fetching Member for memberId: {}", memberId, e);
			throw new RuntimeException("Failed to fetch Member data", e);
		}
	}

	@Override
	public ExtensionRequestResponseDTO updateExtensionRequest(Long id, ExtensionRequestDTO requestDTO) {
		 ExtensionRequest er = extenionRequestRepository.findById(id).orElse(null);
		    if (er == null) return null;
		    
		    if (er.getStatusForMinistry() == null || !er.getStatusForMinistry().equalsIgnoreCase("Returned")) {
		        throw new IllegalStateException("Update not allowed. statusForMinistry must be 'Returned'.");
		    }

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

	@Override
	public List<CommonRequestResponseDTO> getAllRequests() {
	    List<CommonRequestResponseDTO> response = new ArrayList<>();
	    Long houseId = getHouseId(); // Assume this fetches current houseId

	    // ExtensionRequest
	    extenionRequestRepository.findAll().forEach(req -> {
	        if (Boolean.TRUE.equals(req.getFromMinistry()) &&
	                ("Submitted".equalsIgnoreCase(req.getStatusForMinistry()) ||
	                 "Returned".equalsIgnoreCase(req.getStatusForMinistry()))) {

	            Assurance assurance = getAssurance(req.getAssuranceId());
	            if (isValidForHouse(houseId, assurance)) {
	                response.add(buildDTO(
	                        req.getId(), "ExtensionRequest", req.getAssuranceId(),
	                        req.getSubject(), req.getRemarks(), req.getFromMinistry(),
	                        req.getStatusForMinistry(), assurance,
	                        req.getCreatedAt() != null ? req.getCreatedAt().toLocalDate() : null
	                ));
	            }
	        }
	    });

	    // OtherRequests
	    otherRequestRepository.findAll().forEach(req -> {
	        if (Boolean.TRUE.equals(req.getFromMinistry()) &&
	                ("Submitted".equalsIgnoreCase(req.getStatusForMinistry()) ||
	                 "Returned".equalsIgnoreCase(req.getStatusForMinistry()))) {

	            Assurance assurance = getAssurance(req.getAssuranceId());
	            if (isValidForHouse(houseId, assurance)) {
	                response.add(buildDTO(
	                        req.getId(), "OtherRequests", req.getAssuranceId(),
	                        req.getSubject(), req.getRemarks(), req.getFromMinistry(),
	                        req.getStatusForMinistry(), assurance,
	                        req.getCreatedAt() != null ? req.getCreatedAt().toLocalDate() : null
	                ));
	            }
	        }
	    });

	    // ClubbedAssurance
	    clubbedAssuranceRepository.findAll().forEach(req -> {
	        if (Boolean.TRUE.equals(req.getFromMinistry()) &&
	                ("Submitted".equalsIgnoreCase(req.getStatusForMinistry()) ||
	                 "Returned".equalsIgnoreCase(req.getStatusForMinistry()))) {

	            Assurance assurance = getAssurance(req.getAssuranceId());
	            if (isValidForHouse(houseId, assurance)) {
	                response.add(buildDTO(
	                        req.getId(), "ClubbedAssurance", req.getAssuranceId(),
	                        null, null, req.getFromMinistry(),
	                        req.getStatusForMinistry(), assurance,
	                        req.getCreatedAt() != null ? req.getCreatedAt().toLocalDate() : null
	                ));
	            }
	        }
	    });

	    // DroppingRequest
	    droppingRequestRepository.findAll().forEach(req -> {
	        if (Boolean.TRUE.equals(req.getFromMinistry()) &&
	                ("Submitted".equalsIgnoreCase(req.getStatusForMinistry()) ||
	                 "Returned".equalsIgnoreCase(req.getStatusForMinistry()))) {

	            Assurance assurance = getAssurance(req.getAssuranceId());
	            if (isValidForHouse(houseId, assurance)) {
	                response.add(buildDTO(
	                        req.getId(), "DroppingRequest", req.getAssuranceId(),
	                        req.getSubject(), req.getRemarks(), req.getFromMinistry(),
	                        req.getStatusForMinistry(), assurance,
	                        req.getCreatedAt() != null ? req.getCreatedAt().toLocalDate() : null
	                ));
	            }
	        }
	    });

	    return response;
	}

	private boolean isValidForHouse(Long houseId, Assurance assurance) {
	    if (assurance == null) return false;

	    if (houseId == 1) {
	        return assurance.getRajyaSabhaSessionNo() != null;
	    } else if (houseId == 2) {
	        return assurance.getLokSabhaSessionNo() != null;
	    }

	    return true; // If houseId is null or not 1/2, allow all.
	}



	    private Assurance getAssurance(Long assuranceId) {
	        return assuranceId != null ? assuranceRepository.findById(assuranceId).orElse(null) : null;
	    }

	    private CommonRequestResponseDTO buildDTO(Long id, String requestType, Long assuranceId,
	                                              String subject, String remarks, Boolean fromMinistry,
	                                              String statusForMinistry, Assurance assurance, LocalDate createdDate) {

	        String status = codeRepository.findCodeById(assurance != null ? assurance.getStatusId() : null);
	        String lokSabhaSessionNumber = assurance != null ? lssRepo.findSessionNumberById(assurance.getLokSabhaSessionNo()) : null;
	        String lokSabhaNumber = assurance != null ? lssRepo.findLokSabhaNumberById(assurance.getLokSabhaNo()) : null;
	        String rajyasabhaSessionNumber = assurance != null ? rssRepo.findSessionNumberById(assurance.getRajyaSabhaSessionNo()) : null;

	        String memberName = null;
	        String departmentName = null;
	        String ministryName = null;

	        if (assurance != null) {
	            if (assurance.getMemberId() != null) {
	                memberName = personalDetailsRepository.findMemberNameByMemberId(assurance.getMemberId());
	            }
	            if (assurance.getDepartmentId() != null) {
	                departmentName = departmentRepository.findById(assurance.getDepartmentId())
	                        .map(Departments::getDepartmentName)
	                        .orElse(null);
	            }
	            if (assurance.getMinistryId() != null) {
	                ministryName = ministryRepository.findById(assurance.getMinistryId())
	                        .map(Ministries::getMinistryName)
	                        .orElse(null);
	            }
	        }

	        return CommonRequestResponseDTO.builder()
	                .id(id)
	                .requestType(requestType)
	                .assuranceId(assuranceId)
	                .subject(subject)
	                .remarks(remarks)
	                .fromMinistry(fromMinistry)
	                .statusForMinistry("Submitted".equalsIgnoreCase(statusForMinistry) ? "Received" : statusForMinistry)
	                .date(createdDate)
	                .status(status)
	                .lokSabhaSessionNumber(lokSabhaSessionNumber)
	                .lokSabhaNumber(lokSabhaNumber)
	                .rajyaSabhaSessionNumber(rajyasabhaSessionNumber)
	                .memberName(memberName)
	                .departmentName(departmentName)
	                .ministryName(ministryName)
	                .build();
	    }





	    @Override
	    public CommonRequestResponseDTO getRequestById(Long id, String requestType) {
	        Long houseId = getHouseId(); // Current houseId

	        // Fetch assurance details by ID and return a partial DTO for merging
	        Function<Assurance, CommonRequestResponseDTO> buildAssuranceDetails = (assurance) -> {
	            if (assurance == null) return null;

	            String status = codeRepository.findCodeById(assurance.getStatusId());
	            String lokSabhaSessionNumber = lssRepo.findSessionNumberById(assurance.getLokSabhaSessionNo());
	            String lokSabhaNumber = lssRepo.findLokSabhaNumberById(assurance.getLokSabhaNo());
	            String rajyaSabhaSessionNumber = rssRepo.findSessionNumberById(assurance.getRajyaSabhaSessionNo());
                 
	            String memberName = null;
	            String departmentName = null;
	            String ministryName = null;

	            if (assurance.getMemberId() != null) {
	                memberName = personalDetailsRepository.findMemberNameByMemberId(assurance.getMemberId());
	            }
	            if (assurance.getDepartmentId() != null) {
	                departmentName = departmentRepository.findById(assurance.getDepartmentId())
	                        .map(Departments::getDepartmentName)
	                        .orElse(null);
	            }
	            if (assurance.getMinistryId() != null) {
	                ministryName = ministryRepository.findById(assurance.getMinistryId())
	                        .map(Ministries::getMinistryName)
	                        .orElse(null);
	            }

	            return CommonRequestResponseDTO.builder()
	                    .status(status)
	                    .lokSabhaNumber(lokSabhaNumber)
	                    .lokSabhaSessionNumber(lokSabhaSessionNumber)
	                    .rajyaSabhaSessionNumber(rajyaSabhaSessionNumber)
	                    .memberName(memberName)
	                    .departmentName(departmentName)
	                    .ministryName(ministryName)
	                    .build();
	        };

	        switch (requestType) {
	            case "ExtensionRequest":
	                return extenionRequestRepository.findById(id)
	                    .filter(req -> Boolean.TRUE.equals(req.getFromMinistry()) &&
	                            ("Submitted".equalsIgnoreCase(req.getStatusForMinistry()) ||
	                             "Returned".equalsIgnoreCase(req.getStatusForMinistry())))
	                    .map(req -> {
	                        Assurance assurance = getAssurance(req.getAssuranceId());
	                        if (isValidForHouse(houseId, assurance)) {
	                            CommonRequestResponseDTO assuranceDetails = buildAssuranceDetails.apply(assurance);
	                            return CommonRequestResponseDTO.builder()
	                                    .id(req.getId())
	                                    .requestType("ExtensionRequest")
	                                    .assuranceId(req.getAssuranceId())
	                                    .subject(req.getSubject())
	                                    .remarks(req.getRemarks())
	                                    .extensionNo(req.getExtensionNo())
	                                    .extRequestedTillDate(req.getExtRequestedTillDate())
	                                    .extGrantedTillDate(req.getExtGrantedTillDate())
	                                    .ministryRefNo(req.getMinistryRefNo())
	                                    .ministryRefDate(req.getMinistryRefDate())
	                                    .statusId(req.getStatusId())
	                                    .statusForMinistry(req.getStatusForMinistry())
	                                    .draftStatus(req.getDraftStatus())
	                                   .extensionFiles(req.getExtensionFiles())
	                                    .fromMinistry(req.getFromMinistry())
	                                    .date(req.getCreatedAt() != null ? req.getCreatedAt().toLocalDate() : null)
	                                    // Merge assurance details
	                                    .status(assuranceDetails != null ? assuranceDetails.getStatus() : null)
	                                    .lokSabhaNumber(assuranceDetails != null ? assuranceDetails.getLokSabhaNumber() : null)
	                                    .lokSabhaSessionNumber(assuranceDetails != null ? assuranceDetails.getLokSabhaSessionNumber() : null)
	                                    .rajyaSabhaSessionNumber(assuranceDetails != null ? assuranceDetails.getRajyaSabhaSessionNumber() : null)
	                                    .memberName(assuranceDetails != null ? assuranceDetails.getMemberName() : null)
	                                    .departmentName(assuranceDetails != null ? assuranceDetails.getDepartmentName() : null)
	                                    .ministryName(assuranceDetails != null ? assuranceDetails.getMinistryName() : null)
	                                    .build();
	                        }
	                        return null;
	                    })
	                    .orElse(null);

	            case "OtherRequests":
	                return otherRequestRepository.findById(id)
	                    .filter(req -> Boolean.TRUE.equals(req.getFromMinistry()) &&
	                            ("Submitted".equalsIgnoreCase(req.getStatusForMinistry()) ||
	                             "Returned".equalsIgnoreCase(req.getStatusForMinistry())))
	                    .map(req -> {
	                        Assurance assurance = getAssurance(req.getAssuranceId());
	                        if (isValidForHouse(houseId, assurance)) {
	                            CommonRequestResponseDTO assuranceDetails = buildAssuranceDetails.apply(assurance);
	                            return CommonRequestResponseDTO.builder()
	                                    .id(req.getId())
	                                    .requestType("OtherRequests")
	                                    .assuranceId(req.getAssuranceId())
	                                    .assuranceNo(assurance.getAssuranceNo())
	                                    .subject(req.getSubject())
	                                    .remarks(req.getRemarks())
	                                    .requestedDate(req.getRequestedDate())
	                                    .statusId(req.getStatusId())
	                                    .statusForMinistry(req.getStatusForMinistry())
	                                    .fileUrl(req.getFileUrl())
	                                    .fromMinistry(req.getFromMinistry())
	                                    .date(req.getCreatedAt() != null ? req.getCreatedAt().toLocalDate() : null)
	                                    .status(assuranceDetails != null ? assuranceDetails.getStatus() : null)
	                                    .lokSabhaNumber(assuranceDetails != null ? assuranceDetails.getLokSabhaNumber() : null)
	                                    .lokSabhaSessionNumber(assuranceDetails != null ? assuranceDetails.getLokSabhaSessionNumber() : null)
	                                    .rajyaSabhaSessionNumber(assuranceDetails != null ? assuranceDetails.getRajyaSabhaSessionNumber() : null)
	                                    .memberName(assuranceDetails != null ? assuranceDetails.getMemberName() : null)
	                                    .departmentName(assuranceDetails != null ? assuranceDetails.getDepartmentName() : null)
	                                    .ministryName(assuranceDetails != null ? assuranceDetails.getMinistryName() : null)
	                                    .build();
	                        }
	                        return null;
	                    })
	                    .orElse(null);

	            case "DroppingRequest":
	                return droppingRequestRepository.findById(id)
	                    .filter(req -> Boolean.TRUE.equals(req.getFromMinistry()) &&
	                            ("Submitted".equalsIgnoreCase(req.getStatusForMinistry()) ||
	                             "Returned".equalsIgnoreCase(req.getStatusForMinistry())))
	                    .map(req -> {
	                        Assurance assurance = getAssurance(req.getAssuranceId());
	                        if (isValidForHouse(houseId, assurance)) {
	                            CommonRequestResponseDTO assuranceDetails = buildAssuranceDetails.apply(assurance);
	                            return CommonRequestResponseDTO.builder()
	                                    .id(req.getId())
	                                    .requestType("DroppingRequest")
	                                    .assuranceId(req.getAssuranceId())
	                                    .subject(req.getSubject())
	                                    .droppingType(codeRepository.findCodeById(req.getDroppingType()))
	                                    .supplementaryId(req.getSupplementaryId())
	                                    .partId(req.getPartId())
	                                    .dropRequestDate(req.getDropRequestDate())
	                                    .actualDropDate(req.getActualDropDate())
	                                    .ministryRefNo(req.getMinistryRefNo())
	                                    .ministryRefDate(req.getMinistryRefDate())
	                                    .acceptedId(req.getAcceptedId())
	                                    .type(req.getType())
	                                    .communicationDate(req.getCommunicationDate())
	                                    .droppingFiles(req.getDroppingFiles())
	                                    .session(req.getSession())
	                                    .meetingNo(req.getMeetingNo())
	                                    .meetingDate(req.getMeetingDate())
	                                    .subject(assurance.getSubject())
	                                    .questionNo(assurance.getQuestionNo())
	                                    .assuranceNo(assurance.getAssuranceNo())
	                                    .remarks(req.getRemarks())
	                                    .statusForMinistry(req.getStatusForMinistry())
	                                    .droppingFiles(req.getDroppingFiles())
	                                    .draftStatus(req.getDraftStatus())
	                                    .fromMinistry(req.getFromMinistry())
	                                    .date(req.getCreatedAt() != null ? req.getCreatedAt().toLocalDate() : null)
	                                    .status(assuranceDetails != null ? assuranceDetails.getStatus() : null)
	                                    .lokSabhaNumber(assuranceDetails != null ? assuranceDetails.getLokSabhaNumber() : null)
	                                    .lokSabhaSessionNumber(assuranceDetails != null ? assuranceDetails.getLokSabhaSessionNumber() : null)
	                                    .rajyaSabhaSessionNumber(assuranceDetails != null ? assuranceDetails.getRajyaSabhaSessionNumber() : null)
	                                    .memberName(assuranceDetails != null ? assuranceDetails.getMemberName() : null)
	                                    .departmentName(assuranceDetails != null ? assuranceDetails.getDepartmentName() : null)
	                                    .ministryName(assuranceDetails != null ? assuranceDetails.getMinistryName() : null)
	                                    .build();
	                        }
	                        return null;
	                    })
	                    .orElse(null);

	            case "ClubbedAssurance":
	                return clubbedAssuranceRepository.findById(id)
	                    .filter(req -> Boolean.TRUE.equals(req.getFromMinistry()) &&
	                            ("Submitted".equalsIgnoreCase(req.getStatusForMinistry()) ||
	                             "Returned".equalsIgnoreCase(req.getStatusForMinistry())))
	                    .map(req -> {
	                        Assurance assurance = getAssurance(req.getAssuranceId());
	                        if (isValidForHouse(houseId, assurance)) {
	                            CommonRequestResponseDTO assuranceDetails = buildAssuranceDetails.apply(assurance);
	                            return CommonRequestResponseDTO.builder()
	                                    .id(req.getId())
	                                    .requestType("ClubbedAssurance")
	                                    .assuranceId(req.getAssuranceId())
	                                    .assuranceNo(assurance.getAssuranceNo())
	                                    .clubbedAssuranceIds(req.getClubbedAssuranceIds())
	                                    .fromMinistry(req.getFromMinistry())
	                                    .statusForMinistry(req.getStatusForMinistry())
	                                    .subject(assurance.getSubject())
	                                    .questionNo(assurance.getQuestionNo())
	                                    .date(req.getCreatedAt() != null ? req.getCreatedAt().toLocalDate() : null)
	                                    .status(assuranceDetails != null ? assuranceDetails.getStatus() : null)
	                                    .lokSabhaNumber(assuranceDetails != null ? assuranceDetails.getLokSabhaNumber() : null)
	                                    .lokSabhaSessionNumber(assuranceDetails != null ? assuranceDetails.getLokSabhaSessionNumber() : null)
	                                    .rajyaSabhaSessionNumber(assuranceDetails != null ? assuranceDetails.getRajyaSabhaSessionNumber() : null)
	                                    .memberName(assuranceDetails != null ? assuranceDetails.getMemberName() : null)
	                                    .departmentName(assuranceDetails != null ? assuranceDetails.getDepartmentName() : null)
	                                    .ministryName(assuranceDetails != null ? assuranceDetails.getMinistryName() : null)
	                                    .build();
	                        }
	                        return null;
	                    })
	                    .orElse(null);
	                
	            case "ImplementationRequest":
	                return implementationRequestRepository.findById(id)
	                    .filter(req -> Boolean.TRUE.equals(req.getFromMinistry()) &&
	                    		 (
	                    	                ("Submitted".equalsIgnoreCase(req.getStatusForMinistry()) ||
	                    	                "Returned".equalsIgnoreCase(req.getStatusForMinistry())) &&
	                    	                "CGA".equalsIgnoreCase(req.getReturnFrom())
	                    	            )
	                    	        )
	                    .map(req -> {
	                        Assurance assurance = getAssurance(req.getAssuranceId());
	                        if (isValidForHouse(houseId, assurance)) {
	                            CommonRequestResponseDTO assuranceDetails = buildAssuranceDetails.apply(assurance);
	                            return CommonRequestResponseDTO.builder()
	                                    .id(req.getId())
	                                    .requestType("ImplementationRequest")
	                                    .assuranceId(req.getAssuranceId())
	                                    .assuranceNo(assurance.getAssuranceNo())
	                                    .fromMinistry(req.getFromMinistry())
	                                    .dueDateOfImplementation(req.getDueDateOfImplementation())
	                                    .dateOfImplementation(req.getDateOfImplementation())
	                                    .detailsOfImplementation(req.getDetailsOfImplementation())
	                                    .layingDate(req.getLayingDate())
	                                    .partId(req.getPartId())
	                                    .supplementaryId(req.getSupplementaryId())
	                                    .implementationReportFiles(req.getImplementationReportFiles())
	                                    .partReportFiles(req.getPartReportFiles())
	                                    .subject(assurance.getSubject())
	                                    .questionNo(assurance.getQuestionNo())
	                                    .statusForMinistry(req.getStatusForMinistry())
	                                    .date(req.getCreatedAt() != null ? req.getCreatedAt().toLocalDate() : null)
	                                    .status(assuranceDetails != null ? assuranceDetails.getStatus() : null)
	                                    .lokSabhaNumber(assuranceDetails != null ? assuranceDetails.getLokSabhaNumber() : null)
	                                    .lokSabhaSessionNumber(assuranceDetails != null ? assuranceDetails.getLokSabhaSessionNumber() : null)
	                                    .rajyaSabhaSessionNumber(assuranceDetails != null ? assuranceDetails.getRajyaSabhaSessionNumber() : null)
	                                    .memberName(assuranceDetails != null ? assuranceDetails.getMemberName() : null)
	                                    .departmentName(assuranceDetails != null ? assuranceDetails.getDepartmentName() : null)
	                                    .ministryName(assuranceDetails != null ? assuranceDetails.getMinistryName() : null)
	                                    .build();
	                        }
	                        return null;
	                    })
	                    .orElse(null);


	            default:
	                throw new IllegalArgumentException("Invalid request type: " + requestType);
	        }
	    }

	    @Override
	    public CommonRequestResponseDTO updateRequest(Long id, String requestType, CommonRequestResponseDTO updateDto) {
	    	System.out.println(requestType);
	        switch (requestType) {
	            case "ExtensionRequest":
	                return extenionRequestRepository.findById(id).map(entity -> {
	                    if (updateDto.getRemarks() != null) entity.setRemarks(updateDto.getRemarks());
	                  
	                    if (updateDto.getStatusForMinistry() != null) entity.setStatusForMinistry(updateDto.getStatusForMinistry());
	                  
	                    extenionRequestRepository.save(entity);
	                    return getRequestById(id, requestType);
	                }).orElse(null);

	            case "OtherRequests":
	                return otherRequestRepository.findById(id).map(entity -> {
	                    if (updateDto.getRemarks() != null) entity.setRemarks(updateDto.getRemarks());
	                    if (updateDto.getStatusForMinistry() != null) entity.setStatusForMinistry(updateDto.getStatusForMinistry());
	                    
	                    otherRequestRepository.save(entity);
	                    return getRequestById(id, requestType);
	                }).orElse(null);

	            case "DroppingRequest":
	                return droppingRequestRepository.findById(id).map(entity -> {
	                    
	                    if (updateDto.getRemarks() != null) entity.setRemarks(updateDto.getRemarks());
	                    if (updateDto.getStatusForMinistry() != null) entity.setStatusForMinistry(updateDto.getStatusForMinistry());
	                   
	                    droppingRequestRepository.save(entity);
	                    return getRequestById(id, requestType);
	                }).orElse(null);

	            case "ClubbedAssurance":
	                return clubbedAssuranceRepository.findById(id).map(entity -> {
	                    if (updateDto.getStatusForMinistry() != null) entity.setStatusForMinistry(updateDto.getStatusForMinistry());
	                    // Add more fields if required
	                    clubbedAssuranceRepository.save(entity);
	                    return getRequestById(id, requestType);
	                }).orElse(null);
	                
	            case "ImplementationRequest":  // New case added
	                return implementationRequestRepository.findById(id).map(entity -> {
	                    if (updateDto.getStatusForMinistry() != null) entity.setStatusForMinistry(updateDto.getStatusForMinistry());
	                    entity.setReturnFrom("CGA");
	                    implementationRequestRepository.save(entity);
	                    return getRequestById(id, requestType);
	                }).orElse(null);

	            default:
	                throw new IllegalArgumentException("Invalid request type: " + requestType);

	           
	        }
	        
	        
	    }


	   


}
