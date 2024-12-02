package com.dsp.cga.assurance.extensionrequest.service.impl;

import java.util.ArrayList;
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
	    List<ExtensionRequest> extensionRequests = extenionRequestRepository.findByAssuranceId(id);

	    // If no records are found, log the message and return an empty list
	    if (extensionRequests.isEmpty()) {
	        log.debug("No ExtensionRequest found for Assurance ID: {}", id);
	        return Collections.emptyList(); // Return an empty list
	    }

	    // Map the list of entities to a list of DTOs
	    List<ExtensionRequestDTO> dtos = extensionRequests.stream()
	            .map(ExtensionRequestMapper::toDTO)
	            .collect(Collectors.toList());

	    log.debug("Returning ExtensionRequestDTOs: {}", dtos);
	    return dtos;
	}


	

	@Override
	public GenericResponse<List<ExtensionRequestDTO>> createOrUpdateExtensionRequests(
	        List<ExtensionRequestDTO> extensionRequestDTOList) {

	    List<ExtensionRequestDTO> savedDTOs = new ArrayList<>();

	    for (ExtensionRequestDTO dto : extensionRequestDTOList) {
	        if (dto == null || dto.getAssuranceId() == null || dto.getExtensionNo() == null) {
	            throw new IllegalArgumentException("Invalid input: AssuranceId and ExtensionNo cannot be null.");
	        }

	        ExtensionRequest savedRequest;

	        if (dto.getId() != null) {
	            // If the ID is provided, try to find the existing record by ID
	            Optional<ExtensionRequest> existingRequestOpt = extenionRequestRepository.findById(dto.getId());

	            if (existingRequestOpt.isPresent()) {
	                // Update the existing entity
	                ExtensionRequest existingRequest = existingRequestOpt.get();
	                ExtensionRequestMapper.updateEntityFromDTO(dto, existingRequest);

	                // Save the updated entity
	                savedRequest = extenionRequestRepository.save(existingRequest);
	            } else {
	                throw new IllegalArgumentException("No ExtensionRequest found with the provided ID: " + dto.getId());
	            }

	        } else {
	            // If no ID is provided, check if an entry exists for assuranceId + extensionNo
	            Optional<ExtensionRequest> existingRequestOpt = extenionRequestRepository
	                    .findByAssuranceIdAndExtensionNo(dto.getAssuranceId(), dto.getExtensionNo());

	            if (existingRequestOpt.isPresent()) {
	                // Update the existing entity
	                ExtensionRequest existingRequest = existingRequestOpt.get();
	                ExtensionRequestMapper.updateEntityFromDTO(dto, existingRequest);

	                // Save the updated entity
	                savedRequest = extenionRequestRepository.save(existingRequest);
	            } else {
	                // Create a new entity
	                ExtensionRequest newRequest = ExtensionRequestMapper.toEntity(dto);

	                // Save the new entity
	                savedRequest = extenionRequestRepository.save(newRequest);
	            }
	        }

	        // Convert the saved entity to DTO and add to the response list
	        ExtensionRequestDTO savedDTO = ExtensionRequestMapper.toDTO(savedRequest);
	        savedDTOs.add(savedDTO);
	    }

	    // Return the response wrapped in GenericResponse
	    return new GenericResponse<>(
	            HttpStatus.CREATED.value(),
	            true,
	            "ExtensionRequests created or updated successfully",
	            savedDTOs
	    );
	}


}
