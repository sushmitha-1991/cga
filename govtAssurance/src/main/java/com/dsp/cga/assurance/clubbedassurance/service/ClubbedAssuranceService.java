package com.dsp.cga.assurance.clubbedassurance.service;

import java.util.List;

import com.dsp.cga.assurance.clubbedassurance.dto.ClubbedAssuranceDTO;
import com.dsp.cga.response.GenericResponse;

public interface ClubbedAssuranceService {

	GenericResponse<ClubbedAssuranceDTO> createOrUpdateClubbedAssurance(ClubbedAssuranceDTO clubbedAssuranceDTO);

	//ClubbedAssuranceDTO getClubbedAssuranceByAssuranceId(Long id);

	List<ClubbedAssuranceDTO> getClubbedAssurancesByAssuranceId(Long assuranceId);

}
