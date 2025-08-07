package com.dsp.cga.assurance.clubbedassurance.service.impl;

import java.time.LocalDate;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dsp.cga.assurance.clubbedassurance.dto.AssuranceIdNoPairDTO;
import com.dsp.cga.assurance.clubbedassurance.dto.AssuranceResponseDTO;
import com.dsp.cga.assurance.clubbedassurance.dto.ClubbedAssuranceDTO;
import com.dsp.cga.assurance.clubbedassurance.entity.ClubbedAssurance;
import com.dsp.cga.assurance.clubbedassurance.repository.ClubbedAssuranceRepository;
import com.dsp.cga.assurance.clubbedassurance.service.ClubbedAssuranceService;
import com.dsp.cga.assurance.droppingrequest.dto.DroppingRequestDTO;
import com.dsp.cga.assurance.droppingrequest.entity.DroppingRequest;
import com.dsp.cga.assurance.dto.SessionDTO;
import com.dsp.cga.assurance.dto.UserDetailsDTO;
import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.assurance.entity.Departments;
import com.dsp.cga.assurance.entity.Ministries;
import com.dsp.cga.assurance.partdetails.entity.PartDetails;
import com.dsp.cga.assurance.repository.AssuranceRepository;
import com.dsp.cga.assurance.repository.CodeRepository;
import com.dsp.cga.assurance.repository.DepartmentRepository;
import com.dsp.cga.assurance.repository.MasterLSSRepository;
import com.dsp.cga.assurance.repository.MastersRSSRepository;
import com.dsp.cga.assurance.repository.MinistryRepository;
import com.dsp.cga.assurance.repository.PersonalDetailsRepository;
import com.dsp.cga.eunms.CgaStatus;
import com.dsp.cga.eunms.MinistryStatus;
import com.dsp.cga.mapper.ClubbedAssuranceMapper;
import com.dsp.cga.mapper.DroppingRequestMapper;
import com.dsp.cga.response.GenericResponse;
import com.dsp.cga.util.Utils;

import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ClubbedAssuranceServiceImpl implements ClubbedAssuranceService {

	@Autowired
	private ClubbedAssuranceRepository clubbedAssuranceRepository;

	@Autowired
	private AssuranceRepository assuranceRepository;

	@Autowired
	private MinistryRepository ministryRepository;

	@Autowired
	private MasterLSSRepository lssRepo;
	
	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;
	
	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private MastersRSSRepository rssRepo;

	@Autowired
	private CodeRepository codeRepository;
	
	@Autowired
	private Utils util;

	@Override
	public GenericResponse<ClubbedAssuranceDTO> createOrUpdateClubbedAssurance(
			ClubbedAssuranceDTO clubbedAssuranceDTO) {
		Optional<ClubbedAssurance> existingAssuranceOpt = clubbedAssuranceRepository
				.findByAssuranceId(clubbedAssuranceDTO.getAssuranceId());

		ClubbedAssurance savedAssurance;

		if (existingAssuranceOpt.isPresent()) {
			ClubbedAssurance existingAssurance = existingAssuranceOpt.get();
			System.out.println("Existing CA: " + existingAssurance);
			System.out.println("DTO CA: " + clubbedAssuranceDTO);

			ClubbedAssuranceMapper.updateEntityFromDTO(clubbedAssuranceDTO, existingAssurance);
			
			System.out.println("Updated CA: " + existingAssurance);

			savedAssurance = clubbedAssuranceRepository.save(existingAssurance);
		} else {
			ClubbedAssurance newAssurance = ClubbedAssuranceMapper.toEntity(clubbedAssuranceDTO);
			savedAssurance = clubbedAssuranceRepository.save(newAssurance);
		}

		ClubbedAssuranceDTO savedDTO = ClubbedAssuranceMapper.toDTO(savedAssurance);
		return new GenericResponse<>(HttpStatus.CREATED.value(), true,
				"Clubbed Assurance created or updated successfully", savedDTO);
	}

//	@Override
//	public ClubbedAssuranceDTO getClubbedAssuranceByAssuranceId(Long id) {
//	    // Fetch ClubbedAssurance by Assurance ID
//	    ClubbedAssurance clubbedAssurance = clubbedAssuranceRepository.findByAssuranceId(id).orElse(null);
//
//	    // If no record is found, return null and log the message
//	    if (clubbedAssurance == null) {
//	       // log.debug("No ClubbedAssurance found for Assurance ID: {}", id);
//	        return null; // Return null to handle at the controller level with a custom message
//	    }
//
//	    // Fetch additional Assurance data
////	    Assurance assurance = assuranceRepository.findById(id).orElse(null);
////	    String assuranceNo = (assurance != null) ? assurance.getAssuranceNo() : null;
//
//	    // Map the ClubbedAssurance entity to DTO
//	    ClubbedAssuranceDTO dto = ClubbedAssuranceMapper.toDTO(clubbedAssurance);
//	   // dto.setAssuranceNo(assuranceNo);
//
//	    // Populate additional data if available
//	  //  dto.setAdditionalData(fetchAdditionalData(clubbedAssurance.getAdditionalFieldId()));
//
//	   // log.debug("Returning ClubbedAssuranceDTO: {}", dto);
//	    return dto;
//	}

	@Override
	public List<ClubbedAssuranceDTO> getClubbedAssurancesByAssuranceId(Long assuranceId) {
		List<ClubbedAssurance> clubbedAssurances = clubbedAssuranceRepository
				.findClubbedAssuranceByAssuranceId(assuranceId);

		// Fetch AssuranceNo for the given assuranceId
		String assuranceNo = fetchAssuranceNoById(assuranceId);

		return clubbedAssurances.stream().map(clubbedAssurance -> {
			// Map the main ClubbedAssurance
			ClubbedAssuranceDTO dto = ClubbedAssuranceMapper.toDTO(clubbedAssurance);
			dto.setAssuranceId(assuranceId);
			dto.setAssuranceNo(assuranceNo);

			// Fetch AssuranceId and AssuranceNo pairs for clubbedAssuranceIds
			List<AssuranceIdNoPairDTO> clubbedPairs = clubbedAssurance.getClubbedAssuranceIds().stream().map(id -> {
				String no = fetchAssuranceNoById(id);
				return AssuranceIdNoPairDTO.builder().id(id).no(no).build();
			}).collect(Collectors.toList());
			System.out.println(clubbedPairs);
			dto.setClubbedAssurances(clubbedPairs);
			return dto;
		}).collect(Collectors.toList());
	}

	// Fetch AssuranceNo by AssuranceId
	private String fetchAssuranceNoById(Long assuranceId) {
		Optional<Assurance> assurance = assuranceRepository.findById(assuranceId);
		return assurance.map(Assurance::getAssuranceNo).orElse(null);
	}


	@Override
	public List<AssuranceResponseDTO> getFilteredClubbedAssurances(Long sessionNo, LocalDate assuranceDate,
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

	    String houseAbbr = util.getHouseAbbr();
	    
	    Long houseId = Utils.getHouseId();
	    
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

	    // 🔹 Get all clubbed assurance records (sorted by latest first)
	    List<ClubbedAssurance> allClubbedAssurances = clubbedAssuranceRepository
	            .findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

	    // 🔹 Filter them based on Assurance fields
	    List<ClubbedAssurance> filteredClubbedList = allClubbedAssurances.stream()
	        .filter(clubbed -> {
	            Assurance assurance = assuranceRepository.findById(clubbed.getAssuranceId()).orElse(null);
	            if (assurance == null) return false;

	            if (assuranceDate != null && !assuranceDate.equals(assurance.getAssuranceDate())) return false;
	            
	            //if (statusId != null && !statusId.equals(assurance.getStatusId())) return false;
	            if (!StringUtils.isEmpty(statusForMinistry) && 
	            		!statusForMinistry.equals(clubbed.getStatusForMinistry())) return false;
	            
	            if (ministryId != null && !ministryId.equals(assurance.getMinistryId())) return false;

//	            if (houseId == 1 && !finalSessionNo.equals(assurance.getRajyaSabhaSessionNo())) return false;
//	            if (houseId != 1 && !finalSessionNo.equals(assurance.getLokSabhaSessionNo())) return false;
	            
	            if (Utils.RAJYA_SABHA.equals(houseAbbr) && !finalSessionNo.equals(assurance.getRajyaSabhaSessionNo())) return false;
	             
	            if (Utils.LOK_SABHA.equals(houseAbbr) && !finalSessionNo.equals(assurance.getLokSabhaSessionNo())) return false;

	            return true;
	        })
	        .collect(Collectors.toList());

	    // 🔹 Map to DTO
	    return filteredClubbedList.stream()
	        .map(clubbed -> {
	            Assurance assurance = assuranceRepository.findById(clubbed.getAssuranceId()).orElse(null);

	            List<Long> clubbedIds = clubbed.getClubbedAssuranceIds() != null
	                    ? clubbed.getClubbedAssuranceIds().stream().distinct().toList()
	                    : List.of();

	            List<String> clubbedAssuranceNos = new ArrayList<>();
	            if (clubbed.getClubbedAssuranceIds() != null) {
	                clubbed.getClubbedAssuranceIds().stream().distinct().forEach(id -> {
	                    assuranceRepository.findById(id).ifPresent(ca -> {
	                        if (ca.getAssuranceNo() != null) {
	                            clubbedAssuranceNos.add(ca.getAssuranceNo());
	                        }
	                    });
	                });
	            }

	            return AssuranceResponseDTO.builder()
	                    .id(clubbed.getId())
	                    .assuranceNo(assurance != null ? assurance.getAssuranceNo() : null)
	                    .lokSabhaSessionNo(assurance != null && assurance.getLokSabhaSessionNo() != null
	                            ? lssRepo.findSessionNumberById(assurance.getLokSabhaSessionNo()) : null)
	                    .lokSabhaNo(assurance != null && assurance.getLokSabhaNo() != null
	                            ? lssRepo.findLokSabhaNumberById(assurance.getLokSabhaNo()) : null)
	                    .rajyaSabhaSessionNo(assurance != null && assurance.getRajyaSabhaSessionNo() != null
	                            ? rssRepo.findSessionNumberById(assurance.getRajyaSabhaSessionNo()) : null)
	                    .questionNo(assurance != null ? assurance.getQuestionNo() : null)
	                    .status(assurance != null
	                            ? codeRepository.findCodeById(assurance.getStatusId()) : null)
	                    .clubbedAssuranceIds(clubbedIds)
	                    .clubbedAssuranceNos(clubbedAssuranceNos)
	                    .statusForMinistry(clubbed.getStatusForMinistry())
	                    .subject(assurance.getSubject() != null 
	                        ? assurance.getSubject() 
	                        : (assurance.getPartDetails() != null && !assurance.getPartDetails().isEmpty()
	                            ? assurance.getPartDetails().stream()
	                                .sorted(Comparator.comparing(PartDetails::getId).reversed()) // latest part
	                                .map(PartDetails::getSubject)
	                                .filter(Objects::nonNull)
	                                .findFirst()
	                                .orElse(null)
	                            : null))
	                    .date(clubbed.getDate())
	                    .memberName(assurance != null && assurance.getMemberId() != null
	                        ? personalDetailsRepository.findMemberNameByMemberId(assurance.getMemberId()) : null)
	                    .departmentName(assurance != null && assurance.getDepartmentId() != null
	                        ? departmentRepository.findById(assurance.getDepartmentId()).map(Departments::getDepartmentName).orElse(null)
	                        : null)
	                    .ministryName(assurance != null && assurance.getMinistryId() != null
	                        ? ministryRepository.findById(assurance.getMinistryId()).map(Ministries::getMinistryName).orElse(null)
	                        : null)
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
	public ClubbedAssuranceDTO createClubbedAssurance(ClubbedAssuranceDTO dto) {
		
		        ClubbedAssurance entity = ClubbedAssurance.builder()
		                .assuranceId(dto.getAssuranceId())
		                .clubbedAssuranceIds(dto.getClubbedAssuranceIds())
		                .date(dto.getDate())
			             .statusForMinistry(MinistryStatus.SUBMITTED.getValue())
			             .statusForCga(Utils.getStatusForCga(MinistryStatus.SUBMITTED.getValue()))
			             .fromMinistry(true)
		                .build();

		        ClubbedAssurance saved = clubbedAssuranceRepository.save(entity);

		        return ClubbedAssuranceDTO.builder()
		                .id(saved.getId())
		                .assuranceId(saved.getAssuranceId())
		                .clubbedAssuranceIds(saved.getClubbedAssuranceIds())
		                .date(saved.getDate())
		                .statusForMinistry(saved.getStatusForMinistry())
		                .statusForCga(saved.getStatusForCga())
		                .fromMinistry(saved.getFromMinistry())
		                .build();
		    }



	
	@Override
	public AssuranceResponseDTO getClubbedAssuranceById(Long id) {
	    ClubbedAssurance clubbed = clubbedAssuranceRepository.findById(id).orElse(null);
	    if (clubbed == null) return null;

	    Assurance assurance = assuranceRepository.findById(clubbed.getAssuranceId()).orElse(null);
	    if (assurance == null) return null;

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

	    List<Long> clubbedIds = clubbed.getClubbedAssuranceIds() != null
	        ? clubbed.getClubbedAssuranceIds().stream().distinct().toList()
	        : List.of();

	    // Get assuranceNos from the clubbed assuranceIds
	    List<String> clubbedAssuranceNos = new ArrayList<>();
	    if (clubbed.getClubbedAssuranceIds() != null) {
	        clubbed.getClubbedAssuranceIds().stream().distinct().forEach(clubbedId -> {
	            assuranceRepository.findById(clubbedId).ifPresent(clubbedAssurance -> {
	                if (clubbedAssurance.getAssuranceNo() != null) {
	                    clubbedAssuranceNos.add(clubbedAssurance.getAssuranceNo());
	                }
	            });
	        });
	    }

	    return AssuranceResponseDTO.builder()
	        .id(clubbed.getId())
	        .assuranceNo(assurance.getAssuranceNo())
	        .lokSabhaSessionNo(assurance.getLokSabhaSessionNo() != null
	            ? lssRepo.findSessionNumberById(assurance.getLokSabhaSessionNo()) : null)
	        .lokSabhaNo(assurance.getLokSabhaNo() != null
	            ? lssRepo.findLokSabhaNumberById(assurance.getLokSabhaNo()) : null)
	        .rajyaSabhaSessionNo(assurance.getRajyaSabhaSessionNo() != null
	            ? rssRepo.findSessionNumberById(assurance.getRajyaSabhaSessionNo()) : null)
	        .assuranceDate(assurance.getAssuranceDate())
	        .questionNo(assurance.getQuestionNo())
	        .status(codeRepository.findCodeById(assurance.getStatusId()))
	        .clubbedAssuranceIds(clubbedIds)
	        .clubbedAssuranceNos(clubbedAssuranceNos) // Add this in your DTO
	        .memberName(memberName)
	        .departmentName(departmentName)
	        .ministryName(ministryName)
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
	        //.subject(assurance.getSubject())
	        .date(clubbed.getDate())
	        .statusForMinistry(clubbed.getStatusForMinistry())
	        .statusForCga(clubbed.getStatusForCga())		
	        .build();
	}

	@Override
	public AssuranceResponseDTO updateClubbedAssuranceById(Long id, ClubbedAssuranceDTO dto) {
		// TODO Auto-generated method stub
		 ClubbedAssurance clubbed = clubbedAssuranceRepository.findById(id).orElse(null);
		    if (clubbed == null) return null;
		    
		    if (clubbed.getStatusForMinistry() == null || 
		            !clubbed.getStatusForMinistry().equalsIgnoreCase("Returned")) {
		            throw new IllegalStateException("Update not allowed. statusForMinistry must be 'Returned'.");
		        }

		    // Update status fields
		    clubbed.setStatusForMinistry(MinistryStatus.SUBMITTED.getValue());
		    clubbed.setStatusForCga(CgaStatus.RECEIVED.getValue());
		    
		    if (dto.getAssuranceId() != null) {
		        clubbed.setAssuranceId(dto.getAssuranceId());
		    }
		    
		    if (dto.getDate() != null) {
		        clubbed.setDate(dto.getDate());
		    }
		    if (dto.getClubbedAssuranceIds() != null) {
		        clubbed.setClubbedAssuranceIds(dto.getClubbedAssuranceIds());
		    }

		    clubbedAssuranceRepository.save(clubbed);

		    Assurance assurance = assuranceRepository.findById(clubbed.getAssuranceId()).orElse(null);
		    List<Long> clubbedIds = clubbed.getClubbedAssuranceIds() != null
		            ? clubbed.getClubbedAssuranceIds().stream().distinct().toList()
		            : List.of();

		    return AssuranceResponseDTO.builder()
		            .id(clubbed.getId())
		            .assuranceNo(assurance != null ? assurance.getAssuranceNo() : null)
		            .lokSabhaSessionNo(assurance != null && assurance.getLokSabhaSessionNo() != null
		                    ? lssRepo.findSessionNumberById(assurance.getLokSabhaSessionNo()) : null)
		            .lokSabhaNo(assurance != null && assurance.getLokSabhaNo() != null
		                    ? lssRepo.findLokSabhaNumberById(assurance.getLokSabhaNo()) : null)
		            .rajyaSabhaSessionNo(assurance != null && assurance.getRajyaSabhaSessionNo() != null
		                    ? rssRepo.findSessionNumberById(assurance.getRajyaSabhaSessionNo()) : null)
		            .assuranceDate(assurance != null ? assurance.getAssuranceDate() : null)
		            .questionNo(assurance != null ? assurance.getQuestionNo() : null)
		            .status(assurance != null ? codeRepository.findCodeById(assurance.getStatusId()) : null)
		            .clubbedAssuranceIds(clubbedIds)
		            .build();
		}

	
	@Override
	public ClubbedAssuranceDTO getAcceptedRequest (Long assuranceId) {
		
	    // Fetch the list of ExtensionRequest entities by Assurance ID
	    ClubbedAssurance clubRequest = clubbedAssuranceRepository.getAcceptedRequest(assuranceId);

	    // If no records are found, log the message and return an empty list
	    if (clubRequest == null) {
	        log.debug("No Clubbing Request found for Assurance ID: {}", assuranceId); 
	        return null;
	    }

	    // Fetch Assurance to get the assuranceNo
	    Assurance assurance = assuranceRepository.findById(assuranceId).orElse(null);
	    String assuranceNo = (assurance != null) ? assurance.getAssuranceNo() : null;

	    // Map the list of entities to a list of DTOs and set the assuranceNo in DTO
	    ClubbedAssuranceDTO dto = ClubbedAssuranceMapper.toDTO(clubRequest);
        dto.setAssuranceNo(assuranceNo);
        
		// Fetch AssuranceId and AssuranceNo pairs for clubbedAssuranceIds
		List<AssuranceIdNoPairDTO> clubbedPairs = clubRequest.getClubbedAssuranceIds().stream().map(id -> {
			String no = fetchAssuranceNoById(id);
			return AssuranceIdNoPairDTO.builder().id(id).no(no).build();
		}).collect(Collectors.toList());
		dto.setClubbedAssurances(clubbedPairs);  
	                
        return dto;

	}			
}
