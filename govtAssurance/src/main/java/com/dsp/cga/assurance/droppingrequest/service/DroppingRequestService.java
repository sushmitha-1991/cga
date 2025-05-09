package com.dsp.cga.assurance.droppingrequest.service;

import java.time.LocalDate;
import java.util.List;

import com.dsp.cga.assurance.droppingrequest.dto.AssuranceDroppingResponseDTO;
import com.dsp.cga.assurance.droppingrequest.dto.DroppingRequestDTO;
import com.dsp.cga.response.GenericResponse;

public interface DroppingRequestService {

	DroppingRequestDTO getDroppingRequestByAssuranceId(Long id);

	GenericResponse<DroppingRequestDTO> createOrUpdateDroppingRequest(DroppingRequestDTO droppingRequestDTO);

	List<AssuranceDroppingResponseDTO> getFilteredDroppingRequests(Long sessionNo, Long droppingTypeId,
			LocalDate requestedDate, LocalDate dateOfDropping, Long statusId, Integer ministryCode);

	DroppingRequestDTO createDroppingRequest(DroppingRequestDTO droppingRequestDTO);

}
