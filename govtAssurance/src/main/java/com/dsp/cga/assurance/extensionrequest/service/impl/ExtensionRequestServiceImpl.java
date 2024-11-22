package com.dsp.cga.assurance.extensionrequest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsp.cga.assurance.extensionrequest.dto.ExtensionRequestDTO;
import com.dsp.cga.assurance.extensionrequest.entity.ExtensionRequest;
import com.dsp.cga.assurance.extensionrequest.repository.ExtensionRequestRepository;
import com.dsp.cga.assurance.extensionrequest.service.ExtensionRequestService;
import com.dsp.cga.mapper.ExtensionRequestMapper;

@Service
public class ExtensionRequestServiceImpl implements ExtensionRequestService {

	@Autowired
	private ExtensionRequestRepository extenionRequestRepository;

	@Override
	public ExtensionRequestDTO createExtensionRequest(ExtensionRequestDTO extensionRequestDTO) {
		ExtensionRequest entensionRequest = ExtensionRequestMapper.toEntity(extensionRequestDTO);

		// Save Assurance entity
		ExtensionRequest savedExtensionRequest = extenionRequestRepository.save(entensionRequest); // Save
																									// once,

		// Map entity back to DTO to return response
		ExtensionRequestDTO savedExtensionRequestDTO = ExtensionRequestMapper.toDTO(savedExtensionRequest);

		return savedExtensionRequestDTO;
	}

}
