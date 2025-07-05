package com.dsp.cga.assurance.otherRequests.service.impl;

import java.time.LocalDate;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dsp.cga.assurance.dto.MemberDTO;
import com.dsp.cga.assurance.dto.SessionDTO;
import com.dsp.cga.assurance.dto.UserDetailsDTO;
import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.assurance.entity.Departments;
import com.dsp.cga.assurance.entity.Ministries;
import com.dsp.cga.assurance.otherRequests.dto.OtherRequestDTO;
import com.dsp.cga.assurance.otherRequests.entity.OtherRequests;
import com.dsp.cga.assurance.otherRequests.repository.OtherRequestRepository;
import com.dsp.cga.assurance.otherRequests.service.OtherRequestService;
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
import com.dsp.cga.response.GenericResponse;
import com.dsp.cga.util.Utils;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;

@Service
public class OtherRequestServiceImpl implements OtherRequestService {

	@Autowired
	private OtherRequestRepository otherRequestRepo;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${member.memberServiceUrl}")
	private String memberServiceUrl;
	
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
	private Utils util;
	
	@Override
	public OtherRequestDTO createOtherRequest(OtherRequestDTO dto) {
		System.out.println(dto.getAssuranceId());
		OtherRequests entity = OtherRequests.builder()
				.assuranceId(dto.getAssuranceId())
				//.subject(dto.getSubject())
                .requestedDate(dto.getRequestedDate())
                .statusId(dto.getStatusId())
                .remarks(dto.getRemarks())
                .fileUrl(dto.getFileUrl())
                .fromMinistry(true)
	            .statusForMinistry(MinistryStatus.SUBMITTED.getValue())
	            .statusForCga(CgaStatus.RECEIVED.getValue())
                .build();

        OtherRequests saved = otherRequestRepo.save(entity);

        return OtherRequestDTO.builder()
                .id(saved.getId())
                .assuranceId(saved.getAssuranceId())
                .subject(saved.getSubject())
                .requestedDate(saved.getRequestedDate())
                .statusId(saved.getStatusId())
                .remarks(saved.getRemarks())
                .fileUrl(saved.getFileUrl())
                .fileUrl(saved.getFileUrl())
                .statusForMinistry(saved.getStatusForMinistry())
                .statusForCga(saved.getStatusForCga())
                .build();
    }

	@Override
	public List<OtherRequestDTO> getFilteredOtherRequests(Long sessionNo, LocalDate requestedDate,
	                                                      String statusForMinistry, Integer ministryCode) {

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

	    if (sessionNo == null) {
//	        sessionNo = (houseId == 1)
//	            ? rssRepo.findLatestByCreatedAt()
//	            : lssRepo.findLatestByCreatedAt();
	        
	    	SessionDTO session = util.getCurrentSession();
	    	sessionNo = session.getLokSabhaSessionNoId() != null 
	    					? session.getLokSabhaSessionNoId() : session.getRajyaSabhaSessionNoId();	        
	    }

	    Long finalSessionNo = sessionNo;

	    // 🔹 Filter on OtherRequests fields only
	    Specification<OtherRequests> specification = (root, query, cb) -> {
	        List<Predicate> predicates = new ArrayList<>();

	        if (statusForMinistry != null) {
	            predicates.add(cb.equal(root.get("statusForMinistry"), statusForMinistry));
	        }

	        if (requestedDate != null) {
	            predicates.add(cb.equal(root.get("requestedDate"), requestedDate));
	        }

	        return cb.and(predicates.toArray(new Predicate[0]));
	    };

	    // 🔹 Fetch and filter based on Assurance
	    String houseAbbr = util.getHouseAbbr();
	    List<OtherRequests> filteredList = otherRequestRepo
	    	    .findAll(specification, Sort.by(Sort.Direction.DESC, "createdAt"))
	    	    .stream()

	        .filter(or -> {
	            if (or.getAssuranceId() == null) return false;

	            Assurance assurance = assuranceRepository.findById(or.getAssuranceId()).orElse(null);
	            if (assurance == null) return false;

	            if (Utils.RAJYA_SABHA.equals(houseAbbr)) {
	                if (assurance.getRajyaSabhaSessionNo() == null) return false;
	                if (!finalSessionNo.equals(assurance.getRajyaSabhaSessionNo())) return false;
	            } else if (Utils.LOK_SABHA.equals(houseAbbr)){
	                if (assurance.getLokSabhaSessionNo() == null || assurance.getLokSabhaNo() == null) return false;
	                if (!finalSessionNo.equals(assurance.getLokSabhaSessionNo())) return false;
	            }

	            if (ministryId != null && !ministryId.equals(assurance.getMinistryId())) return false;

	            return true;
	        })
	      //  .sorted(Comparator.comparing(OtherRequests::getCreatedAt).reversed()) // 🔹 Sort here

	        .collect(Collectors.toList());

	    // 🔹 Map to DTO
	    return filteredList.stream()
	            .sorted(Comparator.comparing(OtherRequests::getCreatedAt).reversed()) // 🔹 Sort here
.map(or -> {
	        Assurance assurance = assuranceRepository.findById(or.getAssuranceId()).orElse(null);

	        return OtherRequestDTO.builder()
	            .id(or.getId())
	            .assuranceNo(assurance != null ? assurance.getAssuranceNo() : null)
	            .status(codeRepository.findCodeById(or.getStatusId()))
	            .questionNo(assurance.getQuestionNo())
	            .requestedDate(or.getRequestedDate())
	            .remarks(or.getRemarks())
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
	            .statusForMinistry(or.getStatusForMinistry())
	            //.subject(assurance.getSubject())
	            .lokSabhaSessionNumber(assurance != null
	                ? lssRepo.findSessionNumberById(assurance.getLokSabhaSessionNo())
	                : null)
	            .lokSabhaNumber(assurance != null
	                ? lssRepo.findLokSabhaNumberById(assurance.getLokSabhaNo())
	                : null)
	            .rajyaSabhaSessionNumber(assurance != null
	                ? rssRepo.findSessionNumberById(assurance.getRajyaSabhaSessionNo())
	                : null)
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
	public OtherRequestDTO getOtherRequestById(Long id) {
		   Optional<OtherRequests> optional = otherRequestRepo.findById(id);
	        if (optional.isEmpty()) return null;

	        OtherRequests or = optional.get();
	        Assurance assurance = null;
	        String memberName = null;
		    String departmentName = null;
		    String ministryName = null;
		    
		    List<MemberDTO> clubbedMembersData = new ArrayList<>();
		    List<MemberDTO> supplementaryClubbedMembersData = new ArrayList<>();

	        if (or.getAssuranceId() != null) {
	            assurance = assuranceRepository.findById(or.getAssuranceId()).orElse(null);
	            
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
        }

	        return OtherRequestDTO.builder()
	            .id(or.getId())
	            .assuranceNo(assurance != null ? assurance.getAssuranceNo() : null)
	            .status(codeRepository.findCodeById(or.getStatusId()))
	           // .subject(assurance.getSubject())
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
	            .memberName(memberName)
	            .ministryName(ministryName)
	            .departmentName(departmentName)
	            .questionNo(assurance.getQuestionNo())
	            .fileUrl(or.getFileUrl())
	            .clubbedMembersData(clubbedMembersData)
	            .supplementaryClubbedMembersData(supplementaryClubbedMembersData) // ✅ Added
	            .requestedDate(or.getRequestedDate())
	            .remarks(or.getRemarks())
	            .lokSabhaSessionNumber(assurance != null
	                ? lssRepo.findSessionNumberById(assurance.getLokSabhaSessionNo()) : null)
	            .lokSabhaNumber(assurance != null
	                ? lssRepo.findLokSabhaNumberById(assurance.getLokSabhaNo()) : null)
	            .rajyaSabhaSessionNumber(assurance != null
	                ? rssRepo.findSessionNumberById(assurance.getRajyaSabhaSessionNo()) : null)
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
		//log.debug("Fetching Member by memberId from URL: {}", url);

		try {
			ResponseEntity<GenericResponse<MemberDTO>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<GenericResponse<MemberDTO>>() {
					});

			if (response == null || response.getBody() == null || response.getBody().getData() == null) {
				//log.error("Response or body is null when fetching Member for memberId: {}", memberId);
				throw new RuntimeException("Failed to fetch Member, response is null");
			}

			MemberDTO memberDTO = response.getBody().getData();
			memberDTO.generateMemberName();
			//log.debug("Fetched MemberDTO: {}", memberDTO);
			return memberDTO;

		} catch (ResourceNotFoundException e) {
		//	log.error("Resource not found for memberId: {}", memberId);
			throw e;
		} catch (Exception e) {
			//log.error("Error fetching Member for memberId: {}", memberId, e);
			throw new RuntimeException("Failed to fetch Member data", e);
		}
	}
	
	@Override
	
	public OtherRequestDTO updateOtherRequestById(Long id, OtherRequestDTO dto) {
	    Optional<OtherRequests> optional = otherRequestRepo.findById(id);
	    if (optional.isEmpty()) {
	        return null;
	    }

	    OtherRequests existing = optional.get();
	    
	   

	    // Check statusForMinistry before allowing update
	    if (existing.getStatusForMinistry() == null || 
	        !existing.getStatusForMinistry().equalsIgnoreCase("Returned")) {
	        throw new IllegalStateException("Update not allowed. statusForMinistry must be 'Returned'.");
	    }
	    
	    // Update status fields
	    existing.setStatusForMinistry(MinistryStatus.SUBMITTED.getValue());
	    existing.setStatusForCga(Utils.getStatusForCga(MinistryStatus.SUBMITTED.getValue()));
	    
	    // Only update fields if they are not null (optional update)
	    if (dto.getAssuranceId() != null) {
	        existing.setAssuranceId(dto.getAssuranceId());
	    }
	    if (dto.getSubject() != null) {
	        existing.setSubject(dto.getSubject());
	    }
	    if (dto.getRequestedDate() != null) {
	        existing.setRequestedDate(dto.getRequestedDate());
	    }
	    if (dto.getStatusId() != null) {
	        existing.setStatusId(dto.getStatusId());
	    }
	    if (dto.getRemarks() != null) {
	        existing.setRemarks(dto.getRemarks());
	    }
	    if (dto.getFileUrl() != null) {
	        existing.setFileUrl(dto.getFileUrl());
	    }

	    OtherRequests updated = otherRequestRepo.save(existing);

	    return OtherRequestDTO.builder()
	            .id(updated.getId())
	            .assuranceId(updated.getAssuranceId())
	            .subject(updated.getSubject())
	            .requestedDate(updated.getRequestedDate())
	            .statusId(updated.getStatusId())
	            .remarks(updated.getRemarks())
	            .fileUrl(updated.getFileUrl())
	            .build();
	}




}
