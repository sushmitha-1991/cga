package com.dsp.cga.assurance.clubbedassurance.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.dsp.cga.assurance.dto.UserDetailsDTO;
import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.assurance.entity.Ministries;
import com.dsp.cga.assurance.repository.AssuranceRepository;
import com.dsp.cga.assurance.repository.CodeRepository;
import com.dsp.cga.assurance.repository.MasterLSSRepository;
import com.dsp.cga.assurance.repository.MastersRSSRepository;
import com.dsp.cga.assurance.repository.MinistryRepository;
import com.dsp.cga.mapper.ClubbedAssuranceMapper;
import com.dsp.cga.response.GenericResponse;

import jakarta.persistence.criteria.Predicate;

@Service
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
	private MastersRSSRepository rssRepo;

	@Autowired
	private CodeRepository codeRepository;

	@Override
	public GenericResponse<ClubbedAssuranceDTO> createOrUpdateClubbedAssurance(
			ClubbedAssuranceDTO clubbedAssuranceDTO) {
		Optional<ClubbedAssurance> existingAssuranceOpt = clubbedAssuranceRepository
				.findByAssuranceId(clubbedAssuranceDTO.getAssuranceId());

		ClubbedAssurance savedAssurance;

		if (existingAssuranceOpt.isPresent()) {
			ClubbedAssurance existingAssurance = existingAssuranceOpt.get();
			ClubbedAssuranceMapper.updateEntityFromDTO(clubbedAssuranceDTO, existingAssurance);
			savedAssurance = clubbedAssuranceRepository.save(existingAssurance);
		} else {
			ClubbedAssurance newAssurance = ClubbedAssuranceMapper.toEntity(clubbedAssuranceDTO);
			savedAssurance = clubbedAssuranceRepository.save(newAssurance);
		}

		ClubbedAssuranceDTO savedDTO = ClubbedAssuranceMapper.toDTO(savedAssurance);
		return new GenericResponse<>(HttpStatus.CREATED.value(), true,
				"ClubbedAssurance created or updated successfully", savedDTO);
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

	    // 🔹 Get current houseId
	    Long houseId = getHouseId(); // 1 = RS, else LS

	    // 🔹 Get latest session if sessionNo is null
	    if (sessionNo == null) {
	        sessionNo = (houseId == 1)
	                ? rssRepo.findLatestByCreatedAt()
	                : lssRepo.findLatestByCreatedAt();
	    }
	    Long finalSessionNo = sessionNo;

	    // 🔹 Get all clubbed assurance records
	    List<ClubbedAssurance> allClubbedAssurances = clubbedAssuranceRepository.findAll();

	    // 🔹 Filter them based on Assurance fields
	    List<ClubbedAssurance> filteredClubbedList = allClubbedAssurances.stream()
	        .filter(clubbed -> {
	            Assurance assurance = assuranceRepository.findById(clubbed.getAssuranceId()).orElse(null);
	            if (assurance == null) return false;

	            if (assuranceDate != null && !assuranceDate.equals(assurance.getAssuranceDate())) return false;
	            if (statusId != null && !statusId.equals(assurance.getStatusId())) return false;
	            if (ministryId != null && !ministryId.equals(assurance.getMinistryId())) return false;

	            if (houseId == 1 && !finalSessionNo.equals(assurance.getRajyaSabhaSessionNo())) return false;
	            if (houseId != 1 && !finalSessionNo.equals(assurance.getLokSabhaSessionNo())) return false;

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

	            return AssuranceResponseDTO.builder()
	                    .assuranceNo(assurance != null ? assurance.getAssuranceNo() : null)
	                    .lokSabhaSessionNo(assurance != null && assurance.getLokSabhaSessionNo() != null
	                            ? lssRepo.findSessionNumberById(assurance.getLokSabhaSessionNo()) : null)
	                    .lokSabhaNo(assurance != null && assurance.getLokSabhaNo() != null
	                            ? lssRepo.findLokSabhaNumberById(assurance.getLokSabhaNo()) : null)
	                    .rajyaSabhaSessionNo(assurance != null && assurance.getRajyaSabhaSessionNo() != null
	                            ? rssRepo.findSessionNumberById(assurance.getRajyaSabhaSessionNo()) : null)
	                    .assuranceDate(assurance != null ? assurance.getAssuranceDate() : null)
	                    .questionNo(assurance != null ? assurance.getQuestionNo() : null)
	                    .status(assurance != null
	                            ? codeRepository.findCodeById(assurance.getStatusId()) : null)
	                    .clubbedAssuranceIds(clubbedIds)
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
		                .build();

		        ClubbedAssurance saved = clubbedAssuranceRepository.save(entity);

		        return ClubbedAssuranceDTO.builder()
		                .id(saved.getId())
		                .assuranceId(saved.getAssuranceId())
		                .clubbedAssuranceIds(saved.getClubbedAssuranceIds())
		                .date(saved.getDate())
		                .build();
		    }

}
