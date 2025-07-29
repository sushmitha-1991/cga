package com.dsp.cga.assurance.otherRequests.service;

import java.time.LocalDate;
import java.util.List;

import com.dsp.cga.assurance.otherRequests.dto.OtherRequestDTO;

public interface OtherRequestService {

	OtherRequestDTO createOtherRequest(OtherRequestDTO dto);

	List<OtherRequestDTO> getFilteredOtherRequests(Long sessionNo, LocalDate requestedDate, String statusForMinistry,
			Integer ministryCode);

	OtherRequestDTO getOtherRequestById(Long id);

	OtherRequestDTO updateOtherRequestById(Long id, OtherRequestDTO dto);

	OtherRequestDTO getAcceptedRequest(Long assuranceId);

}
