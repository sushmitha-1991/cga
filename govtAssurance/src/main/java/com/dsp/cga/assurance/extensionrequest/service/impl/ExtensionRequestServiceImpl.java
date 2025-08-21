package com.dsp.cga.assurance.extensionrequest.service.impl;

import java.text.MessageFormat;
import java.time.LocalDate;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.dsp.cga.assurance.clubbedassurance.dto.AssuranceIdNoPairDTO;
import com.dsp.cga.assurance.clubbedassurance.entity.ClubbedAssurance;
import com.dsp.cga.assurance.clubbedassurance.repository.ClubbedAssuranceRepository;
import com.dsp.cga.assurance.droppingrequest.entity.DroppingRequest;
import com.dsp.cga.assurance.droppingrequest.repository.DroppingRequestRepository;
import com.dsp.cga.assurance.dto.CommonRequestResponseDTO;
import com.dsp.cga.assurance.dto.MemberDTO;
import com.dsp.cga.assurance.dto.SessionDTO;
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
import com.dsp.cga.assurance.implementationrequest.service.impl.ImplementationRequestSpecification;
import com.dsp.cga.assurance.otherRequests.dto.OtherRequestDTO;
import com.dsp.cga.assurance.otherRequests.entity.OtherRequests;
import com.dsp.cga.assurance.otherRequests.repository.OtherRequestRepository;
import com.dsp.cga.assurance.partdetails.entity.PartDetails;
import com.dsp.cga.assurance.repository.AssuranceRepository;
import com.dsp.cga.assurance.repository.CodeRepository;
import com.dsp.cga.assurance.repository.DepartmentRepository;
import com.dsp.cga.assurance.repository.MasterLSSRepository;
import com.dsp.cga.assurance.repository.MastersRSSRepository;
import com.dsp.cga.assurance.repository.MinistryRepository;
import com.dsp.cga.assurance.repository.PersonalDetailsRepository;
import com.dsp.cga.assurance.supplementry.entity.ClubbedMemberInfo;
import com.dsp.cga.assurance.supplementry.entity.Supplementary;
import com.dsp.cga.eunms.CgaStatus;
import com.dsp.cga.eunms.Entities;
import com.dsp.cga.eunms.MinistryStatus;
import com.dsp.cga.eunms.MopaStatus;
import com.dsp.cga.eunms.RequestType;
import com.dsp.cga.exception.ResourceNotFoundException;
import com.dsp.cga.mapper.ClubbedAssuranceMapper;
import com.dsp.cga.mapper.DroppingRequestMapper;
import com.dsp.cga.mapper.ExtensionRequestMapper;
import com.dsp.cga.mapper.ImplementationRequestMapper;
import com.dsp.cga.mapper.OtherRequestMapper;
import com.dsp.cga.repository.HousesRepository;
import com.dsp.cga.response.GenericResponse;
import com.dsp.cga.specification.ClubbedAssuranceRequestSpec;
import com.dsp.cga.specification.DroppingRequestSpec;
import com.dsp.cga.specification.ExtensionRequestSpec;
import com.dsp.cga.specification.ImplementationRequestSpec;
import com.dsp.cga.specification.OtherRequestSpec;
import com.dsp.cga.util.Utils;

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
    private Utils util;

    @Autowired
    private HousesRepository houseRepository;
    
	@Autowired
	private DroppingRequestRepository droppingRequestRepository;
	
	@Autowired
	private ClubbedAssuranceRepository clubbedAssuranceRepository;
	
	@Autowired
	private ExtensionRequestRepository extensionRepository;

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

	    if (extensionRequestDTOList == null || extensionRequestDTOList.isEmpty()) {
		    return new GenericResponse<>(
		            HttpStatus.CREATED.value(),
		            true,
		            "No Extension requests available for saving",
		            Collections.EMPTY_LIST
		    );
	    }
	    
	    Long assuranceId = extensionRequestDTOList.get(0).getAssuranceId();
	    
	    // First retrieve all existing extension requests for the assurance
	    List<Long> extnList = extenionRequestRepository.getExtensionIdList(assuranceId);
	    
	    for (ExtensionRequestDTO dto : extensionRequestDTOList) {
//	        if (dto.getId() == null && (dto.getAssuranceId() == null || dto.getExtensionNo() == null)) {
	    	
	    	System.out.println("ID: " + dto.getId());
	    	System.out.println("Assurance ID: " + dto.getAssuranceId()); 

	    	if (dto.getId() == null && dto.getAssuranceId() == null) {
	            throw new IllegalArgumentException("Invalid input: AssuranceId and Extension ID both cannot be null.");
	        }

	        ExtensionRequest savedRequest = null;

	        if (dto.getId() != null) {
	            // If the ID is provided, try to find the existing record by ID
	            Optional<ExtensionRequest> existingRequestOpt = extenionRequestRepository.findById(dto.getId());

	            if (existingRequestOpt.isPresent()) {
	                // Update the existing entity
	                ExtensionRequest existingRequest = existingRequestOpt.get();
	                ExtensionRequestMapper.updateEntityFromDTO(dto, existingRequest);

	                // Save the updated entity
	                savedRequest = extenionRequestRepository.save(existingRequest);
	                
	            	log.info("Existing Entity updated");

	                // Remove from extnList as this has been processed
	                extnList.remove(dto.getId());
	            } else {
	                throw new IllegalArgumentException("No ExtensionRequest found with the provided ID: " + dto.getId());
	            }

	        } else if (dto.getAssuranceId() != null && dto.getExtensionNo() != null) { 
	            // If no ID is provided, check if an entry exists for assuranceId + extensionNo
	        	Optional<ExtensionRequest> existingRequestOpt = extenionRequestRepository
	                    .findByAssuranceIdAndExtensionNo(dto.getAssuranceId(), dto.getExtensionNo());

	            if (existingRequestOpt.isPresent()) {
	                // Update the existing entity
	                ExtensionRequest existingRequest = existingRequestOpt.get();
	                ExtensionRequestMapper.updateEntityFromDTO(dto, existingRequest);

	                // Save the updated entity
	                savedRequest = extenionRequestRepository.save(existingRequest);
	            	log.info("Existing Entity updated");
	            } 
	        } else {
	            	
	                // Create a new entity
	                ExtensionRequest newRequest = ExtensionRequestMapper.toEntity(dto);

	                // Save the new entity
	                savedRequest = extenionRequestRepository.save(newRequest);
	            	log.info("New Entity created");

	        }

	        // Convert the saved entity to DTO and add to the response list
	        ExtensionRequestDTO savedDTO = ExtensionRequestMapper.toDTO(savedRequest);
	        savedDTOs.add(savedDTO);
	    }

	    if (extnList != null && !extnList.isEmpty()) {
		    // Remove any residual items in extnLists. These have been deleted
	    	extnList.forEach(x-> {
	    		extenionRequestRepository.deleteExtenstion(x);
	    		log.info("Deleted extension request: {}", x);
	    	});
	    }
	    	    
	    String msg = createResponseMsg(extensionRequestDTOList, savedDTOs);
	    
	    // Return the response wrapped in GenericResponse
	    return new GenericResponse<>(
	            HttpStatus.CREATED.value(),
	            true,
	            msg,
	            savedDTOs
	    );
	}
	
	private String createResponseMsg(List<ExtensionRequestDTO> extensionRequestDTOList,
									 List<ExtensionRequestDTO> savedDTOs) {
		
		ExtensionRequestDTO er = extensionRequestDTOList.get(0);
		return generateResponseMsg (er, savedDTOs.get(0).getAssuranceNo());
	}

	private String generateResponseMsg(ExtensionRequestDTO req, String assuranceNo) {

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

		String finalMsg = MessageFormat.format(response, "Extension request", req.getAssuranceNo());

		return finalMsg;
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
	    	
//	    	SessionDTO session = util.getCurrentSession();
//	    	sessionNo = session.getLokSabhaSessionNoId() != null 
//	    					? session.getLokSabhaSessionNoId() : session.getRajyaSabhaSessionNoId();
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

	    String houseAbbr = util.getHouseAbbr();
	    
	    //  Fetch and filter manually using assuranceId
	    List<ExtensionRequest> filteredList = extenionRequestRepository
    	    .findAll(specification, Sort.by(Sort.Direction.DESC, "createdAt")).stream()
    	    .filter(er -> {
	        	 if (er.getAssuranceId() == null) return false;
	            Assurance assurance = assuranceRepository.findById(er.getAssuranceId()).orElse(null);
	            if (assurance == null) return false;

	            if (Utils.RAJYA_SABHA.equals(houseAbbr)) {
	                if (assurance.getRajyaSabhaSessionNo() == null) return false;
	                if (finalSessionNo != null && !finalSessionNo.equals(assurance.getRajyaSabhaSessionNo())) return false;
	            } else if (Utils.LOK_SABHA.equals(houseAbbr)) {
	                if (assurance.getLokSabhaSessionNo() == null || assurance.getLokSabhaNo() == null) return false;
	                if (finalSessionNo != null && !finalSessionNo.equals(assurance.getLokSabhaSessionNo())) return false;
	            }

	            if (ministryId != null && !ministryId.equals(assurance.getMinistryId())) return false;

	            return true;
	        })
	        .collect(Collectors.toList());

	    //  Convert to DTO
	    return filteredList.stream()
	    	   // .sorted(Comparator.comparing(ExtensionRequest::getCreatedAt).reversed()) // sort before mapping
.map(er -> {
	        Assurance assurance = assuranceRepository.findById(er.getAssuranceId()).orElse(null);
	        return ExtensionRequestResponseDTO.builder()
	                .assuranceNo(assurance != null ? assurance.getAssuranceNo() : null)
	                .assuranceId(er.getAssuranceId())
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
	                //.subject(assurance.getSubject())
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
	            .assuranceId(er.getAssuranceId())
	            .extensionNo(er.getExtensionNo())
	            .questionNo(assurance != null ? assurance.getQuestionNo() : null)
	           // .subject(assurance != null ? assurance.getSubject() : null)
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
		    
		    // Update status fields
		    er.setStatusForMinistry(MinistryStatus.SUBMITTED.getValue());
		    er.setStatusForCga(CgaStatus.RECEIVED.getValue());

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

	    String houseAbbr = util.getHouseAbbr();

	    // ExtensionRequest
	    extenionRequestRepository.findAll().forEach(req -> {
	        if (Boolean.TRUE.equals(req.getFromMinistry()) &&
	                ("Submitted".equalsIgnoreCase(req.getStatusForMinistry()) ||
	                 "Returned".equalsIgnoreCase(req.getStatusForMinistry()))) {

	            Assurance assurance = getAssurance(req.getAssuranceId());
	            if (isValidForHouse(houseAbbr, assurance)) {
	                response.add(buildDTO(
	                        req.getId(), "Extension Request", req.getAssuranceId(),
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
	            if (isValidForHouse(houseAbbr, assurance)) {
	                response.add(buildDTO(
	                        req.getId(), "Other Request", req.getAssuranceId(),
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
	            if (isValidForHouse(houseAbbr, assurance)) {
	                response.add(buildDTO(
	                        req.getId(), "Clubbed Assurance", req.getAssuranceId(),
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
	            if (isValidForHouse(houseAbbr, assurance)) {
	                response.add(buildDTO(
	                        req.getId(), "Dropping Request", req.getAssuranceId(),
	                        req.getSubject(), req.getRemarks(), req.getFromMinistry(),
	                        req.getStatusForMinistry(), assurance,
	                        req.getCreatedAt() != null ? req.getCreatedAt().toLocalDate() : null
	                ));
	            }
	        }
	    });
	    
	    response.sort(
	    	    Comparator.comparing(CommonRequestResponseDTO::getDate, Comparator.nullsLast(Comparator.reverseOrder()))
	    	);


	    return response;
	}

	private boolean isValidForHouse(String houseAbbr, Assurance assurance) {
	    if (assurance == null) return false;

	    if (Utils.RAJYA_SABHA.equals(houseAbbr)) {
	        return assurance.getRajyaSabhaSessionNo() != null;
	    } else if (Utils.LOK_SABHA.equals(houseAbbr)) { 
	        return assurance.getLokSabhaSessionNo() != null;
	    }

	    return true;
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
	                .assuranceNo(assurance != null ? assurance.getAssuranceNo() : null) // ✅ Set assuranceNo

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
//	        Long houseId = getHouseId(); // Current houseId
	        String houseAbbr = util.getHouseAbbr();
	        
	        switch (requestType) {
	            case "Extension Request":
	                return extenionRequestRepository.findById(id)
	                    .filter(req -> Boolean.TRUE.equals(req.getFromMinistry()) &&
	                            ("Submitted".equalsIgnoreCase(req.getStatusForMinistry()) ||
	                             "Returned".equalsIgnoreCase(req.getStatusForMinistry())))
	                    .map(req -> {
	                        Assurance assurance = getAssurance(req.getAssuranceId());
	                        if (isValidForHouse(houseAbbr, assurance)) {
	                        	
	                            CommonRequestResponseDTO assuranceDetails = buildDTO(assurance, req);
	                            return assuranceDetails;
	                        }
	                        return null;
	                    }).orElse(null);

	            case "Other Request":
	                return otherRequestRepository.findById(id)
	                    .filter(req -> Boolean.TRUE.equals(req.getFromMinistry()) &&
	                            ("Submitted".equalsIgnoreCase(req.getStatusForMinistry()) ||
	                             "Returned".equalsIgnoreCase(req.getStatusForMinistry())))
	                    .map(req -> {
	                        Assurance assurance = getAssurance(req.getAssuranceId());
	                        if (isValidForHouse(houseAbbr, assurance)) {

	                            CommonRequestResponseDTO assuranceDetails = buildDTO(assurance, req);
	                            return assuranceDetails;

	                        }
	                        return null;
	                    })
	                    .orElse(null);

	            case "Dropping Request":
	                return droppingRequestRepository.findById(id)
	                    .filter(req -> Boolean.TRUE.equals(req.getFromMinistry()) &&
	                            ("Submitted".equalsIgnoreCase(req.getStatusForMinistry()) ||
	                             "Returned".equalsIgnoreCase(req.getStatusForMinistry())))
	                    .map(req -> {
	                        Assurance assurance = getAssurance(req.getAssuranceId());
	                        if (isValidForHouse(houseAbbr, assurance)) {
	                            
	                            CommonRequestResponseDTO assuranceDetails = buildDTO(assurance, req);
                            	return assuranceDetails;

	                        }
	                        return null;
	                    })
	                    .orElse(null);

	            case "Clubbed Assurance":
	                return clubbedAssuranceRepository.findById(id)
	                    .filter(req -> Boolean.TRUE.equals(req.getFromMinistry()) &&
	                            ("Submitted".equalsIgnoreCase(req.getStatusForMinistry()) ||
	                             "Returned".equalsIgnoreCase(req.getStatusForMinistry())))
	                    .map(req -> {
	                        Assurance assurance = getAssurance(req.getAssuranceId());
	                        if (isValidForHouse(houseAbbr, assurance)) {

	                            CommonRequestResponseDTO assuranceDetails = buildDTO(assurance, req);
	                            return assuranceDetails;
	                        }
	                        return null;
	                    })
	                    .orElse(null);
	                
	            case "Implementation Request":
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
	                        if (isValidForHouse(houseAbbr, assurance)) {
	                        	
	                            CommonRequestResponseDTO assuranceDetails = buildDTO(assurance, req);
	                            return assuranceDetails;

	                        }
	                        return null;
	                    })
	                    .orElse(null);


	            default:
	                throw new IllegalArgumentException("Invalid request type: " + requestType);
	        }
	    }

	    // Used for updating Return & Accept status
	    @Override
	    @Transactional
	    public CommonRequestResponseDTO updateRequest(Long id, String requestType, CommonRequestResponseDTO updateDto) {
	    	System.out.println(requestType);
	    	
	    	String updatedStatus = updateDto.getStatusForMinistry();
	    	
	        switch (requestType) {
	            case "Extension Request":
	                return extenionRequestRepository.findById(id).map(entity -> {
	                  
//	                    if (updateDto.getStatusForMinistry() != null) {
//	                    	entity.setStatusForMinistry(updateDto.getStatusForMinistry());
//	                    	entity.setStatusForCga(updateDto.getStatusForMinistry());
//	                    }	                  

	                	ExtensionRequestMapper.updateEntityForReturnOrAccept(updateDto, entity);
	                	extenionRequestRepository.save(entity);
	                    
	                	if (MinistryStatus.ACCEPTED.getValue().equals(updatedStatus)) {
	                		updateAssuranceOnAccept(entity);
	                	}
	                    
	                    return getRequestById(id, requestType);
	                }).orElse(null);

	            case "Other Request":
	                return otherRequestRepository.findById(id).map(entity -> {
	                    
	                	OtherRequestMapper.updateEntityForReturnOrAccept(updateDto, entity); 

	                	otherRequestRepository.save(entity);
	                    return getRequestById(id, requestType);
	                }).orElse(null);

	            case "Dropping Request":
	                return droppingRequestRepository.findById(id).map(entity -> {
	                    
	                	DroppingRequestMapper.updateEntityForReturnOrAccept(updateDto, entity);
	                	
                    	droppingRequestRepository.save(entity);
	                    return getRequestById(id, requestType);
	                }).orElse(null);

	            case "Clubbed Assurance":
	                return clubbedAssuranceRepository.findById(id).map(entity -> {
	                	
	                	ClubbedAssuranceMapper.updateEntityForReturnOrAccept(updateDto, entity); 
	                	System.out.println("***** UPDATED RETURNED ENTITY: " + entity);
	                	clubbedAssuranceRepository.save(entity);
	                    return getRequestById(id, requestType);
	                    
	                }).orElse(null);
	                 
	            case "Implementation Request":  // New case added
	                return implementationRequestRepository.findById(id).map(entity -> {
	                    if (updateDto.getStatusForMinistry() != null) {
	                    	
                    		if (CgaStatus.ACCEPTED.getValue().equals(entity.getStatusForCga())) {
                    			throw new IllegalArgumentException("ACCEPTED Implementation reqest cannot be ACCEPTED again");
                    		}
                    		
	                    	entity.setStatusForCga(updateDto.getStatusForMinistry());
	                    	
	                    	if (CgaStatus.ACCEPTED.getValue().equals(updatedStatus)) {
		                    	entity.setStatusForMopa(MopaStatus.ACCEPTED.getValue());
		                    	entity.setStatusForCga(CgaStatus.ACCEPTED.getValue());
		                    	entity.setStatusForMinistry(MinistryStatus.ACCEPTED.getValue());
		                    	
		                    	// Date of Implementation & Extended upto date should be updated with Date of Implementation,
		                    	// Details of Implementation
		                    	updateAssuranceOnAccept(entity);

		                    } else {
	                    		// Returned
		                    	entity.setStatusForMopa(MopaStatus.RETURNED.getValue());
			                    entity.setReturnFrom("CGA");
	                    	}                 	
                        }

	                    implementationRequestRepository.save(entity);
	                    return getRequestById(id, requestType);
	                }).orElse(null);

	            default:
	                throw new IllegalArgumentException("Invalid request type: " + requestType);
	        }
	    }

	    
    private void updateAssuranceOnAccept(ImplementationRequest entity) {
    	 
    	System.out.println("IMP REQ: " + entity.getRemarks());
    	
    	Assurance assurance = entity.getAssurance();

    	if (assurance != null) {
        	log.debug ("ASSURANCE ID: " + assurance.getId());

	    	assurance.setDueDateOfImplementation(entity.getDueDateOfImplementation());
	    	assurance.setDateOfImplementation(entity.getDateOfImplementation());
	    	assurance.setRemarks(entity.getRemarks());
        	assuranceRepository.saveAndFlush(assurance);
    	}
    	
    	// Get Extension by Assurance ID
    	ExtensionRequest extRequest = extensionRepository.getLatest(assurance.getId());
    	if (extRequest == null) return;
    	
    	extRequest.setExtRequestedTillDate(entity.getDueDateOfImplementation());
    	extenionRequestRepository.saveAndFlush(extRequest);    	
    }
	
    private void updateAssuranceOnAccept(ExtensionRequest entity) {
   	 
    	System.out.println("EXT REQ: " + entity.getRemarks());
    	
    	Assurance assurance = entity.getAssurance();

    	if (assurance != null) {
        	log.debug ("ASSURANCE ID: " + assurance.getId());

	    	assurance.setDueDateOfImplementation(entity.getExtRequestedTillDate()); 
        	assuranceRepository.saveAndFlush(assurance);
    	}
    }
    
	@Override
	@Transactional(readOnly = true)
	public List<CommonRequestResponseDTO> getAllRequests(
				LocalDate fromDate, LocalDate toDate, Long ministryId, Long memberId, 
				String status, String statusFor, String houseAbbr) {
	    
		List<CommonRequestResponseDTO> assurances = new ArrayList<>();

		// Implementation Request
		assurances.addAll(getImplementationRequests(fromDate, toDate, ministryId, memberId, status, statusFor, houseAbbr));

		if (!Entities.MOPA.getValue().equals(statusFor)) {
			// ExtensionRequest
			assurances.addAll(getExtensionRequests(fromDate, toDate, ministryId, memberId, status, statusFor));
			// OtherRequests
			assurances.addAll(getOtherRequests(fromDate, toDate, ministryId, memberId, status, statusFor));
			// ClubbedAssurance
			assurances.addAll(getClubbedAssurances(fromDate, toDate, ministryId, memberId, status, statusFor));
			// DroppingRequest
			assurances.addAll(getDroppingRequests(fromDate, toDate, ministryId, memberId, status, statusFor));
		}

		assurances.sort(Comparator.comparing(CommonRequestResponseDTO::getDate,
				Comparator.nullsLast(Comparator.reverseOrder())));

		return assurances;
	}


	private Specification<ExtensionRequest> createExtensionSpecification(
			LocalDate fromDate, LocalDate toDate, Long ministryId, Long memberId, String status, String statusFor) {
		
		Specification<ExtensionRequest> spec = Specification.where(null);
		spec = spec.and(ExtensionRequestSpec.isActive(true));
		spec = spec.and(ExtensionRequestSpec.hasHouse(util.getHouseAbbr()));
		spec = spec.and(ExtensionRequestSpec.isFromMinistry(true));

		if (fromDate != null || toDate != null) {
			spec = spec.and(ExtensionRequestSpec.hasDateBetween(fromDate, toDate));
		}
		
		if (!StringUtils.isEmpty(status)) {
			spec = spec.and(ExtensionRequestSpec.hasStatus(status, statusFor));
		} else {
			spec = spec.and(ExtensionRequestSpec.hasStatus(statusFor));
		}
		
		if (ministryId != null) {
			spec = spec.and(ExtensionRequestSpec.hasMinistryId(ministryId));
		}

		if (memberId != null) {
			spec = spec.and(ExtensionRequestSpec.hasMemberId(memberId));
		}
		
		return spec;
	}
	

	
	private List<CommonRequestResponseDTO> getExtensionRequests(LocalDate fromDate, LocalDate toDate, Long ministryId,
			Long memberId, String status, String statusFor) {

		String reqBy = StringUtils.isEmpty(statusFor) ? Entities.MINISTRY.getValue() : statusFor;

		Specification<ExtensionRequest> spec = createExtensionSpecification(fromDate, toDate, ministryId, memberId,
				status, reqBy);

		System.out.println("SPEC:" + spec.toString());
				
		List<CommonRequestResponseDTO> response = new ArrayList<>();
		extenionRequestRepository.findAll(spec).forEach(req -> {
		response.add(buildDTO(req.getAssurance(), req));
		});

		System.out.println("Number of records: " + response.size());

		return response;
	}
	
	
	private Specification<OtherRequests> createOtherRequestSpecification(
			LocalDate fromDate, LocalDate toDate, Long ministryId, Long memberId, String status, String statusFor) {
		
		Specification<OtherRequests> spec = Specification.where(null);
		spec = spec.and(OtherRequestSpec.isActive(true));
		spec = spec.and(OtherRequestSpec.hasHouse(util.getHouseAbbr()));
		spec = spec.and(OtherRequestSpec.isFromMinistry(true));

		if (fromDate != null || toDate != null) {
			spec = spec.and(OtherRequestSpec.hasDateBetween(fromDate, toDate));
		}
		
		if (!StringUtils.isEmpty(status)) {
			spec = spec.and(OtherRequestSpec.hasStatus(status, statusFor));
		} else {
			spec = spec.and(OtherRequestSpec.hasStatus(statusFor));
		}
	
		if (ministryId != null) {
			spec = spec.and(OtherRequestSpec.hasMinistryId(ministryId));
		}

		if (memberId != null) {
			spec = spec.and(OtherRequestSpec.hasMemberId(memberId));
		}
		
		return spec;
	}	
	
	
	private List<CommonRequestResponseDTO> getOtherRequests(LocalDate fromDate, LocalDate toDate, Long ministryId,
			Long memberId, String status, String statusFor) {

		String reqBy = StringUtils.isEmpty(statusFor) ? Entities.MINISTRY.getValue() : statusFor;

		Specification<OtherRequests> spec = createOtherRequestSpecification(fromDate, toDate, ministryId, memberId,
				status, reqBy);
		
		List<CommonRequestResponseDTO> response = new ArrayList<>();

		otherRequestRepository.findAll(spec).forEach(req -> {

			response.add(buildDTO(req.getAssurance(), req));
		});
		return response;
	}

	private Specification<ClubbedAssurance> createClubbedAssuranceSpecification(
			LocalDate fromDate, LocalDate toDate, Long ministryId, Long memberId, String status, String statusFor) {
		
		Specification<ClubbedAssurance> spec = Specification.where(null);
		spec = spec.and(ClubbedAssuranceRequestSpec.isActive(true));
		spec = spec.and(ClubbedAssuranceRequestSpec.hasHouse(util.getHouseAbbr())); 
		spec = spec.and(ClubbedAssuranceRequestSpec.isFromMinistry(true));

		if (fromDate != null || toDate != null) {
			spec = spec.and(ClubbedAssuranceRequestSpec.hasDateBetween(fromDate, toDate));
		}
		
		if (!StringUtils.isEmpty(status)) {
			spec = spec.and(ClubbedAssuranceRequestSpec.hasStatus(status, statusFor));
		} else {
			spec = spec.and(ClubbedAssuranceRequestSpec.hasStatus(statusFor));
		}
	
		if (ministryId != null) {
			System.out.println("Ministry ID: " + ministryId);
			spec = spec.and(ClubbedAssuranceRequestSpec.hasMinistryId(ministryId));
		}

		if (memberId != null) {
			System.out.println("Member ID: " + memberId);
			spec = spec.and(ClubbedAssuranceRequestSpec.hasMemberId(memberId));
		}
		
		return spec;
	}	

		
	private List<CommonRequestResponseDTO> getClubbedAssurances(LocalDate fromDate, LocalDate toDate, Long ministryId,
			Long memberId, String status, String statusFor) {

		String reqBy = StringUtils.isEmpty(statusFor) ? Entities.MINISTRY.getValue() : statusFor;

		Specification<ClubbedAssurance> spec = createClubbedAssuranceSpecification(fromDate, toDate, ministryId, memberId,
				status, reqBy);
		
		System.out.println("Club SPEC: " + spec);

		List<CommonRequestResponseDTO> response = new ArrayList<>();

		clubbedAssuranceRepository.findAll(spec).forEach(req -> {

			System.out.println(req);
			
			response.add(buildDTO(req.getAssurance(), req));

		});
		return response;
	}
	
	
	private Specification<DroppingRequest> createDroppingRequestSpecification(
			LocalDate fromDate, LocalDate toDate, Long ministryId, Long memberId, String status, String statusFor) {
		
		Specification<DroppingRequest> spec = Specification.where(null);
		spec = spec.and(DroppingRequestSpec.isActive(true));
		spec = spec.and(DroppingRequestSpec.hasHouse(util.getHouseAbbr()));
		spec = spec.and(DroppingRequestSpec.isFromMinistry(true));

		if (fromDate != null || toDate != null) {
			spec = spec.and(DroppingRequestSpec.hasDateBetween(fromDate, toDate));
		}
		
		if (!StringUtils.isEmpty(status)) {
			spec = spec.and(DroppingRequestSpec.hasStatus(status, statusFor));
		} else {
			spec = spec.and(DroppingRequestSpec.hasStatus(statusFor));
		}
	
		if (ministryId != null) {
			spec = spec.and(DroppingRequestSpec.hasMinistryId(ministryId));
		}

		if (memberId != null) {
			spec = spec.and(DroppingRequestSpec.hasMemberId(memberId));
		}
		
		return spec;
	}	

	
	private List<CommonRequestResponseDTO> getDroppingRequests (LocalDate fromDate, LocalDate toDate, Long ministryId,
			Long memberId, String status, String statusFor) {

		String reqBy = StringUtils.isEmpty(statusFor) ? Entities.MINISTRY.getValue() : statusFor;

		Specification<DroppingRequest> spec = createDroppingRequestSpecification(fromDate, toDate, ministryId, memberId,
				status, statusFor);

		List<CommonRequestResponseDTO> response = new ArrayList<>();

		droppingRequestRepository.findAll(spec).forEach(req -> {

			response.add(buildDTO(req.getAssurance(), req));
		
		});
		return response;
	}
	
	
	private Specification<ImplementationRequest> createImplementationSpecification(
			LocalDate fromDate, LocalDate toDate, Long ministryId, Long memberId, 
			String status, String statusFor, String houseAbbr) {
		
		Specification<ImplementationRequest> spec = Specification.where(null);
		spec = spec.and(ImplementationRequestSpec.isActive(true));
		
		if (!Entities.MOPA.getValue().equalsIgnoreCase(statusFor) ||
			!StringUtils.isEmpty(houseAbbr)) {
			spec = spec.and(ImplementationRequestSpec.hasHouse(houseAbbr));
		}
		
		spec = spec.and(ImplementationRequestSpec.isFromMinistry(true));

		if (fromDate != null || toDate != null) {
			spec = spec.and(ImplementationRequestSpec.hasDateBetween(fromDate, toDate));
		}
		
		if (!StringUtils.isEmpty(status)) {
			spec = spec.and(ImplementationRequestSpec.hasStatus(status, statusFor));
		} else {
			spec = spec.and(ImplementationRequestSpec.hasStatus(statusFor));
		}
		
		if (ministryId != null) {
			spec = spec.and(ImplementationRequestSpec.hasMinistryId(ministryId));
		}

		if (memberId != null) {
			spec = spec.and(ImplementationRequestSpec.hasMemberId(memberId));
		}
		
		if (!StringUtils.isEmpty(statusFor)) {
			spec = spec.and(ImplementationRequestSpec.hasMemberId(memberId));
		}
		return spec;
	}
	
	
	private List<CommonRequestResponseDTO> getImplementationRequests (LocalDate fromDate, LocalDate toDate, Long ministryId,
			Long memberId, String status, String statusFor, String houseAbbr) {

		String reqBy = StringUtils.isEmpty(statusFor) ? Entities.MINISTRY.getValue() : statusFor;

		Specification<ImplementationRequest> spec = createImplementationSpecification(fromDate, toDate, ministryId, 
								memberId, status, reqBy, houseAbbr);

		System.out.println("***************IMPL SPEC: " + spec);
		
		List<CommonRequestResponseDTO> response = new ArrayList<>();

		implementationRequestRepository.findAll(spec).forEach(req -> {

			response.add(buildDTO(req.getAssurance(), req));
		
		});
		
		return response;
	}
	
	
	private CommonRequestResponseDTO buildDTO(Assurance assurance, DroppingRequest req) {

		if (assurance == null) return null;
		
		CommonRequestResponseDTO dto = populateCommonParams(assurance);
		dto.setId(req.getId());
		dto.setRequestType(RequestType.DROPPING_REQ.getValue());
		dto.setRemarks(req.getRemarks());
		dto.setFromMinistry(req.getFromMinistry());
		dto.setStatusForMinistry(req.getStatusForMinistry());
		dto.setStatusForCga(req.getStatusForCga());
		dto.setDate(req.getCreatedAt() != null ? req.getCreatedAt().toLocalDate() : null);
		
		dto.setDroppingType(codeRepository.findCodeById(req.getDroppingType()));
		dto.setSupplementaryId(req.getSupplementaryId());
		dto.setPartId(req.getPartId());
        dto.setDropRequestDate(req.getDropRequestDate());
        dto.setActualDropDate(req.getActualDropDate());
        dto.setMinistryRefNo(req.getMinistryRefNo());
        dto.setMinistryRefDate(req.getMinistryRefDate());
        dto.setAcceptedId(req.getAcceptedId());
        dto.setType(req.getType());
        dto.setCommunicationDate(req.getCommunicationDate());
        dto.setDroppingFiles(req.getDroppingFiles());
        dto.setSession(req.getSession());
        dto.setMeetingNo(req.getMeetingNo());
        dto.setMeetingDate(req.getMeetingDate());
        dto.setDroppingFiles(req.getDroppingFiles());
        dto.setDraftStatus(req.getDraftStatus());
 
		return dto;
	
	}
	
	
	private CommonRequestResponseDTO buildDTO(Assurance assurance, ClubbedAssurance req) {

		if (assurance == null) return null;
		
		CommonRequestResponseDTO dto = populateCommonParams(assurance);
		dto.setId(req.getId());
		dto.setRequestType(RequestType.CLUB_REQ.getValue());
		dto.setFromMinistry(req.getFromMinistry());
		dto.setStatusForMinistry(req.getStatusForMinistry());
		dto.setStatusForCga(req.getStatusForCga());
//		dto.setDate(req.getCreatedAt() != null ? req.getCreatedAt().toLocalDate() : null);
		dto.setDate(req.getDate());

		dto.setClubbedAssuranceIds(req.getClubbedAssuranceIds());
		
		// Fetch AssuranceId and AssuranceNo pairs for clubbedAssuranceIds
		List<AssuranceIdNoPairDTO> clubbedPairs = req.getClubbedAssuranceIds().stream().map(id -> {
			String no = fetchAssuranceNoById(id);
			return AssuranceIdNoPairDTO.builder().id(id).no(no).build();
		}).collect(Collectors.toList());
		dto.setClubbedAssurances(clubbedPairs);

		System.out.println(clubbedPairs);

		return dto;
	}	

	// Fetch AssuranceNo by AssuranceId
	private String fetchAssuranceNoById(Long assuranceId) {
		Optional<Assurance> assurance = assuranceRepository.findById(assuranceId);
		return assurance.map(Assurance::getAssuranceNo).orElse(null);
	}
	
	private CommonRequestResponseDTO buildDTO(Assurance assurance, OtherRequests req) { 

		if (assurance == null) return null;
		
		CommonRequestResponseDTO dto = populateCommonParams(assurance);
        dto.setStatus(codeRepository.findCodeById(req.getStatusId()));
		dto.setId(req.getId());
		dto.setRequestType(RequestType.OTHER_REQ.getValue());
		dto.setRemarks(req.getRemarks());
		dto.setFromMinistry(req.getFromMinistry());
		dto.setStatusForMinistry(req.getStatusForMinistry());
		dto.setStatusForCga(req.getStatusForCga());
		dto.setDate(req.getCreatedAt() != null ? req.getCreatedAt().toLocalDate() : null);

		dto.setRequestedDate(req.getRequestedDate());
		dto.setStatusId(req.getStatusId());

		dto.setOtherSubject(req.getSubject());
		dto.setFileUrl(req.getFileUrl());
		
		return dto;
	}	
	
	private CommonRequestResponseDTO buildDTO(Assurance assurance, ExtensionRequest req) {

		if (assurance == null) return null;
		
		CommonRequestResponseDTO dto = populateCommonParams(assurance);
		dto.setId(req.getId());
		dto.setRequestType(RequestType.EXTENSION_REQ.getValue());
		dto.setRemarks(req.getRemarks());
		dto.setFromMinistry(req.getFromMinistry());
		dto.setStatusForMinistry(req.getStatusForMinistry());
		dto.setStatusForCga(req.getStatusForCga());
		dto.setDate(req.getCreatedAt() != null ? req.getCreatedAt().toLocalDate() : null);

		dto.setStatusId(req.getStatusId());
	
		dto.setExtensionNo(req.getExtensionNo());
		dto.setExtensionFiles(req.getExtensionFiles());
		dto.setExtRequestedTillDate(req.getExtRequestedTillDate());
		dto.setExtGrantedTillDate(req.getExtGrantedTillDate());
		dto.setMinistryRefNo(req.getMinistryRefNo());
		dto.setMinistryRefDate(req.getMinistryRefDate());
		
		return dto;
	}	
	
	private CommonRequestResponseDTO buildDTO(Assurance assurance, ImplementationRequest req) {

		if (assurance == null) return null;
		
		CommonRequestResponseDTO dto = populateCommonParams(assurance);
		dto.setId(req.getId());
		dto.setRequestType(RequestType.IMPLEMENTATION_REQ.getValue());
		dto.setRemarks(req.getRemarks());
		dto.setFromMinistry(req.getFromMinistry());
		dto.setStatusForMinistry(req.getStatusForMinistry());
		dto.setStatusForCga(req.getStatusForCga());
		dto.setStatusForMopa(req.getStatusForMopa());
		dto.setDate(req.getCreatedAt().toLocalDate());

		dto.setDueDateOfImplementation(req.getDueDateOfImplementation()); 
		dto.setDateOfImplementation(req.getDateOfImplementation());
		dto.setDetailsOfImplementation(req.getDetailsOfImplementation());
		dto.setLayingDate(req.getLayingDate());
		dto.setPartId(req.getPartId());
		dto.setSupplementaryId(req.getSupplementaryId());
		dto.setImplementationReportFiles(req.getImplementationReportFiles());
		dto.setPartReportFiles(req.getPartReportFiles());
		
		// House
        if (assurance.getHouseId() != null) {
        	dto.setHouse(houseRepository.findHouseNameById(assurance.getHouseId()));
        }
		return dto;
	}	
	
	private CommonRequestResponseDTO populateCommonParams(Assurance assurance) {

		String status = codeRepository.findCodeById(assurance != null ? assurance.getStatusId() : null);
		
		String lokSabhaSessionNumber = assurance != null
				? lssRepo.findSessionNumberById(assurance.getLokSabhaSessionNo())
				: null;
		
		String lokSabhaNumber = assurance != null ? lssRepo.findLokSabhaNumberById(assurance.getLokSabhaNo()) : null;
		
		String rajyasabhaSessionNumber = assurance != null
				? rssRepo.findSessionNumberById(assurance.getRajyaSabhaSessionNo())
				: null;

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
			ministryName = ministryRepository.findById(assurance.getMinistryId()).map(Ministries::getMinistryName)
					.orElse(null);
		}
		
		CommonRequestResponseDTO dto = CommonRequestResponseDTO.builder()
											.assuranceId(assurance.getId())
											.subject(getSubject(assurance))
											.assuranceNo(assurance != null ? assurance.getAssuranceNo() : null)
											.questionNo(assurance.getQuestionNo())
											.lokSabhaSessionNumber(lokSabhaSessionNumber)
											.lokSabhaNumber(lokSabhaNumber)
											.rajyaSabhaSessionNumber(rajyasabhaSessionNumber)
											.memberName(memberName)
											.departmentName(departmentName)
											.ministryName(ministryName)
											.status(status)
											.build();
		return dto;	
	}
	
	
	private String getSubject(Assurance assurance) {
		
		if (!StringUtils.isEmpty(assurance.getSubject())) return assurance.getSubject();
		
        if (assurance.getPartDetails() != null && !assurance.getPartDetails().isEmpty()) {
              return assurance.getPartDetails().stream()
                    .sorted(Comparator.comparing(PartDetails::getId).reversed()) // latest by ID
                    .map(PartDetails::getSubject)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }
        
        return null;

	}
	
	
	@Override
	public List<ExtensionRequestDTO> getAcceptedRequests(Long assuranceId) {
		
	    // Fetch the list of ExtensionRequest entities by Assurance ID
	    List<ExtensionRequest> extensionRequests = extenionRequestRepository.getAcceptedRequests(assuranceId);

	    // If no records are found, log the message and return an empty list
	    if (extensionRequests.isEmpty()) {
	        log.debug("No ExtensionRequest found for Assurance ID: {}", assuranceId);
	        return Collections.emptyList(); // Return an empty list
	    }

	    // Fetch Assurance to get the assuranceNo
	    Assurance assurance = assuranceRepository.findById(assuranceId).orElse(null);
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
	public void deleteExtensionRequest(Long id) {
		
		extensionRepository.deleteById(id);
		return;
		
	}

}