package com.dsp.cga.assurance.implementationrequest.service.impl;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.dsp.cga.assurance.clubbedassurance.repository.ClubbedAssuranceRepository;
import com.dsp.cga.assurance.droppingrequest.repository.DroppingRequestRepository;
import com.dsp.cga.assurance.dto.CommitteeDecisionDTO;
import com.dsp.cga.assurance.dto.ImplementationTypeDTO;
import com.dsp.cga.assurance.dto.MemberDTO;
import com.dsp.cga.assurance.dto.SerialNumberRequest;
import com.dsp.cga.assurance.dto.SerialNumberResponse;
import com.dsp.cga.assurance.dto.SessionDTO;
import com.dsp.cga.assurance.dto.UserDetailsDTO;
import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.assurance.entity.Departments;
import com.dsp.cga.assurance.entity.Ministries;
import com.dsp.cga.assurance.extensionrequest.repository.ExtensionRequestRepository;
import com.dsp.cga.assurance.implementationrequest.dto.AssuranceImplementationDTO;
import com.dsp.cga.assurance.implementationrequest.dto.ImplementationRequestDTO;
import com.dsp.cga.assurance.implementationrequest.entity.Application;
import com.dsp.cga.assurance.implementationrequest.entity.ImplementationRequest;
import com.dsp.cga.assurance.implementationrequest.repository.ApplicationRepository;
import com.dsp.cga.assurance.implementationrequest.repository.ImplementationRequestRepository;
import com.dsp.cga.assurance.implementationrequest.service.ImplementationRequestService;
import com.dsp.cga.assurance.otherRequests.repository.OtherRequestRepository;
import com.dsp.cga.assurance.partdetails.entity.PartDetails;
import com.dsp.cga.assurance.repository.AssuranceRepository;
import com.dsp.cga.assurance.repository.BaseUserRepository;
import com.dsp.cga.assurance.repository.CodeRepository;
import com.dsp.cga.assurance.repository.DepartmentRepository;
import com.dsp.cga.assurance.repository.MasterLSSRepository;
import com.dsp.cga.assurance.repository.MastersRSSRepository;
import com.dsp.cga.assurance.repository.MinistryRepository;
import com.dsp.cga.assurance.repository.PersonalDetailsRepository;
import com.dsp.cga.assurance.supplementry.entity.ClubbedMemberInfo;
import com.dsp.cga.assurance.supplementry.entity.Supplementary;
import com.dsp.cga.entity.BaseUser;
import com.dsp.cga.entity.MastersLokSabhaSession;
import com.dsp.cga.entity.MastersRajyaSabha;
import com.dsp.cga.entity.RequestHistory;
import com.dsp.cga.eunms.CgaStatus;
import com.dsp.cga.eunms.Entities;
import com.dsp.cga.eunms.MinistryStatus;
import com.dsp.cga.eunms.MopaStatus;
import com.dsp.cga.eunms.RequestHistoryStatus;
import com.dsp.cga.eunms.RequestType;
import com.dsp.cga.exception.ResourceNotFoundException;
import com.dsp.cga.mapper.ImplementationRequestMapper;
import com.dsp.cga.repository.RequestHistoryRepository;
import com.dsp.cga.response.GenericResponse;
import com.dsp.cga.util.Utils;

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
	private DroppingRequestRepository droppingRepo;
	
	@Autowired 
	private ClubbedAssuranceRepository clubbedRepo;
	
	@Autowired
	private OtherRequestRepository otherRequestRepo;
	
	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;
	
	@Autowired
	private MastersRSSRepository rssRepo;
	
	@Autowired
	private RequestHistoryRepository rqtHistoryRepo;
	
	@Autowired
	private BaseUserRepository userRepo;
	
	@Value("${master.service.url}")
	private String masterServiceUrl;
	@Value("${workflow.service.url}")
	private String draftedServiceUrl;
	@Autowired
	private HttpServletRequest httpRequest;
	
	@Autowired
	private CodeRepository codeRepository;

	@Autowired
	private Utils util;
	
	@Override
	@Transactional
	public ImplementationRequestDTO createImplementationRequest(ImplementationRequestDTO implementationRequestDTO) {

		// New to be created. Validate if implementation exists for the assurance
		Long count = implementationRequestRepository.countByAssuranceId(implementationRequestDTO.getAssuranceId());
		
		if (count > 0)  {
			return null;
		}
		
		// Convert DTO to Entity
	    ImplementationRequest implementationRequest = ImplementationRequestMapper.toEntity(implementationRequestDTO);
	    System.out.println("Implementation request created " + implementationRequest);
	    
	    // Get house ID (Lok Sabha or Rajya Sabha)
	    String houseAbbr = util.getHouseAbbr();
	    
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
		    System.out.println("Assurance found");

	        Assurance assurance = optionalAssurance.get();
	        
	        if (Utils.RAJYA_SABHA.equals(houseAbbr)) { // Rajya Sabha
			    System.out.println("In RS");

	            Long rajyaSabhaSessionId = assurance.getRajyaSabhaSessionNo();
	            serialNumberRequest.setSessionId(rajyaSabhaSessionId);

	            Optional<MastersRajyaSabha> optionalSession = rssRepo.findById(rajyaSabhaSessionId);
	            optionalSession.ifPresent(session -> {
	                implementationRequest.setRajyaSabhaSession(session.getSessionNumber());
	            });

	        } else if (Utils.LOK_SABHA.equals(houseAbbr)) { // Lok Sabha
			    System.out.println("In LS");

	            Long lokSabhaSessionId = assurance.getLokSabhaSessionNo();
			    System.out.println("lokSabhaSessionId: " + lokSabhaSessionId);

	            serialNumberRequest.setSessionId(lokSabhaSessionId);

	            Optional<MastersLokSabhaSession> optionalSession = lssRepo.findById(lokSabhaSessionId);
	            optionalSession.ifPresent(session -> {
	                implementationRequest.setLokSabhaSessionNumber(session.getSessionNumber());
	                implementationRequest.setLokSabhaSession(session.getLokSabha().getLokSabhaNumber());
	            });
	        }
		    System.out.println("session set");

	        // Generate serial number if blank
	        if (StringUtils.isBlank(implementationRequestDTO.getApplicationNo())) {
			    System.out.println("In Serial No");

	            SerialNumberResponse serialNumberResponse = generateSerialNumber(serialNumberRequest);
	            implementationRequest.setApplicationNo(serialNumberResponse.getId());
			    System.out.println("Created Serial No");

	        }

	    } else {
		    System.out.println("Assurance not found");
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
		System.out.println("saving Implementation");

		// Save to DB
		ImplementationRequest saved = implementationRequestRepository.save(implementationRequest);
		System.out.println("Implementation saved");

		saved.setRefId(saved.getId());

		// Save again to persist the refId update
		saved = implementationRequestRepository.save(saved);
		
		createRequestHistory(saved,"Created");
		System.out.println("new record saved in request history table");
	
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

	@Transactional
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
	@Transactional
	public GenericResponse<ImplementationRequestDTO> createOrUpdateImplementationRequest(
	        ImplementationRequestDTO implementationRequestDTO) {

	    Optional<ImplementationRequest> existingRequestOpt = implementationRequestRepository
	            .findByAssuranceId(implementationRequestDTO.getAssuranceId());

	    ImplementationRequest savedRequest = null;
	    ImplementationRequestDTO savedDTO = null;  
 
	    if (existingRequestOpt.isPresent()) {
	        if (implementationRequestDTO.getId() == null) {
	        	// implies new Request, but another exists for the same assurance. So return error.
				return new GenericResponse<>(HttpStatus.FAILED_DEPENDENCY.value(), false,
						"Implementation Request cannot be created as another Implementation Request exists for the Assurance", null);
			}			
	        
	    	// Else Update existing entity
	        ImplementationRequest existingRequest = existingRequestOpt.get();
			System.out.println("Existing IMPL req: " + existingRequest.getId());

	        ImplementationRequestMapper.updateEntityFromDTO(implementationRequestDTO, existingRequest);
	        System.out.println("Updated: " + existingRequest);
	        savedRequest = implementationRequestRepository.save(existingRequest);  
	        
	        createRequestHistory(savedRequest,savedRequest.getStatusForMopa());
	        System.out.println("existing record saved in request history table");
	        
	    } else {
	        // Create new entity
	    	
			// New to be created. Validate if implementation exists for the assurance
			Long count = implementationRequestRepository.countByAssuranceId(implementationRequestDTO.getAssuranceId());
			System.out.println("Count of records: " + count);
			if (count > 0) {
				return new GenericResponse<>(HttpStatus.FAILED_DEPENDENCY.value(), false,
						"Implementation Request cannot be created as another Implementation Request exists for the Assurance", null);
			}			
			
			savedDTO = createImplementationRequest(implementationRequestDTO);
			
//			
//	        ImplementationRequest newRequest = ImplementationRequestMapper.toEntity(implementationRequestDTO);
//	        savedRequest = implementationRequestRepository.save(newRequest);
			System.out.println("New record saved");

	    }

//	    // Fetch ImplementationType data from external service
//	    if (implementationRequestDTO.getImplementationTypeId() != null) {
//	        ImplementationTypeDTO implementationTypeDTO = fetchImplementationTypeData(
//	                implementationRequestDTO.getImplementationTypeId());
//	        implementationRequestDTO.setImplementationTypeDTO(implementationTypeDTO);
//	    }
//	    
//	    if (implementationRequestDTO.getCommitteeDecisionId() != null) {
//	        CommitteeDecisionDTO committeeDecisionDTO = fetchCommitteeDecisionData(
//	        		implementationRequestDTO.getCommitteeDecisionId());
//	        implementationRequestDTO.setCommitteeDecisionDTO(committeeDecisionDTO);
//	        
//	    }
//
	    // Convert saved entity to DTO
	    if (savedDTO == null) {
	    	savedDTO = ImplementationRequestMapper.toDTO(savedRequest);
	    }
	    
//	    // Set fetched ImplementationTypeDTO in the response DTO
//	    savedDTO.setImplementationTypeDTO(implementationRequestDTO.getImplementationTypeDTO());
//	    savedDTO.setCommitteeDecisionDTO(implementationRequestDTO.getCommitteeDecisionDTO());

	    // Return response
		System.out.println("Response returned");
		
	    return new GenericResponse<>(HttpStatus.CREATED.value(), true, 
	    		generateResponseMsg(implementationRequestDTO, savedDTO.getAssuranceNo()), savedDTO);
	}


	private String generateResponseMsg (ImplementationRequestDTO req, String assuranceNo) {
		
		log.debug("REQ for MSG: " + req);

		if (req == null) return "Success";
		boolean isDraft = req.getDraftStatus();
		String response = isDraft ? Utils.draftMsg : Utils.createMsg;

		if (!isDraft && req.getId() != null) response = Utils.updateMsg;
		
		if (StringUtils.isEmpty(assuranceNo)) {
			assuranceNo = assuranceRepository.findAssuranceNoById(req.getAssuranceId());
			req.setAssuranceNo(assuranceNo);
		}
		log.debug("Assurance No: " + req.getAssuranceNo());
		
		String finalMsg = MessageFormat.format(response, "Implementation request", req.getAssuranceNo());

		return finalMsg;
	}



	@Override
	@Transactional (readOnly = true)
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
	@Transactional(readOnly = true)
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
//	        sessionNo = (houseId == 1) ? rssRepo.findLatestByCreatedAt() : lssRepo.findLatestByCreatedAt();
	    
	    	SessionDTO session = util.getCurrentSession();
	    	sessionNo = session.getLokSabhaSessionNoId() != null 
	    					? session.getLokSabhaSessionNoId() : session.getRajyaSabhaSessionNoId();
	    }

	    final Long finalSessionNo = sessionNo;
	    String houseAbbr = util.getHouseAbbr();
	    
	    // ✅ Common Specification using ImplementationRequest
	    Specification<ImplementationRequest> spec =
	            ImplementationRequestSpecification.filterByCriteria(finalSessionNo, layingDate, implementationTypeId, statusForMinistry, ministryId, houseAbbr);

	    //List<ImplementationRequest> implementationRequests = implementationRequestRepository.findAll(spec);
	    List<ImplementationRequest> implementationRequests = implementationRequestRepository
	            .findAll(spec, Sort.by(Sort.Direction.DESC, "createdAt"));

	    if (CollectionUtils.isEmpty(implementationRequests)) { 
	        return List.of();
	    }

	    return implementationRequests.stream().map(impl -> {
	        Assurance assurance = assuranceRepository.findById(impl.getAssuranceId()).orElse(null);
	        if (assurance == null) {
	            return null;
	        }

	        // ✅ House check: skip if Rajya Sabha selected but RS session null
	        if (Utils.RAJYA_SABHA.equals(houseAbbr) && assurance.getRajyaSabhaSessionNo() == null) {
	            return null;
	        }

	        // ✅ House check: skip if Lok Sabha selected but LS session null
	        if (Utils.LOK_SABHA.equals(houseAbbr) && assurance.getLokSabhaSessionNo() == null) {
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
	                .subject(assurance.getSubject() != null 
                    ? assurance.getSubject() 
                    : (assurance.getPartDetails() != null && !assurance.getPartDetails().isEmpty()
                        ? assurance.getPartDetails().stream()
                            .sorted(Comparator.comparing(PartDetails::getId).reversed()) // latest by ID
                            .map(PartDetails::getSubject)
                            .filter(Objects::nonNull)
                            .findFirst()
                            .orElse(null)
                        : null))
	                .status(codeRepository.findCodeById(assurance.getStatusId()))
	                .dueDateOfImplementation(impl.getDueDateOfImplementation())
	                .detailsOfImplementation(impl.getDetailsOfImplementation())
	                .dateOfImplementation(impl.getDateOfImplementation())
	                .supplementaryId(impl.getSupplementaryId())
	                .partId(impl.getPartId())
	                .reason(impl.getReason())
	                .layingDate(impl.getLayingDate())
	                .statusForMinistry(impl.getStatusForMinistry())
//	                .subject(assurance.getSubject())
	                .implementationTypeId(codeRepository.findCodeById(impl.getImplementationTypeId()))
	                .memberName(memberName)
	                .departmentName(departmentName)
	                .ministryName(ministryName)
	                .assuranceId(impl.getAssuranceId())
	                .creationDate(impl.getCreatedAt().toLocalDate());

	        if (Utils.RAJYA_SABHA.equals(houseAbbr)) {
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
            .assuranceId(implementationRequest.getAssuranceId())
	        .memberName(memberName)
	        .ministryName(ministryName)
	        .departmentName(departmentName)
	        .status(codeRepository.findCodeById(assurance.getStatusId()))
	        .dueDateOfImplementation(implementationRequest.getDueDateOfImplementation())
	        .subject(assurance.getSubject() != null 
            ? assurance.getSubject() 
            : (assurance.getPartDetails() != null && !assurance.getPartDetails().isEmpty()
                ? assurance.getPartDetails().stream()
                    .sorted(Comparator.comparing(PartDetails::getId).reversed()) // latest by ID
                    .map(PartDetails::getSubject)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null)
                : null))
	        .questionNo(assurance.getQuestionNo())
	        .dateOfImplementation(implementationRequest.getDateOfImplementation())
	        .detailsOfImplementation(implementationRequest.getDetailsOfImplementation())
	        .remarks(implementationRequest.getRemarks()) 
	        .reason(implementationRequest.getReason())
	        .implementationReportFiles(implementationRequest.getImplementationReportFiles())
	        .partReportFiles(implementationRequest.getPartReportFiles())
	        .layingDate(implementationRequest.getLayingDate())
	        .implementationTypeId(codeRepository.findCodeById(implementationRequest.getImplementationTypeId()))
	        .supplementaryId(implementationRequest.getSupplementaryId())
	        .statusForMinistry(implementationRequest.getStatusForMinistry())
	        .statusForCga(implementationRequest.getStatusForCga())
	        .statusForMopa(implementationRequest.getStatusForMopa())
	        .partId(implementationRequest.getPartId())
	        .applicationId(implementationRequest.getApplicationId())
	        .applicationNo(implementationRequest.getApplicationNo())
	        .houseId(houseId)
	        .clubbedMembersData(clubbedMembersData)
            .supplementaryClubbedMembersData(supplementaryClubbedMembersData) // ✅ Added
	        .currentStatus(implementationRequest.getCurrentStatus())
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

	    Optional<ImplementationRequest> optionalRequest = implementationRequestRepository.findById(id);
	    if (optionalRequest.isEmpty()) return null;

	    ImplementationRequest implReq = optionalRequest.get();

	    // Check statusForMinistry before allowing update
	    if (!implReq.getDraft()) { 
	    	if (implReq.getStatusForMinistry() == null || 
	        	!implReq.getStatusForMinistry().equalsIgnoreCase("Returned")) {
	    		throw new IllegalStateException("Update not allowed. statusForMinistry must be 'Returned'.");
	    	}
		}
	    
	    // Update status fields
	    implReq.setStatusForMinistry(MinistryStatus.SUBMITTED.getValue());
	    
	    if (Entities.MOPA.getValue().equalsIgnoreCase(implReq.getReturnFrom())) {
	    	implReq.setStatusForMopa(MopaStatus.RECEIVED.getValue());
	    } else if (Entities.CGA.getValue().equalsIgnoreCase(implReq.getReturnFrom())) {
	    	implReq.setStatusForMopa(CgaStatus.RECEIVED.getValue());
	    }	
	    implReq.setReturnFrom(null);
	    
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

//	    if (Utils.RAJYA_SABHA.equals(houseAbbr)) {
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

	@Override
	public List<AssuranceImplementationDTO> getAllImplementationDetails(String houseAbbr,
																		String requestedBy) {
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
	            expectedSendTo = "Lok Sabha";
	        } else if (houseAbbr.equalsIgnoreCase("RS")) {
	            filterHouseId = 1L; // Rajya Sabha
	            expectedSendTo = "Rajya Sabha";
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


	
	private AssuranceImplementationDTO buildImplementationDTO(Assurance assurance, 
															ImplementationRequest impl) {
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
            .assuranceId(impl.getAssuranceId())
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
	        .implementationTypeId(codeRepository.findCodeById(impl.getImplementationTypeId()))
	        .supplementaryId(impl.getSupplementaryId())
	        .partId(impl.getPartId())
	        .houseId(houseId)
	        .house(isLokSabha ? "Lok Sabha" : "Rajya Sabha")
	        .creationDate(impl.getCreatedAt().toLocalDate())
	        .statusForMinistry(impl.getStatusForMinistry())
	        .statusForMopa(impl.getStatusForMopa())
	        .statusForCga(impl.getStatusForCga()) ;

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

	
	private String getSubject (Assurance assurance) {
		
		System.out.println("Part Details: " + assurance.getPartDetails());
		
	    String subject = (!StringUtils.isEmpty(assurance.getSubject()))
	            ? assurance.getSubject()
	            : (!assurance.getPartDetails().isEmpty()
	                ? assurance.getPartDetails().stream()
	                    .sorted(Comparator.comparing(PartDetails::getId).reversed())
	                    .map(PartDetails::getSubject)
	                    .filter(Objects::nonNull)
	                    .findFirst()
	                    .orElse(null)
	                : null);
	    return subject;
		
	}
	

	@Override
	public AssuranceImplementationDTO updateImplementationRequestforMinistryStatusById(Long id,
			ImplementationRequestDTO dto) {
		Optional<ImplementationRequest> optionalRequest = implementationRequestRepository.findById(id);
		if (optionalRequest.isEmpty())
			return null;

		ImplementationRequest implReq = optionalRequest.get();

		if (dto.getRemarks() != null) {
			implReq.setRemarks(dto.getRemarks()); // still updating remarks in Assurance (if required)
		}

		// When returned by MoPA to Ministry
		if (!StringUtils.isEmpty(dto.getStatusForMinistry())
				&& MinistryStatus.RETURNED.getValue().equalsIgnoreCase(dto.getStatusForMinistry())) {
			System.out.println(dto.getStatusForMinistry());

			implReq.setStatusForMinistry(dto.getStatusForMinistry());
			implReq.setReturnFrom("Mopa");
			implReq.setStatusForMopa(MopaStatus.RETURNED.getValue());
		}

		// When sending to LS / RS
		if (StringUtils.isEmpty(dto.getStatusForMinistry()) && !StringUtils.isEmpty(dto.getSendTo())) {
			implReq.setSendTo(dto.getSendTo());
			implReq.setStatusForMopa(MopaStatus.IN_PROGRESS.getValue());
			implReq.setStatusForCga(CgaStatus.RECEIVED.getValue());
			// Ministry status remains unchanged - SUBMITTED
		}

		implementationRequestRepository.save(implReq);
		// assuranceRepository.save(assurance); // only if remarks changed

		return getImplementationDetailById(id);
	}
	
	@Override
	public AssuranceImplementationDTO sendToCga(ImplementationRequestDTO dto) {

		Optional<ImplementationRequest> optionalRequest = implementationRequestRepository.findById(dto.getId());

		if (optionalRequest.isEmpty())
			return null;

		ImplementationRequest implReq = optionalRequest.get();

		implReq.setSendTo(dto.getSendTo());
		implReq.setStatusForMopa(MopaStatus.IN_PROGRESS.getValue());
		implReq.setStatusForCga(CgaStatus.RECEIVED.getValue());
		// Ministry status remains unchanged - SUBMITTED

		implementationRequestRepository.save(implReq);

		return getImplementationDetailById(dto.getId());
	}
	  
	@Override
	@Transactional(readOnly = true)
	public ImplementationRequestDTO getAcceptedRequest(Long assuranceId) {

		// Fetch the list of ExtensionRequest entities by Assurance ID
		ImplementationRequest implRequest = implementationRequestRepository.getAcceptedRequest(assuranceId);

		// If no records are found, log the message and return an empty list
		if (implRequest == null) {
			log.debug("No Other Request found for Assurance ID: {}", assuranceId);
			return null;
		}

		// Fetch Assurance to get the assuranceNo
		// Assurance assurance = assuranceRepository.findById(assuranceId).orElse(null);
		Assurance assurance = implRequest.getAssurance();
		String assuranceNo = (assurance != null) ? assurance.getAssuranceNo() : null;

		// Map the list of entities to a list of DTOs and set the assuranceNo in DTO
		ImplementationRequestDTO dto = ImplementationRequestMapper.toDTO(implRequest);
		dto.setAssuranceNo(assuranceNo);

		if (implRequest.getImplementationTypeId() != null) {
			dto.setImplementationTypeDTO(fetchImplementationTypeData(implRequest.getImplementationTypeId()));
		}
		// Populate additional data if available
		if (implRequest.getCommitteeDecisionId() != null) {
			dto.setCommitteeDecisionDTO(fetchCommitteeDecisionData(implRequest.getCommitteeDecisionId()));
		}
		log.debug("Returning ImplementationRequestDTO: {}", dto);

		return dto;

	}	
	
	private void createRequestHistory(ImplementationRequest input, String actionTaken) {

//    	String pendingWithName = null;
//    	String actiontakenByName = null;
//    	
//    	if ("Returned".equalsIgnoreCase(actionTaken)) {
//    		pendingWithName = getActionTakenBy(input);
//    		actiontakenByName = input.getPendingAt();
//    	}

		RequestHistory history = RequestHistory.builder()
				.requestId(input.getId())
				.requestType(RequestType.IMPLEMENTATION_REQ.getValue())
				.actionType(actionTaken)
				.referenceFiles(input.getImplementationReportFiles())
				.pendingWith(RequestHistoryStatus.MOPA.getValue())
				.actionTakenBy(RequestHistoryStatus.MINISTRY.getValue())
				.actionTakenByName(Entities.MINISTRY.getValue())
				.pendingWithName(Entities.MOPA.getValue())
				.commentForDisplay(input.getRemarks())
//    			.ipAddress(getClientIp(httpRequest))
				.build();

		rqtHistoryRepo.save(history);
	}

	private String getActionTakenBy(ImplementationRequest input) {

		String userName="";
		if(input.getUpdatedBy()==null && input.getCreatedBy()!=null)
		{
			Optional<BaseUser> user = userRepo.findById(input.getCreatedBy());
			if (user == null)
				return null;
			System.out.println("Fetched user: " + input.getId() + " " + user.get());

		 userName = user.get().getFirstName() + " " + (user.get().getMiddleName() != null ? (user.get().getMiddleName() + " ") : "")
				+ (user.get().getLastName() != null ? user.get().getLastName() : "");
	}
		
		if(!(input.getUpdatedBy()==null))
		{
			Optional<BaseUser> user = userRepo.findById(input.getUpdatedBy());
			if (user == null)
				return null;
			System.out.println("Fetched user: " + input.getId() + " " + user.get());

		 userName = user.get().getFirstName() + " " + (user.get().getMiddleName() != null ? (user.get().getMiddleName() + " ") : "")
				+ (user.get().getLastName() != null ? user.get().getLastName() : "");
		}
		return userName;
	}
	
}