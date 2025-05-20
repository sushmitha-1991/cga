package com.dsp.cga.assurance.implementationrequest.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.dsp.cga.assurance.dto.AssuranceDTO;
import com.dsp.cga.assurance.dto.CommitteeDecisionDTO;
import com.dsp.cga.assurance.dto.ImplementationTypeDTO;
import com.dsp.cga.assurance.dto.MemberDTO;
import com.dsp.cga.assurance.dto.StatusDTO;
import com.dsp.cga.assurance.dto.UserDetailsDTO;
import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.assurance.entity.Departments;
import com.dsp.cga.assurance.entity.Ministries;
import com.dsp.cga.assurance.implementationrequest.dto.AssuranceImplementationDTO;
import com.dsp.cga.assurance.implementationrequest.dto.ImplementationRequestDTO;
import com.dsp.cga.assurance.implementationrequest.entity.ImplementationRequest;
import com.dsp.cga.assurance.implementationrequest.repository.ImplementationRequestRepository;
import com.dsp.cga.assurance.implementationrequest.service.ImplementationRequestService;
import com.dsp.cga.assurance.repository.AssuranceRepository;
import com.dsp.cga.assurance.repository.CodeRepository;
import com.dsp.cga.assurance.repository.DepartmentRepository;
import com.dsp.cga.assurance.repository.MasterLSSRepository;
import com.dsp.cga.assurance.repository.MastersRSSRepository;
import com.dsp.cga.assurance.repository.MinistryRepository;
import com.dsp.cga.assurance.repository.PersonalDetailsRepository;
import com.dsp.cga.assurance.service.impl.AssuranceServiceImpl;
import com.dsp.cga.exception.ResourceNotFoundException;
import com.dsp.cga.mapper.ImplementationRequestMapper;
import com.dsp.cga.response.GenericResponse;

import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;


@Service
public class ImplemantationRequestServiceImpl implements ImplementationRequestService {

	private static final Logger log = LoggerFactory.getLogger(ImplemantationRequestServiceImpl.class);

	@Value("${master.codeServiceUrl}")
	private String codeServiceUrl;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ImplementationRequestRepository implementationRequestRepository;
	
	@Autowired
	private AssuranceRepository assuranceRepository;
	
	@Autowired
	private MinistryRepository ministryRepository;
	
	@Autowired
	private MasterLSSRepository lssRepo;
	
	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;
	
	@Autowired
	private MastersRSSRepository rssRepo;
	
	@Autowired
	private CodeRepository codeRepository;

	@Override
	public ImplementationRequestDTO createImplementationRequest(ImplementationRequestDTO implementationRequestDTO) {
		ImplementationRequest implementationRequest = ImplementationRequestMapper.toEntity(implementationRequestDTO);

		// Save Assurance entity
		ImplementationRequest savedimplementationRequest = implementationRequestRepository.save(implementationRequest); // Save
																														// once,

		// Map entity back to DTO to return response
		ImplementationRequestDTO savedImplementationRequestDTO = ImplementationRequestMapper
				.toDTO(savedimplementationRequest);

		return savedImplementationRequestDTO;
	}


	
	@Override
	public GenericResponse<ImplementationRequestDTO> createOrUpdateImplementationRequest(
	        ImplementationRequestDTO implementationRequestDTO) {

	    Optional<ImplementationRequest> existingRequestOpt = implementationRequestRepository
	            .findByAssuranceId(implementationRequestDTO.getAssuranceId());

	    ImplementationRequest savedRequest;

	    if (existingRequestOpt.isPresent()) {
	        // Update existing entity
	        ImplementationRequest existingRequest = existingRequestOpt.get();
	        ImplementationRequestMapper.updateEntityFromDTO(implementationRequestDTO, existingRequest);
	        savedRequest = implementationRequestRepository.save(existingRequest);
	    } else {
	        // Create new entity
	        ImplementationRequest newRequest = ImplementationRequestMapper.toEntity(implementationRequestDTO);
	        savedRequest = implementationRequestRepository.save(newRequest);
	    }

	    // Fetch ImplementationType data from external service
	    if (implementationRequestDTO.getImplementationTypeId() != null) {
	        ImplementationTypeDTO implementationTypeDTO = fetchImplementationTypeData(
	                implementationRequestDTO.getImplementationTypeId());
	        implementationRequestDTO.setImplementationTypeDTO(implementationTypeDTO);
	    }
	    
	    if (implementationRequestDTO.getCommitteeDecisionId() != null) {
	        CommitteeDecisionDTO committeeDecisionDTO = fetchCommitteeDecisionData(
	        		implementationRequestDTO.getCommitteeDecisionId());
	        implementationRequestDTO.setCommitteeDecisionDTO(committeeDecisionDTO);
	        
	    }

	    // Convert saved entity to DTO
	    ImplementationRequestDTO savedDTO = ImplementationRequestMapper.toDTO(savedRequest);

	    // Set fetched ImplementationTypeDTO in the response DTO
	    savedDTO.setImplementationTypeDTO(implementationRequestDTO.getImplementationTypeDTO());
	    savedDTO.setCommitteeDecisionDTO(implementationRequestDTO.getCommitteeDecisionDTO());

	    // Return response
	    return new GenericResponse<>(HttpStatus.CREATED.value(), true, "Success", savedDTO);
	}






	@Override
	public ImplementationRequestDTO getImplementationRequestByAssuranceId(Long id) {
	    // Attempt to find the ImplementationRequest by Assurance ID
	    ImplementationRequest implementationRequest = implementationRequestRepository.findByAssuranceId(id).orElse(null);

	    // If no record is found, return a null DTO and log the message
	    if (implementationRequest == null) {
	        log.debug("No ImplementationRequest found for Assurance ID: {}", id);
	        return null;  // Return null to handle it at the controller level with custom message
	    }

	    // Log and map the entity to DTO if found
	    log.debug("Fetched ImplementationRequest: {}", implementationRequest);
	    
	    Assurance assurance = assuranceRepository.findById(id).orElse(null);
        String assuranceNo = (assurance != null) ? assurance.getAssuranceNo() : null;
        
	    ImplementationRequestDTO dto = ImplementationRequestMapper.toDTO(implementationRequest);
	    dto.setAssuranceNo(assuranceNo);
	    if (implementationRequest.getImplementationTypeId() != null) {
	    dto.setImplementationTypeDTO(fetchImplementationTypeData(implementationRequest.getImplementationTypeId()));
	    }
	    // Populate additional data if available
	    if (implementationRequest.getCommitteeDecisionId() != null) {
	    dto.setCommitteeDecisionDTO(fetchCommitteeDecisionData(implementationRequest.getCommitteeDecisionId()));
	    }
	    log.debug("Returning ImplementationRequestDTO: {}", dto);
	    return dto;
	}

	


	
	private CommitteeDecisionDTO fetchCommitteeDecisionData(Long committeeDecisionId) {
		
	    String serviceUrl = codeServiceUrl + committeeDecisionId;
	    log.debug("Calling master service URL for Committee Decision: {}", serviceUrl);

	    try {
	        ResponseEntity<GenericResponse<CommitteeDecisionDTO>> response = restTemplate.exchange(
	        		serviceUrl, HttpMethod.GET, null, 
	                new ParameterizedTypeReference<GenericResponse<CommitteeDecisionDTO>>() {}
	        );

	        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
	            log.warn("Failed to fetch data from master service for Committee Decision. Status: {} - {}",
	                    response.getStatusCode(), response.getBody());
	            return null;
	        }

	        CommitteeDecisionDTO committeeDecisionDTO = response.getBody().getData();
	        log.debug("Fetched CommitteeDecisionDTO: {}", committeeDecisionDTO);
           
	        return committeeDecisionDTO;
	    } catch (Exception e) {
	        log.error("Error occurred while fetching data from master service for Committee Decision ID: {}", committeeDecisionId, e);
	        return null;
	    }
	}
	
	private ImplementationTypeDTO fetchImplementationTypeData(Long implementationTypeId) {
		System.out.println(implementationTypeId);
	    String implementationTypeServiceUrl = codeServiceUrl  + implementationTypeId;
	    log.debug("Calling master service URL for ImplementationType: {}", implementationTypeServiceUrl);

	    ResponseEntity<GenericResponse<ImplementationTypeDTO>> response = restTemplate.exchange(
	            implementationTypeServiceUrl, 
	            HttpMethod.GET,
	            null, 
	            new ParameterizedTypeReference<GenericResponse<ImplementationTypeDTO>>() {}
	    );

	    if (!response.getStatusCode().is2xxSuccessful()) {
	        log.error("Failed to fetch data from master service for ImplementationType: {} - {}", 
	                  response.getStatusCode(), response.getBody());
	        throw new RuntimeException("Failed to fetch ImplementationType data. Status: " + response.getStatusCode());
	    }

	    ImplementationTypeDTO implementationTypeDTO = response.getBody().getData();
	    log.debug("Fetched ImplementationTypeDTO: {}", implementationTypeDTO);
	    return implementationTypeDTO;
	}



	
	@Override
	public List<AssuranceImplementationDTO> getAssuranceImplementations(Long sessionNo, LocalDate layingDate,
	        Long implementationTypeId, Long statusId, Integer ministryCode) {

	    // Initialize ministryId
	    final Long ministryId;
	    
	    // If ministryCode is not null, get ministryId
	    if (ministryCode != null) {
	        Ministries ministry = ministryRepository.findByMinistryCode(ministryCode);
	        if (ministry != null) {
	            ministryId = ministry.getId();
	        } else {
	            return List.of(); // No such ministry code
	        }
	    } else {
	        ministryId = null;
	    }

	    Long houseId = getHouseId();
	    if (sessionNo == null) {
	        sessionNo = (houseId == 1) ? rssRepo.findLatestByCreatedAt() : lssRepo.findLatestByCreatedAt();
	    }

	    // Making sessionNo final for usage inside the lambda
	    final Long finalSessionNo = sessionNo;

	    // ✅ Lok Sabha Logic
	    if (houseId == 2) {
	        // Define the Specification for Lok Sabha
	        Specification<Assurance> lokSabhaSpecification = (root, query, cb) -> {
	            List<Predicate> predicates = new ArrayList<>();
	            predicates.add(cb.isNotNull(root.get("dateOfImplementation")));
	            predicates.add(cb.equal(root.get("lokSabhaSessionNo"), finalSessionNo));

	            if (layingDate != null) {
	                predicates.add(cb.equal(root.get("layingDate"), layingDate));
	            }
	            if (statusId != null) {
	                predicates.add(cb.equal(root.get("statusId"), statusId));
	            }
	            if (ministryId != null) {
	                predicates.add(cb.equal(root.get("ministryId"), ministryId));
	            }

	            return cb.and(predicates.toArray(new Predicate[0]));
	        };

	        List<Assurance> assurances = assuranceRepository.findAll(lokSabhaSpecification);

	        return assurances.stream()
	                .map(assurance -> AssuranceImplementationDTO.builder()
	                        .assuranceNo(assurance.getAssuranceNo())
	                        .id(assurance.getId())
	                        .status(codeRepository.findCodeById(assurance.getStatusId()))
	                        .lokSabhaNumber(lssRepo.findLokSabhaNumberById(assurance.getLokSabhaNo()))
	                        .lokSabhaSessionNumber(lssRepo.findSessionNumberById(assurance.getLokSabhaSessionNo()))
	                        .dueDateOfImplementation(assurance.getDueDateOfImplementation())
	                        .detailsOfImplementation(assurance.getDetailsOfImplementation())
	                        .questionNo(assurance.getQuestionNo())
	                        .dateOfImplementation(assurance.getDateOfImplementation())
	                        .build())
	                .collect(Collectors.toList());
	    }

	    // ✅ Rajya Sabha Logic
	    Specification<ImplementationRequest> rajyaSabhaSpecification = ImplementationRequestSpecification.filterByCriteria(
	            finalSessionNo, layingDate, implementationTypeId, statusId, ministryId, houseId
	    );

	    List<ImplementationRequest> implementationRequests = implementationRequestRepository.findAll(rajyaSabhaSpecification);
	    if (CollectionUtils.isEmpty(implementationRequests)) {
	        return List.of();
	    }

	    return implementationRequests.stream()
	            .map(implementationRequest -> {
	                Assurance assurance = assuranceRepository.findById(implementationRequest.getAssuranceId()).orElse(null);
	                if (assurance == null || (houseId == 1 && assurance.getRajyaSabhaSessionNo() == null)) {
	                    return null;
	                }

	                return AssuranceImplementationDTO.builder()
	                        .assuranceNo(assurance.getAssuranceNo())
	                        .status(codeRepository.findCodeById(assurance.getStatusId()))
	                        .rajyaSabhaSessionNumber(rssRepo.findSessionNumberById(assurance.getRajyaSabhaSessionNo()))
	                        .dueDateOfImplementation(assurance.getDueDateOfImplementation())
	                        .detailsOfImplementation(assurance.getDetailsOfImplementation())
	                        .questionNo(assurance.getQuestionNo())
	                        .dateOfImplementation(assurance.getDateOfImplementation())
	                        .supplementaryId(implementationRequest.getSupplementaryId())
	                        .id(implementationRequest.getId())
	                        .partId(implementationRequest.getPartId())
	                        .reason(implementationRequest.getReason())
	                        .layingDate(implementationRequest.getLayingDate())
	                        .implementationTypeId(codeRepository.findCodeById(implementationRequest.getImplementationTypeId()))
	                        .build();
	            })
	            .filter(Objects::nonNull)
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



//	@Override
//	public AssuranceImplementationDTO getImplementationDetailById(Long id) {
//	    Optional<ImplementationRequest> optionalRequest = implementationRequestRepository.findById(id);
//	    if (optionalRequest.isEmpty()) return null;
//
//	    ImplementationRequest implementationRequest = optionalRequest.get();
//	   // Assurance assurance = null;
//	    String memberName = null;
//	    String departmentName = null;
//	    String ministryName = null;
//
//	    Assurance assurance = assuranceRepository.findById(implementationRequest.getAssuranceId()).orElse(null);
//	    if (assurance != null && assurance.getMemberId() != null) {
//            memberName = personalDetailsRepository.findMemberNameByMemberId(assurance.getMemberId());
//            
//        }
//        if (assurance.getDepartmentId() != null) {
//            departmentName = departmentRepository.findById(assurance.getDepartmentId())
//                .map(Departments::getDepartmentName)
//                .orElse(null);
//        }
//        if (assurance.getMinistryId() != null) {
//            ministryName = ministryRepository.findById(assurance.getDepartmentId())
//                .map(Ministries::getMinistryName)
//                .orElse(null);
//        }
//	    if (assurance == null) return null;
//	    
//
//	    Long houseId = getHouseId();
//
//	    AssuranceImplementationDTO.AssuranceImplementationDTOBuilder builder = AssuranceImplementationDTO.builder()
//	            .assuranceNo(assurance.getAssuranceNo())
//	            .memberName(memberName)
//	            .ministryName(ministryName)
//	            .departmentName(departmentName)
//	            .status(codeRepository.findCodeById(assurance.getStatusId()))
//	            .dueDateOfImplementation(assurance.getDueDateOfImplementation())
//	            .detailsOfImplementation(assurance.getDetailsOfImplementation())
//	            .subject(assurance.getSubject())
//	            .questionNo(assurance.getQuestionNo())
//	            .dateOfImplementation(assurance.getDateOfImplementation())
//	            .supplementaryId(implementationRequest.getSupplementaryId())
//	            .partId(implementationRequest.getPartId())
//	            .id(implementationRequest.getId())
//	            .reason(implementationRequest.getReason())
//	            .layingDate(implementationRequest.getLayingDate())
//	            .implementationTypeId(codeRepository.findCodeById(implementationRequest.getImplementationTypeId()));
//
//	    if (houseId == 1) {
//	        builder.rajyaSabhaSessionNumber(
//	                rssRepo.findSessionNumberById(assurance.getRajyaSabhaSessionNo()));
//	    } else {
//	        builder.lokSabhaSessionNumber(
//	                lssRepo.findSessionNumberById(assurance.getLokSabhaSessionNo()))
//	               .lokSabhaNumber(
//	                lssRepo.findLokSabhaNumberById(assurance.getLokSabhaNo()));
//	    }
//
//	    return builder.build();
//	}
	
	@Override
	public AssuranceImplementationDTO getImplementationDetailById(Long id) {
	    Long houseId = getHouseId();

	    Assurance assurance;
	    ImplementationRequest implementationRequest = null;

	    if (houseId == 2) {
	        // Lok Sabha: ID is from Assurance table
	        assurance = assuranceRepository.findById(id).orElse(null);
	        if (assurance == null) return null;
	    } else {
	        // Rajya Sabha: ID is from ImplementationRequest table
	        Optional<ImplementationRequest> optionalRequest = implementationRequestRepository.findById(id);
	        if (optionalRequest.isEmpty()) return null;
	        implementationRequest = optionalRequest.get();

	        assurance = assuranceRepository.findById(implementationRequest.getAssuranceId()).orElse(null);
	        if (assurance == null) return null;
	    }

	    // Common values
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
	        ministryName = ministryRepository.findById(assurance.getDepartmentId())
	            .map(Ministries::getMinistryName)
	            .orElse(null);
	    }

	    AssuranceImplementationDTO.AssuranceImplementationDTOBuilder builder = AssuranceImplementationDTO.builder()
	        .assuranceNo(assurance.getAssuranceNo())
	        .memberName(memberName)
	        .ministryName(ministryName)
	        .departmentName(departmentName)
	        .status(codeRepository.findCodeById(assurance.getStatusId()))
	        .dueDateOfImplementation(assurance.getDueDateOfImplementation())
	        .subject(assurance.getSubject())
	        .questionNo(assurance.getQuestionNo())
	        .dateOfImplementation(assurance.getDateOfImplementation())
	        .detailsOfImplementation(assurance.getDetailsOfImplementation());

	    if (houseId == 2) {
	        // Lok Sabha: direct from Assurance
	        builder
	            .remarks(assurance.getRemarks())
	            .lokSabhaSessionNumber(lssRepo.findSessionNumberById(assurance.getLokSabhaSessionNo()))
	            .lokSabhaNumber(lssRepo.findLokSabhaNumberById(assurance.getLokSabhaNo()))
	            .id(assurance.getId()); // using assuranceId
	    } else {
	        // Rajya Sabha: from ImplementationRequest
	        builder
	            .reason(implementationRequest.getReason())
	            .layingDate(implementationRequest.getLayingDate())
	            .implementationTypeId(codeRepository.findCodeById(implementationRequest.getImplementationTypeId()))
	            .supplementaryId(implementationRequest.getSupplementaryId())
	            .partId(implementationRequest.getPartId())
	            .rajyaSabhaSessionNumber(rssRepo.findSessionNumberById(assurance.getRajyaSabhaSessionNo()))
	            .id(implementationRequest.getId());
	    }

	    return builder.build();
	}




	@Override
	public AssuranceImplementationDTO updateImplementationRequest(Long id, ImplementationRequestDTO dto) {
	    Optional<ImplementationRequest> optionalRequest = implementationRequestRepository.findById(id);
	    if (optionalRequest.isEmpty()) return null;

	    ImplementationRequest implReq = optionalRequest.get();

	    Assurance assurance = assuranceRepository.findById(implReq.getAssuranceId()).orElse(null);
	    if (assurance == null) return null;

	    // Update ImplementationRequest fields if provided
	    if (dto.getPartId() != null) {
	        implReq.setPartId(dto.getPartId());
	    }
	    if (dto.getSupplementaryId() != null) {
	        implReq.setSupplementaryId(dto.getSupplementaryId());
	    }
	    if (dto.getReason() != null) {
	        implReq.setReason(dto.getReason());
	    }
	    if (dto.getLayingDate() != null) {
	        implReq.setLayingDate(dto.getLayingDate());
	    }
	    if (dto.getImplementationTypeId() != null) {
	        implReq.setImplementationTypeId(dto.getImplementationTypeId());
	    }
	    
	    if (dto.getImplementationReportFiles() != null) {
	        implReq.setImplementationReportFiles(dto.getImplementationReportFiles());
	    }
	    
	    if (dto.getPartReportFiles() != null) {
	        implReq.setPartReportFiles(dto.getPartReportFiles());
	    }

	    // Update Assurance fields if provided
//	    if (dto.getStatusId() != null) {
//	        assurance.setStatusId(dto.getStatusId());
//	    }
//	    if (dto.getDueDateOfImplementation() != null) {
//	        assurance.setDueDateOfImplementation(dto.getDueDateOfImplementation());
//	    }
//	    if (dto.getDetailsOfImplementation() != null) {
//	        assurance.setDetailsOfImplementation(dto.getDetailsOfImplementation());
//	    }
//	    if (dto.getDateOfImplementation() != null) {
//	        assurance.setDateOfImplementation(dto.getDateOfImplementation());
//	    }

	    // Save updated entities
	    implementationRequestRepository.save(implReq);
	    assuranceRepository.save(assurance);

	    return getImplementationDetailById(id); // Reuse existing method for response DTO
	}

}
