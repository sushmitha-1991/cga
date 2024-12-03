package com.dsp.cga.assurance.clubbedassurance.service;

import com.dsp.cga.assurance.clubbedassurance.dto.ClubbedAssuranceDTO;
import com.dsp.cga.response.GenericResponse;

public interface ClubbedAssuranceService {

	GenericResponse<ClubbedAssuranceDTO> createOrUpdateClubbedAssurance(ClubbedAssuranceDTO clubbedAssuranceDTO);

	ClubbedAssuranceDTO getClubbedAssuranceByAssuranceId(Long id);

}
