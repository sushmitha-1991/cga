package com.dsp.cga.assurance.clubbedassurance.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dsp.cga.assurance.clubbedassurance.dto.AssuranceIdNoPairDTO;
import com.dsp.cga.assurance.clubbedassurance.dto.ClubbedAssuranceDTO;
import com.dsp.cga.assurance.clubbedassurance.entity.ClubbedAssurance;
import com.dsp.cga.assurance.clubbedassurance.repository.ClubbedAssuranceRepository;
import com.dsp.cga.assurance.clubbedassurance.service.ClubbedAssuranceService;
import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.assurance.repository.AssuranceRepository;
import com.dsp.cga.mapper.ClubbedAssuranceMapper;
import com.dsp.cga.response.GenericResponse;

@Service
public class ClubbedAssuranceServiceImpl implements ClubbedAssuranceService {
	
	@Autowired
	private ClubbedAssuranceRepository clubbedAssuranceRepository;
	
	@Autowired
	private AssuranceRepository assuranceRepository;

	@Override
	public GenericResponse<ClubbedAssuranceDTO> createOrUpdateClubbedAssurance(ClubbedAssuranceDTO clubbedAssuranceDTO) {
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
	    return new GenericResponse<>(
	            HttpStatus.CREATED.value(),
	            true,
	            "ClubbedAssurance created or updated successfully",
	            savedDTO
	    );
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
	    List<ClubbedAssurance> clubbedAssurances = clubbedAssuranceRepository.findClubbedAssuranceByAssuranceId(assuranceId);

	    // Fetch AssuranceNo for the given assuranceId
	    String assuranceNo = fetchAssuranceNoById(assuranceId);

	    return clubbedAssurances.stream()
	            .map(clubbedAssurance -> {
	                // Map the main ClubbedAssurance
	                ClubbedAssuranceDTO dto = ClubbedAssuranceMapper.toDTO(clubbedAssurance);
	                dto.setAssuranceId(assuranceId);
	                dto.setAssuranceNo(assuranceNo);

	                // Fetch AssuranceId and AssuranceNo pairs for clubbedAssuranceIds
	                List<AssuranceIdNoPairDTO> clubbedPairs = clubbedAssurance.getClubbedAssuranceIds().stream()
	                        .map(id -> {
	                            String no = fetchAssuranceNoById(id);
	                            return AssuranceIdNoPairDTO.builder()
	                                    .id(id)
	                                    .no(no)
	                                    .build();
	                        })
	                        .collect(Collectors.toList());
System.out.println(clubbedPairs);
	                dto.setClubbedAssurances(clubbedPairs);
	                return dto;
	            })
	            .collect(Collectors.toList());
	}

	// Fetch AssuranceNo by AssuranceId
	private String fetchAssuranceNoById(Long assuranceId) {
	    Optional<Assurance> assurance = assuranceRepository.findById(assuranceId);
	    return assurance.map(Assurance::getAssuranceNo).orElse(null);
	}





}
