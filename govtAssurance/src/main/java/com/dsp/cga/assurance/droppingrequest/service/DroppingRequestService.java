package com.dsp.cga.assurance.droppingrequest.service;

import com.dsp.cga.assurance.droppingrequest.dto.DroppingRequestDTO;
import com.dsp.cga.response.GenericResponse;

public interface DroppingRequestService {

	DroppingRequestDTO getDroppingRequestByAssuranceId(Long id);

	GenericResponse<DroppingRequestDTO> createOrUpdateDroppingRequest(DroppingRequestDTO droppingRequestDTO);

}
