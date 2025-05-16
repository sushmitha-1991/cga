package com.dsp.cga.assurance.clubbedassurance.service;

import java.time.LocalDate;
import java.util.List;

import com.dsp.cga.assurance.clubbedassurance.dto.AssuranceResponseDTO;
import com.dsp.cga.assurance.clubbedassurance.dto.ClubbedAssuranceDTO;
import com.dsp.cga.response.GenericResponse;

public interface ClubbedAssuranceService {

	GenericResponse<ClubbedAssuranceDTO> createOrUpdateClubbedAssurance(ClubbedAssuranceDTO clubbedAssuranceDTO);

	//ClubbedAssuranceDTO getClubbedAssuranceByAssuranceId(Long id);

	List<ClubbedAssuranceDTO> getClubbedAssurancesByAssuranceId(Long assuranceId);

	List<AssuranceResponseDTO> getFilteredClubbedAssurances(Long sessionNo, LocalDate assuranceDate, Long statusId,
			Integer ministryCode);

	ClubbedAssuranceDTO createClubbedAssurance(ClubbedAssuranceDTO dto);

	AssuranceResponseDTO getClubbedAssuranceById(Long id);

	AssuranceResponseDTO updateClubbedAssuranceById(Long id, ClubbedAssuranceDTO dto);
	
	

}
