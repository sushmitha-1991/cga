package com.dsp.cga.assurance.otherRequests.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dsp.cga.assurance.dto.UserDetailsDTO;
import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.assurance.entity.Departments;
import com.dsp.cga.assurance.entity.Ministries;
import com.dsp.cga.assurance.otherRequests.dto.OtherRequestDTO;
import com.dsp.cga.assurance.otherRequests.entity.OtherRequests;
import com.dsp.cga.assurance.otherRequests.repository.OtherRequestRepository;
import com.dsp.cga.assurance.otherRequests.service.OtherRequestService;
import com.dsp.cga.assurance.repository.AssuranceRepository;
import com.dsp.cga.assurance.repository.CodeRepository;
import com.dsp.cga.assurance.repository.DepartmentRepository;
import com.dsp.cga.assurance.repository.MasterLSSRepository;
import com.dsp.cga.assurance.repository.MastersRSSRepository;
import com.dsp.cga.assurance.repository.MinistryRepository;
import com.dsp.cga.assurance.repository.PersonalDetailsRepository;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;

@Service
public class OtherRequestServiceImpl implements OtherRequestService {

	@Autowired
	private OtherRequestRepository otherRequestRepo;
	
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
	             .statusForMinistry("Submitted")
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
                .build();
    }

	@Override
	public List<OtherRequestDTO> getFilteredOtherRequests(Long sessionNo, LocalDate requestedDate,
	                                                      Long statusId, Integer ministryCode) {

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

	    // 🔹 Determine houseId (1 = Rajya Sabha, 2 = Lok Sabha)
	    Long houseId = getHouseId();

	    if (sessionNo == null) {
	        sessionNo = (houseId == 1)
	            ? rssRepo.findLatestByCreatedAt()
	            : lssRepo.findLatestByCreatedAt();
	    }

	    Long finalSessionNo = sessionNo;

	    // 🔹 Filter on OtherRequests fields only
	    Specification<OtherRequests> specification = (root, query, cb) -> {
	        List<Predicate> predicates = new ArrayList<>();

	        if (statusId != null) {
	            predicates.add(cb.equal(root.get("statusId"), statusId));
	        }

	        if (requestedDate != null) {
	            predicates.add(cb.equal(root.get("requestedDate"), requestedDate));
	        }

	        return cb.and(predicates.toArray(new Predicate[0]));
	    };

	    // 🔹 Fetch and filter based on Assurance
	    List<OtherRequests> filteredList = otherRequestRepo.findAll(specification).stream()
	        .filter(or -> {
	            if (or.getAssuranceId() == null) return false;

	            Assurance assurance = assuranceRepository.findById(or.getAssuranceId()).orElse(null);
	            if (assurance == null) return false;

	            if (houseId == 1) {
	                if (assurance.getRajyaSabhaSessionNo() == null) return false;
	                if (!finalSessionNo.equals(assurance.getRajyaSabhaSessionNo())) return false;
	            } else {
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
	            .statusForMinistry(or.getStatusForMinistry())
	            .subject(assurance.getSubject())
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
	                ministryName = ministryRepository.findById(assurance.getDepartmentId())
	                    .map(Ministries::getMinistryName)
	                    .orElse(null);
	            }
	        }

	        return OtherRequestDTO.builder()
	            .id(or.getId())
	            .assuranceNo(assurance != null ? assurance.getAssuranceNo() : null)
	            .status(codeRepository.findCodeById(or.getStatusId()))
	            .memberName(memberName)
	            .ministryName(ministryName)
	            .departmentName(departmentName)
	            .questionNo(assurance.getQuestionNo())
	            .fileUrl(or.getFileUrl())
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
