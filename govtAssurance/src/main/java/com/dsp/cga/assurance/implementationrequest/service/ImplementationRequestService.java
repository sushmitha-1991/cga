package com.dsp.cga.assurance.implementationrequest.service;

import com.dsp.cga.assurance.implementationrequest.dto.ImplementationRequestDTO;
import com.dsp.cga.response.GenericResponse;

public interface ImplementationRequestService {

	ImplementationRequestDTO createImplementationRequest(ImplementationRequestDTO implementationRequestDTO);

	GenericResponse<ImplementationRequestDTO> createOrUpdateImplementationRequest(
			ImplementationRequestDTO implementationRequestDTO);

	ImplementationRequestDTO getImplementationRequestByAssuranceId(Long id);

}
