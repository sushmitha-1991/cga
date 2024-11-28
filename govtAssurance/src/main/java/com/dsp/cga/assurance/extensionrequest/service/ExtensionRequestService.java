package com.dsp.cga.assurance.extensionrequest.service;

import java.util.List;

import com.dsp.cga.assurance.extensionrequest.dto.ExtensionRequestDTO;
import com.dsp.cga.response.GenericResponse;

public interface ExtensionRequestService {

	ExtensionRequestDTO createExtensionRequest(ExtensionRequestDTO extensionRequestDTO);

	List<ExtensionRequestDTO> getExtensionRequestByAssuranceId(Long id);

	GenericResponse<ExtensionRequestDTO> createOrUpdateExtensionRequest(ExtensionRequestDTO extensionRequestDTO);

}
