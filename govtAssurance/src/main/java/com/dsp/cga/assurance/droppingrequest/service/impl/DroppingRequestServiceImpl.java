package com.dsp.cga.assurance.droppingrequest.service.impl;

import java.time.LocalDate;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Comparator;
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
import org.springframework.web.client.RestTemplate;

import com.dsp.cga.assurance.droppingrequest.dto.AssuranceDroppingRequestDTO;
import com.dsp.cga.assurance.droppingrequest.dto.AssuranceDroppingResponseDTO;
import com.dsp.cga.assurance.droppingrequest.dto.DroppingRequestDTO;
import com.dsp.cga.assurance.droppingrequest.entity.DroppingRequest;
import com.dsp.cga.assurance.droppingrequest.repository.DroppingRequestRepository;
import com.dsp.cga.assurance.droppingrequest.service.DroppingRequestService;
import com.dsp.cga.assurance.dto.AcceptedDTO;
import com.dsp.cga.assurance.dto.DroppingTypeDTO;
import com.dsp.cga.assurance.dto.MemberDTO;
import com.dsp.cga.assurance.dto.SessionDTO;
import com.dsp.cga.assurance.dto.StatusDTO;
import com.dsp.cga.assurance.dto.UserDetailsDTO;
import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.assurance.entity.Departments;
import com.dsp.cga.assurance.entity.Ministries;
import com.dsp.cga.assurance.implementationrequest.service.impl.ImplemantationRequestServiceImpl;
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
import com.dsp.cga.eunms.MinistryStatus;
import com.dsp.cga.exception.ResourceNotFoundException;
import com.dsp.cga.mapper.DroppingRequestMapper;
import com.dsp.cga.response.GenericResponse;
import com.dsp.cga.util.Utils;

import jakarta.persistence.criteria.Predicate;

@Service
public class DroppingRequestServiceImpl implements DroppingRequestService {

	private static final Logger log = LoggerFactory.getLogger(ImplemantationRequestServiceImpl.class);

	@Value("${master.codeServiceUrl}")
	private String codeServiceUrl;
	
	@Value("${member.memberServiceUrl}")
	private String memberServiceUrl;

	@Autowired
	private DroppingRequestRepository droppingRequestRepository;

	@Autowired
	private RestTemplate restTemplate;

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
	private AssuranceRepository assuranceRepository;

	@Autowired
	private Utils util;
	
	@Override
	public DroppingRequestDTO getDroppingRequestByAssuranceId(Long id) {
		DroppingRequest Request = droppingRequestRepository.findByAssuranceId(id).orElse(null);

		// If no record is found, return a null DTO and log the message
		if (Request == null) {
			log.debug("No ImplementationRequest found for Assurance ID: {}", id);
			return null; // Return null to handle it at the controller level with custom message
		}

		// Log and map the entity to DTO if found
		log.debug("Fetched ImplementationRequest: {}", Request);

		Assurance assurance = assuranceRepository.findById(id).orElse(null);
		String assuranceNo = (assurance != null) ? assurance.getAssuranceNo() : null;
		Long lokshabaNo = (assurance != null) ? assurance.getLokSabhaNo() : null;
		

		DroppingRequestDTO dto = DroppingRequestMapper.toDTO(Request);
		dto.setAssuranceNo(assuranceNo);

		// Populate additional data if available
		// dto.setStatusDTO(fetchStatusData(Request.getDropStatusId()));
		// System.out.println("$$$$$$$$$$$" + Request.getDropStatusId());
		if (Request.getDroppingType() != null) {
		    dto.setDroppingTypeDTO(fetchDroppingTypeData(Request.getDroppingType()));
		}

		if (Request.getAcceptedId() != null) {
			dto.setAcceptedDTO(fetchAcceptdData(Request.getAcceptedId()));
		}

		log.debug("Returning ImplementationRequestDTO: {}", dto);
		return dto;
	}

	private StatusDTO fetchStatusData(Long dropStatusId) {
		String statusServiceUrl = codeServiceUrl + dropStatusId;
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

	private DroppingTypeDTO fetchDroppingTypeData(Long droppingType) {
	    String droppingTypeServiceUrl = codeServiceUrl + droppingType;
	    log.debug("Calling master service URL for Dropping Type: {}", droppingTypeServiceUrl);

	    ResponseEntity<GenericResponse<DroppingTypeDTO>> response = restTemplate.exchange(
	            droppingTypeServiceUrl, 
	            HttpMethod.GET,
	            null, 
	            new ParameterizedTypeReference<GenericResponse<DroppingTypeDTO>>() {}
	    );

	    if (!response.getStatusCode().is2xxSuccessful()) {
	        log.error("Failed to fetch data from master service for Dropping Type: {} - {}", 
	                  response.getStatusCode(), response.getBody());
	        throw new RuntimeException(
	                "Failed to fetch data from master service for Dropping Type. Status: " + response.getStatusCode());
	    }

	    DroppingTypeDTO droppingTypeDTO = response.getBody().getData();
	    log.debug("Fetched DroppingTypeDTO: {}", droppingTypeDTO);

	    return droppingTypeDTO;
	}

	private AcceptedDTO fetchAcceptdData(Long acceptedId) {
		String statusServiceUrl = codeServiceUrl + acceptedId;
		log.debug("Calling master service URL for Status: {}", statusServiceUrl);

		ResponseEntity<GenericResponse<AcceptedDTO>> response = restTemplate.exchange(statusServiceUrl, HttpMethod.GET,
				null, new ParameterizedTypeReference<GenericResponse<AcceptedDTO>>() {
				});
		System.out.println(response);

		if (!response.getStatusCode().is2xxSuccessful()) {
			log.error("Failed to fetch data from master service for Status: {} - {}", response.getStatusCode(),
					response.getBody());
			throw new RuntimeException(
					"Failed to fetch data from master service for Status. Status: " + response.getStatusCode());
		}

		AcceptedDTO acceptedDTO = response.getBody().getData();
		log.debug("Fetched StatusDTO: {}", acceptedDTO);

		return acceptedDTO;
	}

	@Override
	public GenericResponse<DroppingRequestDTO> createOrUpdateDroppingRequest(DroppingRequestDTO droppingRequestDTO) {
		Optional<DroppingRequest> existingRequestOpt = droppingRequestRepository
				.findByAssuranceId(droppingRequestDTO.getAssuranceId());

		DroppingRequest savedRequest;

		if (existingRequestOpt.isPresent()) {
			DroppingRequest existingRequest = existingRequestOpt.get();
			DroppingRequestMapper.updateEntityFromDTO(droppingRequestDTO, existingRequest);
			savedRequest = droppingRequestRepository.save(existingRequest);
		} else {
			// New to be created. Validate if dropping exists for the assurance
			Long count = droppingRequestRepository.countByAssuranceId(droppingRequestDTO.getAssuranceId());
			
			if (count > 0) {
				return new GenericResponse<>(HttpStatus.FAILED_DEPENDENCY.value(), false,
						"Dropping Request cannot be created as another Dropping Request exists for the Assurance", null);
			}
			
			DroppingRequest newRequest = DroppingRequestMapper.toEntity(droppingRequestDTO);
			savedRequest = droppingRequestRepository.save(newRequest);
		}

		DroppingRequestDTO savedDTO = DroppingRequestMapper.toDTO(savedRequest);
		return new GenericResponse<>(HttpStatus.CREATED.value(), true,
				"DroppingRequest created or updated successfully", savedDTO);
	}


	@Override
	public List<AssuranceDroppingResponseDTO> getFilteredDroppingRequests(Long sessionNo,
	        Long droppingTypeId,
	        LocalDate requestedDate,
	        LocalDate dateOfDropping,
	        String statusForMinistry,
	        Integer ministryCode) {

	    // 🔹 Get ministryId from ministryCode
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

	    // 🔹 Determine houseId
	    Long houseId = getHouseId(); // Implement this method
	    String houseAbbr = util.getHouseAbbr();
	    
	    // 🔹 Get latest session if sessionNo is null
	    if (sessionNo == null) {
	        sessionNo = (houseId == 1)
	                ? rssRepo.findLatestByCreatedAt()
	                : lssRepo.findLatestByCreatedAt();
	       
//	    	SessionDTO session = util.getCurrentSession();
//	    	sessionNo = session.getLokSabhaSessionNoId() != null 
//	    					? session.getLokSabhaSessionNoId() : session.getRajyaSabhaSessionNoId();
	    }

	    Long finalSessionNo = sessionNo;

	    // 🔹 Specification for DroppingRequest entity
	    Specification<DroppingRequest> specification = (root, query, cb) -> {
	        List<Predicate> predicates = new ArrayList<>();

	        if (droppingTypeId != null) {
	            predicates.add(cb.equal(root.get("droppingType"), droppingTypeId));
	        }

	        if (requestedDate != null) {
	            predicates.add(cb.equal(root.get("dropRequestDate"), requestedDate));
	        }

	        if (dateOfDropping != null) {
	            predicates.add(cb.equal(root.get("actualDropDate"), dateOfDropping));
	        }

	        if (statusForMinistry != null) {
	            predicates.add(cb.equal(root.get("statusForMinistry"), statusForMinistry));
	        }

	        return cb.and(predicates.toArray(new Predicate[0]));
	    };

	
	    List<DroppingRequest> filteredList = droppingRequestRepository.findAll(specification, Sort.by(Sort.Direction.DESC, "createdAt")).stream()

	    .filter(dr -> {
	                Assurance assurance = assuranceRepository.findById(dr.getAssuranceId()).orElse(null);
	                if (assurance == null) return false;

	                if (Utils.RAJYA_SABHA.equals(houseAbbr)) {
	                    return finalSessionNo.equals(assurance.getRajyaSabhaSessionNo());
	                } else {
	                    return finalSessionNo.equals(assurance.getLokSabhaSessionNo());
	                }
	            })
	            .filter(dr -> {
	                Assurance assurance = assuranceRepository.findById(dr.getAssuranceId()).orElse(null);
	                if (assurance == null) return false;

	                if (ministryId != null && !ministryId.equals(assurance.getMinistryId())) return false;

	                return true;
	            })
	            .collect(Collectors.toList());

	    // 🔹 Map to DTO
	    return filteredList.stream()
	    		//.sorted(Comparator.comparing(DroppingRequest::getCreatedAt).reversed())

	            .map(dr -> {
	            	 String memberName = null;
	     		    String departmentName = null;
	     		    String ministryName = null;

	     		    Assurance assurance = assuranceRepository.findById(dr.getAssuranceId()).orElse(null);
	     		    if (assurance != null && assurance.getMemberId() != null) {
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

	     		    if (assurance == null) return null;
	               // Assurance assurance = assuranceRepository.findById(dr.getAssuranceId()).orElse(null);
	                return AssuranceDroppingResponseDTO.builder()
	                		.lokSabhaNo(assurance != null
	                        ? lssRepo.findLokSabhaNumberById(assurance.getLokSabhaNo())
	                        : null)
	                        .lokSabhaSessionNo(assurance != null
	                                ? lssRepo.findSessionNumberById(assurance.getLokSabhaSessionNo())
	                                : null)
	                        .rajyaSabhaSessionNo(assurance != null
	                                ? rssRepo.findSessionNumberById(assurance.getRajyaSabhaSessionNo())
	                                : null)
	                        .assuranceNo(assurance.getAssuranceNo())
	                        .partId(dr.getPartId())
	                        .questionNo(assurance.getQuestionNo())
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
	                        
	                        .supplementaryId(dr.getSupplementaryId())
	                      .statusForMinistry(dr.getStatusForMinistry())
	                        .droppingType(assurance != null
	                                ? codeRepository.findCodeById(dr.getDroppingType())
	                                : null)
	                        .dropRequestDate(dr.getDropRequestDate())
	                        .actualDropDate(dr.getActualDropDate())
	                        .memberName(memberName)
	                        .ministryName(ministryName)
	                        .departmentName(departmentName)
	                        .status(assurance != null
	                                ? codeRepository.findCodeById(assurance.getStatusId())
	                                : null)
	                        .remarks(dr.getRemarks())
	                        .id(dr.getId())
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
		public DroppingRequestDTO createDroppingRequest(DroppingRequestDTO droppingRequestDTO) {
			
			// New to be created. Validate if dropping exists for the assurance
			Long count = droppingRequestRepository.countByAssuranceId(droppingRequestDTO.getAssuranceId());
			System.out.println("Droppings Existing for assurance ID: " + count);
			if (count > 0) {
				return null;  
			}
			
			System.out.println(droppingRequestDTO);
			DroppingRequest entity = DroppingRequestMapper.toEntity(droppingRequestDTO);
			System.out.println(entity);
			DroppingRequest saved = droppingRequestRepository.save(entity);
	        return DroppingRequestMapper.toDTO(saved);
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
		public AssuranceDroppingResponseDTO getDroppingRequestById(Long id) {
		    DroppingRequest dr = droppingRequestRepository.findById(id).orElse(null);
		    if (dr == null || dr.getAssuranceId() == null) return null;

		 //   if (assurance == null) return null;
		    
		    String memberName = null;
		    String departmentName = null;
		    String ministryName = null;
		    
		    List<MemberDTO> clubbedMembersData = new ArrayList<>();
		    List<MemberDTO> supplementaryClubbedMembersData = new ArrayList<>();

		    Assurance assurance = assuranceRepository.findById(dr.getAssuranceId()).orElse(null);
		    if (assurance != null && assurance.getMemberId() != null) {
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
		    if (assurance == null) return null;

		    return AssuranceDroppingResponseDTO.builder()
		            .lokSabhaNo(assurance.getLokSabhaNo() != null
		                    ? lssRepo.findLokSabhaNumberById(assurance.getLokSabhaNo())
		                    : null)
		            .lokSabhaSessionNo(assurance.getLokSabhaSessionNo() != null
		                    ? lssRepo.findSessionNumberById(assurance.getLokSabhaSessionNo())
		                    : null)
		            .rajyaSabhaSessionNo(assurance.getRajyaSabhaSessionNo() != null
		                    ? rssRepo.findSessionNumberById(assurance.getRajyaSabhaSessionNo())
		                    : null)
		            .assuranceNo(assurance.getAssuranceNo())
		            .subject(assurance.getSubject())
		            .questionNo(assurance.getQuestionNo())
		            .partId(dr.getPartId())
		            .supplementaryId(dr.getSupplementaryId())
		            .droppingType(codeRepository.findCodeById(dr.getDroppingType()))
		            .dropRequestDate(dr.getDropRequestDate())
		            .actualDropDate(dr.getActualDropDate())
		            .id(dr.getId())
		            .ministryRefDate(dr.getMinistryRefDate())
		            .memberName(memberName)
		            .ministryName(ministryName)
		            .departmentName(departmentName)
		            .ministryRefNo(dr.getMinistryRefNo())
		            .droppingFiles(dr.getDroppingFiles())
		            .status(codeRepository.findCodeById(assurance.getStatusId()))
		            .remarks(dr.getRemarks())
		            .clubbedMembersData(clubbedMembersData)
		            .supplementaryClubbedMembersData(supplementaryClubbedMembersData) // ✅ Added
		            .build();
		}

		@Override
		public AssuranceDroppingResponseDTO updateDroppingRequest(Long id, AssuranceDroppingRequestDTO requestDTO) {
		    DroppingRequest dr = droppingRequestRepository.findById(id).orElse(null);
		    if (dr == null || dr.getAssuranceId() == null) return null;
		    
		    if (dr.getStatusForMinistry() == null || !dr.getStatusForMinistry().equalsIgnoreCase("Returned")) {
		        throw new IllegalStateException("Update not allowed. statusForMinistry must be 'Returned'.");
		    }

		    // Update status fields
		    dr.setStatusForMinistry(MinistryStatus.SUBMITTED.getValue());
		    dr.setStatusForCga(CgaStatus.RECEIVED.getValue());
		    
		    Assurance assurance = assuranceRepository.findById(dr.getAssuranceId()).orElse(null);
		    if (assurance == null) return null;

		    // Update DroppingRequest fields if not null
		    if (requestDTO.getPartId() != null) {
		        dr.setPartId(requestDTO.getPartId());
		    }
		    if (requestDTO.getSupplementaryId() != null) {
		        dr.setSupplementaryId(requestDTO.getSupplementaryId());
		    }
		    if (requestDTO.getDroppingType() != null) {
		        dr.setDroppingType(requestDTO.getDroppingType());
		    }
		    if (requestDTO.getDropRequestDate() != null) {
		        dr.setDropRequestDate(requestDTO.getDropRequestDate());
		    }
		    if (requestDTO.getActualDropDate() != null) {
		        dr.setActualDropDate(requestDTO.getActualDropDate());
		    }
		    if (requestDTO.getMinistryRefDate() != null) {
		        dr.setMinistryRefDate(requestDTO.getMinistryRefDate());
		    }
		    if (requestDTO.getMinistryRefNo() != null) {
		        dr.setMinistryRefNo(requestDTO.getMinistryRefNo());
		    }
		    if (requestDTO.getRemarks() != null) {
		        dr.setRemarks(requestDTO.getRemarks());
		    }

		    // Update Assurance status if provided
		    if (requestDTO.getStatusId() != null) {
		        assurance.setStatusId(requestDTO.getStatusId());
		        assuranceRepository.save(assurance);
		    }

		    droppingRequestRepository.save(dr);

		    // Return updated DTO
		    return getDroppingRequestById(id);
		}


}
