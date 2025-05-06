package com.dsp.cga.assurance.extensionrequest.service;

import java.time.LocalDate;
import java.util.List;

import com.dsp.cga.assurance.extensionrequest.dto.ExtensionRequestDTO;
import com.dsp.cga.assurance.extensionrequest.dto.ExtensionRequestResponseDTO;
import com.dsp.cga.response.GenericResponse;

public interface ExtensionRequestService {

	ExtensionRequestDTO createExtensionRequest(ExtensionRequestDTO extensionRequestDTO);

	List<ExtensionRequestDTO> getExtensionRequestByAssuranceId(Long id);

	//GenericResponse<ExtensionRequestDTO> createOrUpdateExtensionRequest(ExtensionRequestDTO extensionRequestDTO);

	GenericResponse<List<ExtensionRequestDTO>> createOrUpdateExtensionRequests(
			List<ExtensionRequestDTO> extensionRequestDTOList);

	List<ExtensionRequestResponseDTO> getFilteredExtensions(Long sessionNo, Long statusId,
			LocalDate extRequestedTillDate, LocalDate extGrantedTillDate, LocalDate ministryRefDate, Integer ministryCode);

}
