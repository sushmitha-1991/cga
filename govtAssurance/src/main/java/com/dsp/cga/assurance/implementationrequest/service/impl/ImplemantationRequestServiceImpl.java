package com.dsp.cga.assurance.implementationrequest.service.impl;

import java.time.LocalDate;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.dsp.cga.assurance.clubbedassurance.repository.ClubbedAssuranceRepository;
import com.dsp.cga.assurance.droppingrequest.repository.DroppingRequestRepositiry;
import com.dsp.cga.assurance.dto.AssuranceDTO;
import com.dsp.cga.assurance.dto.CommitteeDecisionDTO;
import com.dsp.cga.assurance.dto.ImplementationTypeDTO;
import com.dsp.cga.assurance.dto.MemberDTO;
import com.dsp.cga.assurance.dto.SerialNumberRequest;
import com.dsp.cga.assurance.dto.SerialNumberResponse;
import com.dsp.cga.assurance.dto.StatusDTO;
import com.dsp.cga.assurance.dto.UserDetailsDTO;
import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.assurance.entity.Departments;
import com.dsp.cga.assurance.entity.Ministries;
import com.dsp.cga.assurance.extensionrequest.repository.ExtensionRequestRepository;
import com.dsp.cga.assurance.implementationrequest.dto.AssuranceImplementationDTO;
import com.dsp.cga.assurance.implementationrequest.dto.ImplementationRequestDTO;
import com.dsp.cga.assurance.implementationrequest.entity.Application;
import com.dsp.cga.assurance.implementationrequest.entity.ImplementationRequest;
import com.dsp.cga.assurance.implementationrequest.repository.ApplicationInstanceRepository;
import com.dsp.cga.assurance.implementationrequest.repository.ApplicationRepository;
import com.dsp.cga.assurance.implementationrequest.repository.ImplementationRequestRepository;
import com.dsp.cga.assurance.implementationrequest.service.ImplementationRequestService;
import com.dsp.cga.assurance.otherRequests.repository.OtherRequestRepository;
import com.dsp.cga.assurance.repository.AssuranceRepository;
import com.dsp.cga.assurance.repository.CodeRepository;
import com.dsp.cga.assurance.repository.DepartmentRepository;
import com.dsp.cga.assurance.repository.MasterLSSRepository;
import com.dsp.cga.assurance.repository.MastersRSSRepository;
import com.dsp.cga.assurance.repository.MinistryRepository;
import com.dsp.cga.assurance.repository.PersonalDetailsRepository;
import com.dsp.cga.assurance.service.impl.AssuranceServiceImpl;
import com.dsp.cga.assurance.supplementry.entity.ClubbedMemberInfo;
import com.dsp.cga.assurance.supplementry.entity.Supplementary;
import com.dsp.cga.entity.MastersLokSabhaSession;
import com.dsp.cga.entity.MastersRajyaSabha;
import com.dsp.cga.exception.ResourceNotFoundException;
import com.dsp.cga.mapper.ImplementationRequestMapper;
import com.dsp.cga.response.GenericResponse;

import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.servlet.http.HttpServletRequest;


@Service
public class ImplemantationRequestServiceImpl implements ImplementationRequestService {

	private static final Logger log = LoggerFactory.getLogger(ImplemantationRequestServiceImpl.class);

	@Value("${master.codeServiceUrl}")
	private String codeServiceUrl;
	
	@Value("${member.memberServiceUrl}")
	private String memberServiceUrl;
	
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
	private ApplicationRepository applicationRepository;
	
	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private ExtensionRequestRepository extensionRepo;
	
	@Autowired
	private DroppingRequestRepositiry droppingRepo;
	
	@Autowired 
	private ClubbedAssuranceRepository clubbedRepo;
	
	@Autowired
	private OtherRequestRepository otherRequestRepo;
	
	@Autowired
	private ApplicationInstanceRepository applicationInstanceRepository;
	
	
	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;
	
	@Autowired
	private MastersRSSRepository rssRepo;
	
	@Value("${master.service.url}")
	private String masterServiceUrl;
	@Value("${workflow.service.url}")
	private String draftedServiceUrl;
	@Autowired
	private HttpServletRequest httpRequest;
	
	@Autowired
	private CodeRepository codeRepository;


	
	@Override
	public ImplementationRequestDTO createImplementationRequest(ImplementationRequestDTO implementationRequestDTO) {
	    // Convert DTO to Entity
	    ImplementationRequest implementationRequest = ImplementationRequestMapper.toEntity(implementationRequestDTO);

	    // Get house ID (Lok Sabha or Rajya Sabha)
	    Long houseId = getHouseId();
	    SerialNumberRequest serialNumberRequest = new SerialNumberRequest();
	    Optional<Application> optionalApp = applicationRepository.findByAbbr("IRFM");
	    System.out.println(optionalApp.get().getId());
	  
	    if (optionalApp.isPresent()) {
	        serialNumberRequest.setAppId(optionalApp.get().getId());
	    } else {
	         throw new ResourceNotFoundException("Application with abbreviation 'IRFM' not found");
	    }
	   // serialNumberRequest.setAppId(implementationRequestDTO.getApplicationId());

	    // ✅ Get Assurance entity by ID
	    Optional<Assurance> optionalAssurance = assuranceRepository.findById(implementationRequest.getAssuranceId());
	    if (optionalAssurance.isPresent()) {
	        Assurance assurance = optionalAssurance.get();

	        if (houseId == 1) { // Rajya Sabha
	            Long rajyaSabhaSessionId = assurance.getRajyaSabhaSessionNo();
	            serialNumberRequest.setSessionId(rajyaSabhaSessionId);

	            Optional<MastersRajyaSabha> optionalSession = rssRepo.findById(rajyaSabhaSessionId);
	            optionalSession.ifPresent(session -> {
	                implementationRequest.setRajyaSabhaSession(session.getSessionNumber());
	            });

	        } else if (houseId == 2) { // Lok Sabha
	            Long lokSabhaSessionId = assurance.getLokSabhaSessionNo();
	            serialNumberRequest.setSessionId(lokSabhaSessionId);

	            Optional<MastersLokSabhaSession> optionalSession = lssRepo.findById(lokSabhaSessionId);
	            optionalSession.ifPresent(session -> {
	                implementationRequest.setLokSabhaSessionNumber(session.getSessionNumber());
	                implementationRequest.setLokSabhaSession(session.getLokSabha().getLokSabhaNumber());
	            });
	        }

	        // Generate serial number if blank
	        if (StringUtils.isBlank(implementationRequestDTO.getApplicationNo())) {
	            SerialNumberResponse serialNumberResponse = generateSerialNumber(serialNumberRequest);
	            implementationRequest.setApplicationNo(serialNumberResponse.getId());
	        }

	    } else {
	        throw new ResourceNotFoundException("Assurance not found with ID: " + implementationRequest.getAssuranceId());
	    }

	    // Set content fields
	    if (implementationRequest.getLayingDate() != null) {
	        implementationRequest.setDocumentDate(implementationRequest.getLayingDate());
	    } else if (implementationRequest.getDateOfImplementation() != null) {
	        implementationRequest.setDocumentDate(implementationRequest.getDateOfImplementation());
	    }
	    implementationRequest.setCurrentStatus(implementationRequest.getStatusForMinistry());
//	    implementationRequest.setRefId(implementationRequest.getId());
//	    System.out.println("***********refId" + implementationRequest.getId());
	    implementationRequest.setApplicationId(optionalApp.get().getId());
	  
	    // Save to DB
	    ImplementationRequest saved = implementationRequestRepository.save(implementationRequest);

	    saved.setRefId(saved.getId());

	 // Save again to persist the refId update
	 saved = implementationRequestRepository.save(saved);

	    return ImplementationRequestMapper.toDTO(saved);
	}






	
	public SerialNumberResponse generateSerialNumber(SerialNumberRequest request) {
	    String url = masterServiceUrl + "/serial-number";
	    log.debug("Calling Serial Number API at URL: {}", url);

	    try {
	        SerialNumberResponse response = restTemplate.postForObject(url, request, SerialNumberResponse.class);

	        if (response == null) {
	            log.error("Serial Number API returned null response");
	            throw new RuntimeException("Failed to generate serial number, response is null");
	        }

	        log.debug("Received SerialNumberResponse: {}", response);
	        return response;

	    } catch (Exception e) {
	        log.error("Error during Serial Number API call", e);
	        throw new RuntimeException("Failed to generate serial number", e);
	    }
	}




	public String getTokenFromRequest() {
		String authorizationHeader = httpRequest.getHeader("Authorization");
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			return authorizationHeader.substring(7);
		}
		return null;
	}

	public String deleteDraftEntry(String applicationNo) {
		String url = draftedServiceUrl + "/delete-save-as-draft?applicationNo=" + applicationNo;
		String token = getTokenFromRequest();

		if (token == null) {
			throw new IllegalStateException("Authorization token is missing");
		}

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Void> entity = new HttpEntity<>(headers); // No body needed for DELETE request

		try {
			ResponseEntity<GenericResponse> response = restTemplate.exchange(url, HttpMethod.DELETE, entity,
					GenericResponse.class);

			System.out.println("@@@");
			System.out.println(response);
			if (response.getBody() != null && response.getBody().isSuccess()) {
				return response.getBody().getMessage(); // Return success message
			} else {
				return "Failed to delete draft entry";
			}
		} catch (Exception e) {
			System.err.println("Error during API call: " + e.getMessage());
			throw new RuntimeException("Failed to delete draft entry", e);
		}
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
	        Long implementationTypeId, String statusForMinistry, Integer ministryCode) {

	    // Get ministryId from ministryCode if provided
	    final Long ministryId;
	    if (ministryCode != null) {
	        Ministries ministry = ministryRepository.findByMinistryCode(ministryCode);
	        if (ministry != null) {
	            ministryId = ministry.getId();
	        } else {
	            return List.of(); // No such ministry
	        }
	    } else {
	        ministryId = null;
	    }

	    Long houseId = getHouseId(); 
	    if (sessionNo == null) {
	        sessionNo = (houseId == 1) ? rssRepo.findLatestByCreatedAt() : lssRepo.findLatestByCreatedAt();
	    }

	    final Long finalSessionNo = sessionNo;

	    // ✅ Common Specification using ImplementationRequest
	    Specification<ImplementationRequest> spec =
	            ImplementationRequestSpecification.filterByCriteria(finalSessionNo, layingDate, implementationTypeId, statusForMinistry, ministryId, houseId);

	    List<ImplementationRequest> implementationRequests = implementationRequestRepository.findAll(spec);

	    if (CollectionUtils.isEmpty(implementationRequests)) {
	        return List.of();
	    }

	    implementationRequests = implementationRequests.stream()
	    	    .sorted(Comparator.comparing(ImplementationRequest::getCreatedAt).reversed())
	    	    .collect(Collectors.toList());
	    
	    return implementationRequests.stream().map(impl -> {
	        Assurance assurance = assuranceRepository.findById(impl.getAssuranceId()).orElse(null);
	        if (assurance == null) {
	            return null;
	        }

	        // ✅ House check: skip if Rajya Sabha selected but RS session null
	        if (houseId == 1 && assurance.getRajyaSabhaSessionNo() == null) {
	            return null;
	        }

	        // ✅ House check: skip if Lok Sabha selected but LS session null
	        if (houseId == 2 && assurance.getLokSabhaSessionNo() == null) {
	            return null;
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
	                    .map(Departments::getDepartmentName).orElse(null);
	        }

	        if (assurance.getMinistryId() != null) {
	            ministryName = ministryRepository.findById(assurance.getMinistryId())
	                    .map(Ministries::getMinistryName).orElse(null);
	        }

	        AssuranceImplementationDTO.AssuranceImplementationDTOBuilder builder = AssuranceImplementationDTO.builder()
	                .assuranceNo(assurance.getAssuranceNo())
	                .id(impl.getId())
	                .questionNo(assurance.getQuestionNo())
	                .subject(assurance.getSubject())
	                .status(codeRepository.findCodeById(assurance.getStatusId()))
	                .dueDateOfImplementation(impl.getDueDateOfImplementation())
	                .detailsOfImplementation(impl.getDetailsOfImplementation())
	                .dateOfImplementation(impl.getDateOfImplementation())
	                .supplementaryId(impl.getSupplementaryId())
	                .partId(impl.getPartId())
	                .reason(impl.getReason())
	                .layingDate(impl.getLayingDate())
	                .statusForMinistry(impl.getStatusForMinistry())
	                .subject(assurance.getSubject())
	                .implementationTypeId(codeRepository.findCodeById(impl.getImplementationTypeId()))
	                .memberName(memberName)
	                .departmentName(departmentName)
	                .ministryName(ministryName);

	        if (houseId == 1) {
	            builder.house("Rajya Sabha");
	            builder.rajyaSabhaSessionNumber(rssRepo.findSessionNumberById(assurance.getRajyaSabhaSessionNo()));
	        } else {
	            builder.house("Lok Sabha");
	            builder.lokSabhaSessionNumber(lssRepo.findSessionNumberById(assurance.getLokSabhaSessionNo()));
	            builder.lokSabhaNumber(lssRepo.findLokSabhaNumberById(assurance.getLokSabhaNo()));
	        }

	        return builder.build();

	    }).filter(Objects::nonNull).collect(Collectors.toList());
	}


	private Long getHouseId() {
		Long houseId = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetailsDTO) {
			houseId = ((UserDetailsDTO) principal).getHouseId();
		}
		return houseId;
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
	public AssuranceImplementationDTO getImplementationDetailById(Long id) {
	  //  Long houseId = getHouseId();

	    Optional<ImplementationRequest> optionalRequest = implementationRequestRepository.findById(id);
	    if (optionalRequest.isEmpty()) return null;

	    ImplementationRequest implementationRequest = optionalRequest.get();

	    Assurance assurance = assuranceRepository.findById(implementationRequest.getAssuranceId()).orElse(null);
	    if (assurance == null) return null;

	    // Common values
	    String memberName = null;
	    String departmentName = null;
	    String ministryName = null;
	    
	    List<MemberDTO> clubbedMembersData = new ArrayList<>();
	    List<MemberDTO> supplementaryClubbedMembersData = new ArrayList<>();

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

	    Long houseId = getHouseIdFromAssurance(assurance);
	    
	    AssuranceImplementationDTO.AssuranceImplementationDTOBuilder builder = AssuranceImplementationDTO.builder()
	        .assuranceNo(assurance.getAssuranceNo())
	        .memberName(memberName)
	        .ministryName(ministryName)
	        .departmentName(departmentName)
	        .status(codeRepository.findCodeById(assurance.getStatusId()))
	        .dueDateOfImplementation(implementationRequest.getDueDateOfImplementation())
	        .subject(assurance.getSubject())
	        .questionNo(assurance.getQuestionNo())
	        .dateOfImplementation(implementationRequest.getDateOfImplementation())
	        .detailsOfImplementation(implementationRequest.getDetailsOfImplementation())
	        .remarks(assurance.getRemarks())
	        .reason(implementationRequest.getReason())
	        .implementationReportFiles(implementationRequest.getImplementationReportFiles())
	        .partReportFiles(implementationRequest.getPartReportFiles())
	        .layingDate(implementationRequest.getLayingDate())
	        .implementationTypeId(codeRepository.findCodeById(implementationRequest.getImplementationTypeId()))
	        .supplementaryId(implementationRequest.getSupplementaryId())
	        .statusForMinistry(implementationRequest.getStatusForMinistry())
	        .partId(implementationRequest.getPartId())
	        .applicationId(implementationRequest.getApplicationId())
	        .applicationNo(implementationRequest.getApplicationNo())
	        .houseId(houseId)
	        .clubbedMembersData(clubbedMembersData)
            .supplementaryClubbedMembersData(supplementaryClubbedMembersData) // ✅ Added
	        .currentStatus(implementationRequest.getCurrentStatus())
	        //.refId(implementationRequest.getRefId())
	        .id(implementationRequest.getId())
	    .applicationInstanceId(implementationRequest.getId());

	    // Set session number based on house
	    if (houseId == 2) {
	        builder
	            .lokSabhaSessionNumber(lssRepo.findSessionNumberById(assurance.getLokSabhaSessionNo()))
	            .lokSabhaNumber(lssRepo.findLokSabhaNumberById(assurance.getLokSabhaNo()))
	            .house("Lok Sabha");
	    
	    } else {
	        builder
	            .rajyaSabhaSessionNumber(rssRepo.findSessionNumberById(assurance.getRajyaSabhaSessionNo()))
	            .house("Rajya Sabha");
	    }

	    return builder.build();
	}

	@Override
	public AssuranceImplementationDTO updateImplementationRequest(Long id, ImplementationRequestDTO dto) {
	    Long houseId = getHouseId();

	    Optional<ImplementationRequest> optionalRequest = implementationRequestRepository.findById(id);
	    if (optionalRequest.isEmpty()) return null;

	    ImplementationRequest implReq = optionalRequest.get();
	    
	  

	    // Check statusForMinistry before allowing update
	    if (implReq.getStatusForMinistry() == null || 
	        !implReq.getStatusForMinistry().equalsIgnoreCase("Returned")) {
	        throw new IllegalStateException("Update not allowed. statusForMinistry must be 'Returned'.");
	    }

	    Assurance assurance = assuranceRepository.findById(implReq.getAssuranceId()).orElse(null);
	    if (assurance == null) return null;

	    // Update fields in ImplementationRequest
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

	    // Implementation-related shared fields — saved in Assurance earlier, now save in ImplementationRequest
	    if (dto.getDueDateOfImplementation() != null) {
	        implReq.setDueDateOfImplementation(dto.getDueDateOfImplementation());
	    }
	    if (dto.getDetailsOfImplementation() != null) {
	        implReq.setDetailsOfImplementation(dto.getDetailsOfImplementation());
	    }
	    if (dto.getDateOfImplementation() != null) {
	        implReq.setDateOfImplementation(dto.getDateOfImplementation());
	    }
	    if (dto.getRemarks() != null) {
	    	implReq.setRemarks(dto.getRemarks()); // still updating remarks in Assurance (if required)
	    }

	    implementationRequestRepository.save(implReq);
	    assuranceRepository.save(assurance); // only if remarks changed

	    return getImplementationDetailById(id);
	}

	
	@Override
	public Map<String, Object> getRequestCounts(Integer ministryCode, Long houseId) {
	    Long ministryId = null;

	    // Convert ministryCode to ministryId
	    if (ministryCode != null) {
	        Ministries ministry = ministryRepository.findByMinistryCode(ministryCode);
	        if (ministry != null) {
	            ministryId = ministry.getId();
	        } else {
	            throw new ResourceNotFoundException("Ministry not found for code: " + ministryCode);
	        }
	    }

	    if (houseId == null || (houseId != 1 && houseId != 2)) {
	        throw new IllegalArgumentException("Invalid houseId. Use 1 (Rajya Sabha) or 2 (Lok Sabha).");
	    }

	    Map<String, Long> requestCounts = new HashMap<>();

	    if (houseId == 1) {
	        requestCounts.put("implementationRequest", implementationRequestRepository.countByMinistryIdAndRajyaSabha(ministryId));
	        requestCounts.put("extensionRequest", extensionRepo.countByMinistryIdAndRajyaSabha(ministryId));
	        requestCounts.put("droppingRequest", droppingRepo.countByMinistryIdAndRajyaSabha(ministryId));
	        requestCounts.put("clubbedRequest", clubbedRepo.countByMinistryIdAndRajyaSabha(ministryId));
	        requestCounts.put("otherRequest", otherRequestRepo.countByMinistryIdAndRajyaSabha(ministryId));

	    } else {
	        requestCounts.put("implementationRequest", implementationRequestRepository.countByMinistryIdAndLokSabha(ministryId));
	        requestCounts.put("extensionRequest", extensionRepo.countByMinistryIdAndLokSabha(ministryId));
	        requestCounts.put("droppingRequest", droppingRepo.countByMinistryIdAndLokSabha(ministryId));
	        requestCounts.put("clubbedRequest", clubbedRepo.countByMinistryIdAndLokSabha(ministryId));
	        requestCounts.put("otherRequest", otherRequestRepo.countByMinistryIdAndLokSabha(ministryId));

	    }

	    //requestCounts.put("otherRequest", 0L); // placeholder for future use

	    Map<String, Object> result = new HashMap<>();
	    result.put("ministryId", ministryId);
	    result.put("ministryCode", ministryCode);
	    result.put("houseId", houseId);
	    result.put("requestCounts", requestCounts);

	    return result;
	}




//	@Override
//	public List<AssuranceImplementationDTO> getAllImplementationDetails(String houseAbbr) {
//	    List<AssuranceImplementationDTO> dtoList = new ArrayList<>();
//
//	    // Fetch all ImplementationRequests (could optimize with filtering in DB later)
//	    List<ImplementationRequest> implRequests = implementationRequestRepository.findAll();
//
//	    // Determine houseId filter if houseAbbr provided
//	    Long filterHouseId = null;
//	    if (houseAbbr != null) {
//	        if (houseAbbr.equalsIgnoreCase("LS")) {
//	            filterHouseId = 2L; // Lok Sabha
//	        } else if (houseAbbr.equalsIgnoreCase("RS")) {
//	            filterHouseId = 1L; // Rajya Sabha
//	        }
//	    }
//
//	    for (ImplementationRequest impl : implRequests) {
//	        Assurance assurance = assuranceRepository.findById(impl.getAssuranceId()).orElse(null);
//	        if (assurance != null) {
//	            Long houseId = getHouseIdFromAssurance(assurance);
//
//	            // If house filter is present, skip non-matching records
//	            if (filterHouseId != null && !filterHouseId.equals(houseId)) {
//	                continue;
//	            }
//
//	            dtoList.add(buildImplementationDTO(assurance, impl));
//	        }
//	    }
//
//	    return dtoList;
//	}
	
	@Override
	public List<AssuranceImplementationDTO> getAllImplementationDetails(String houseAbbr) {
	    List<AssuranceImplementationDTO> dtoList = new ArrayList<>();

	    // Fetch all ImplementationRequests
	 //   List<ImplementationRequest> implRequests = implementationRequestRepository.findAll();
	    List<ImplementationRequest> implRequests = implementationRequestRepository.findAllByOrderByUpdatedAtDesc();

	    // Determine houseId and sendTo filter if houseAbbr is provided
	    Long filterHouseId = null;
	    String expectedSendTo = null;

	    if (houseAbbr != null) {
	        if (houseAbbr.equalsIgnoreCase("LS")) {
	            filterHouseId = 2L; // Lok Sabha
	            expectedSendTo = "Loksabha";
	        } else if (houseAbbr.equalsIgnoreCase("RS")) {
	            filterHouseId = 1L; // Rajya Sabha
	            expectedSendTo = "RajyaSabha";
	        }
	    }

	    for (ImplementationRequest impl : implRequests) {
	        //  Filter based on sendTo if houseAbbr is provided
	        if (expectedSendTo != null && (impl.getSendTo() == null || !impl.getSendTo().equalsIgnoreCase(expectedSendTo))) {
	            continue;
	        }

	        Assurance assurance = assuranceRepository.findById(impl.getAssuranceId()).orElse(null);
	        if (assurance != null) {
	            Long houseId = getHouseIdFromAssurance(assurance);

	            // ✅ Filter based on houseId if applicable
	            if (filterHouseId != null && !filterHouseId.equals(houseId)) {
	                continue;
	            }

	            dtoList.add(buildImplementationDTO(assurance, impl));
	        }
	    }

	    return dtoList;
	}



	private Long getHouseIdFromAssurance(Assurance assurance) {
	    if (assurance.getLokSabhaNo() != null || assurance.getLokSabhaSessionNo() != null) {
	        return 2L; // Lok Sabha
	    } else if (assurance.getRajyaSabhaSessionNo() != null) {
	        return 1L; // Rajya Sabha
	    }
	    return null; // Unknown / no house info
	}


	
	private AssuranceImplementationDTO buildImplementationDTO(Assurance assurance, ImplementationRequest impl) {
	    String memberName = null, departmentName = null, ministryName = null;

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

	    Long houseId = getHouseIdFromAssurance(assurance);
	    boolean isLokSabha = (houseId != null && houseId == 2);

	    AssuranceImplementationDTO.AssuranceImplementationDTOBuilder builder = AssuranceImplementationDTO.builder()
	        .id(impl.getId())
	        .assuranceNo(assurance.getAssuranceNo())
	        .memberName(memberName)
	        .ministryName(ministryName)
	        .departmentName(departmentName)
	        .subject(assurance.getSubject())
	        .questionNo(assurance.getQuestionNo())
	        .status(codeRepository.findCodeById(assurance.getStatusId()))
	        .dueDateOfImplementation(impl.getDueDateOfImplementation())
	        .dateOfImplementation(impl.getDateOfImplementation())
	        .detailsOfImplementation(impl.getDetailsOfImplementation())
	        .remarks(assurance.getRemarks())
	        .reason(impl.getReason())
	        .layingDate(impl.getLayingDate())
	        .statusForMinistry(impl.getStatusForMinistry())
	        .implementationTypeId(codeRepository.findCodeById(impl.getImplementationTypeId()))
	        .supplementaryId(impl.getSupplementaryId())
	        .partId(impl.getPartId())
	        .houseId(houseId)
	        .house(isLokSabha ? "Lok Sabha" : "Rajya Sabha");

	    if (isLokSabha) {
	        builder
	            .lokSabhaNumber(lssRepo.findLokSabhaNumberById(assurance.getLokSabhaNo()))
	            .lokSabhaSessionNumber(lssRepo.findSessionNumberById(assurance.getLokSabhaSessionNo()));
	    } else {
	        builder
	            .rajyaSabhaSessionNumber(rssRepo.findSessionNumberById(assurance.getRajyaSabhaSessionNo()));
	    }

	    return builder.build();
	}




	@Override
	public AssuranceImplementationDTO updateImplementationRequestforMinistryStatusById(Long id,
			ImplementationRequestDTO dto) {
		  Optional<ImplementationRequest> optionalRequest = implementationRequestRepository.findById(id);
		    if (optionalRequest.isEmpty()) return null;

		    ImplementationRequest implReq = optionalRequest.get();
		    
		   
		    if (dto.getRemarks() != null) {
		    	implReq.setRemarks(dto.getRemarks()); // still updating remarks in Assurance (if required)
		    }
		    
		    if (dto.getSendTo() != null) {
		    	implReq.setSendTo(dto.getSendTo()); 
		    }

		    if (dto.getStatusForMinistry() != null) {
		    	System.out.println(dto.getStatusForMinistry());
		    	implReq.setStatusForMinistry(dto.getStatusForMinistry()); 
		    	  implReq.setReturnFrom("Mopa");
		    }
		    
		  

		    
		    implementationRequestRepository.save(implReq);
		    //assuranceRepository.save(assurance); // only if remarks changed

		    return getImplementationDetailById(id);
		}




	@Override
	public List<AssuranceImplementationDTO> getReturnRequestsFromCGA(String houseAbbr) {
	    List<ImplementationRequest> implRequests = implementationRequestRepository
	        .findByStatusForMinistryAndReturnFrom("Returned", "CGA");

	    List<AssuranceImplementationDTO> dtoList = new ArrayList<>();

	    Long filterHouseId = null;
	    if ("LS".equalsIgnoreCase(houseAbbr)) {
	        filterHouseId = 2L;
	    } else if ("RS".equalsIgnoreCase(houseAbbr)) {
	        filterHouseId = 1L;
	    }

	    for (ImplementationRequest impl : implRequests) {
	        Assurance assurance = assuranceRepository.findById(impl.getAssuranceId()).orElse(null);
	        if (assurance != null) {
	            Long houseId = getHouseIdFromAssurance(assurance);
	            if (filterHouseId != null && !filterHouseId.equals(houseId)) continue;

	            dtoList.add(buildImplementationDTO(assurance, impl));
	        }
	    }

	    return dtoList;
	}
}




	









