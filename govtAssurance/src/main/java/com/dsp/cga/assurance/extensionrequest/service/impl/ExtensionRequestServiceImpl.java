package com.dsp.cga.assurance.extensionrequest.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dsp.cga.assurance.extensionrequest.dto.ExtensionRequestDTO;
import com.dsp.cga.assurance.extensionrequest.entity.ExtensionRequest;
import com.dsp.cga.assurance.extensionrequest.repository.ExtensionRequestRepository;
//import com.dsp.cga.assurance.extensionrequest.repository.ExtensionRequestRepository;
import com.dsp.cga.assurance.extensionrequest.service.ExtensionRequestService;
import com.dsp.cga.assurance.implementationrequest.dto.ImplementationRequestDTO;
import com.dsp.cga.assurance.implementationrequest.entity.ImplementationRequest;
import com.dsp.cga.mapper.ExtensionRequestMapper;
import com.dsp.cga.mapper.ImplementationRequestMapper;
import com.dsp.cga.response.GenericResponse;

@Service
public class ExtensionRequestServiceImpl implements ExtensionRequestService {

	private static final Logger log = LoggerFactory.getLogger(ExtensionRequestServiceImpl.class);

	@Autowired
	private ExtensionRequestRepository extenionRequestRepository;

	@Override
	public ExtensionRequestDTO createExtensionRequest(ExtensionRequestDTO extensionRequestDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ExtensionRequestDTO> getExtensionRequestByAssuranceId(Long id) {
		// Fetch the list of ExtensionRequest entities by Assurance ID
		Optional<ExtensionRequest> extensionRequests = extenionRequestRepository.findByAssuranceId(id);

		// If no records are found, log the message and return an empty list
		if (extensionRequests.isEmpty()) {
			log.debug("No ExtensionRequest found for Assurance ID: {}", id);
			return Collections.emptyList(); // Return an empty list
		}

		// Map the list of entities to a list of DTOs
		List<ExtensionRequestDTO> dtos = extensionRequests.stream().map(ExtensionRequestMapper::toDTO)
				.collect(Collectors.toList());

		log.debug("Returning ExtensionRequestDTOs: {}", dtos);
		return dtos;
	}

	@Override
	public GenericResponse<ExtensionRequestDTO> createOrUpdateExtensionRequest(
	        ExtensionRequestDTO extensionRequestDTO) {

	    if (extensionRequestDTO == null || extensionRequestDTO.getAssuranceId() == null) {
	        throw new IllegalArgumentException("Invalid input: AssuranceId cannot be null.");
	    }

	    // Check if an ExtensionRequest exists with the given assuranceId
	    Optional<ExtensionRequest> existingRequestOpt = extenionRequestRepository
	            .findByAssuranceId(extensionRequestDTO.getAssuranceId());

	    ExtensionRequest savedRequest;

	    if (existingRequestOpt.isPresent()) {
	        // Fetch the existing entity
	        ExtensionRequest existingRequest = existingRequestOpt.get();

	        // Update fields of the existing entity based on the DTO
	        ExtensionRequestMapper.updateEntityFromDTO(extensionRequestDTO, existingRequest);

	        // Save the updated entity
	        savedRequest = extenionRequestRepository.save(existingRequest);

	    } else {
	        // Create a new entity from the DTO
	        ExtensionRequest newRequest = ExtensionRequestMapper.toEntity(extensionRequestDTO);

	        // Save the new entity
	        savedRequest = extenionRequestRepository.save(newRequest);
	    }

	    // Convert the saved entity to a DTO
	    ExtensionRequestDTO savedDTO = ExtensionRequestMapper.toDTO(savedRequest);

	    // Return the response wrapped in GenericResponse
	    return new GenericResponse<>(HttpStatus.CREATED.value(), true, 
	            "ExtensionRequest created or updated successfully", savedDTO);
	}


}
