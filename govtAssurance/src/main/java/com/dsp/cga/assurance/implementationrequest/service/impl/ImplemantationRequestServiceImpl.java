package com.dsp.cga.assurance.implementationrequest.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dsp.cga.assurance.dto.AssuranceDTO;
import com.dsp.cga.assurance.dto.CommitteeDecisionDTO;
import com.dsp.cga.assurance.dto.MemberDTO;
import com.dsp.cga.assurance.dto.StatusDTO;
import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.assurance.implementationrequest.dto.ImplementationRequestDTO;
import com.dsp.cga.assurance.implementationrequest.entity.ImplementationRequest;
import com.dsp.cga.assurance.implementationrequest.repository.ImplementationRequestRepository;
import com.dsp.cga.assurance.implementationrequest.service.ImplementationRequestService;
import com.dsp.cga.assurance.service.impl.AssuranceServiceImpl;
import com.dsp.cga.exception.ResourceNotFoundException;
import com.dsp.cga.mapper.ImplementationRequestMapper;
import com.dsp.cga.response.GenericResponse;

@Service
public class ImplemantationRequestServiceImpl implements ImplementationRequestService {

	private static final Logger log = LoggerFactory.getLogger(ImplemantationRequestServiceImpl.class);

	@Value("${master.codeServiceUrl}")
	private String codeServiceUrl;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ImplementationRequestRepository implementationRequestRepository;

	@Override
	public ImplementationRequestDTO createImplementationRequest(ImplementationRequestDTO implementationRequestDTO) {
		ImplementationRequest implementationRequest = ImplementationRequestMapper.toEntity(implementationRequestDTO);

		// Save Assurance entity
		ImplementationRequest savedimplementationRequest = implementationRequestRepository.save(implementationRequest); // Save
																														// once,

		// Map entity back to DTO to return response
		ImplementationRequestDTO savedImplementationRequestDTO = ImplementationRequestMapper
				.toDTO(savedimplementationRequest);

		return savedImplementationRequestDTO;
	}

	@Override
	public GenericResponse<ImplementationRequestDTO> createOrUpdateImplementationRequest(
	        ImplementationRequestDTO implementationRequestDTO) {

	    Optional<ImplementationRequest> existingRequestOpt = implementationRequestRepository
	            .findByAssuranceId(implementationRequestDTO.getAssuranceId());

	    ImplementationRequest savedRequest;

	    if (existingRequestOpt.isPresent()) {
	        // Record exists, update it
	        ImplementationRequest existingRequest = existingRequestOpt.get();
	        ImplementationRequest updatedRequest = ImplementationRequestMapper.toEntity(implementationRequestDTO);
	        updatedRequest.setId(existingRequest.getId()); // Ensure the same ID is retained
	        savedRequest = implementationRequestRepository.save(updatedRequest);
	    } else {
	        // Record does not exist, create a new one
	        ImplementationRequest newRequest = ImplementationRequestMapper.toEntity(implementationRequestDTO);
	        savedRequest = implementationRequestRepository.save(newRequest);
	    }

	    // Convert the saved entity back to DTO
	    ImplementationRequestDTO savedDTO = ImplementationRequestMapper.toDTO(savedRequest);

	    // Return the response wrapped in GenericResponse
	    return new GenericResponse<>(HttpStatus.CREATED.value(), true, "Success", savedDTO);
	}


//	@Override
//	public ImplementationRequestDTO getImplementationRequestByAssuranceId(Long id) {
//	    ImplementationRequest implementationRequest = implementationRequestRepository.findByAssuranceId(id)
//	            .orElseThrow(() -> new ResourceNotFoundException("Assurance not found with ID: " + id));
//
//	    log.debug("Fetched Assurance: {}", implementationRequest);
//
//	    // Map Assurance entity to DTO
//	    ImplementationRequestDTO dto = ImplementationRequestMapper.toDTO(implementationRequest);
//
//	    // Populate additional data if available
//	    dto.setCommitteeDecisionDTO(fetchCommitteeDecisionData(implementationRequest.getCommitteeDecisionId()));
//
//	    log.debug("Returning AssuranceDTO: {}", dto);
//	    return dto;
//	}

	@Override
	public ImplementationRequestDTO getImplementationRequestByAssuranceId(Long id) {
	    // Attempt to find the ImplementationRequest by Assurance ID
	    ImplementationRequest implementationRequest = implementationRequestRepository.findByAssuranceId(id).orElse(null);

	    // If no record is found, return a null DTO and log the message
	    if (implementationRequest == null) {
	        log.debug("No ImplementationRequest found for Assurance ID: {}", id);
	        return null;  // Return null to handle it at the controller level with custom message
	    }

	    // Log and map the entity to DTO if found
	    log.debug("Fetched ImplementationRequest: {}", implementationRequest);
	    ImplementationRequestDTO dto = ImplementationRequestMapper.toDTO(implementationRequest);

	    // Populate additional data if available
	    dto.setCommitteeDecisionDTO(fetchCommitteeDecisionData(implementationRequest.getCommitteeDecisionId()));

	    log.debug("Returning ImplementationRequestDTO: {}", dto);
	    return dto;
	}

	


	
	private CommitteeDecisionDTO fetchCommitteeDecisionData(Long committeeDecisionId) {
	    String statusServiceUrl = codeServiceUrl + committeeDecisionId;
	    log.debug("Calling master service URL for Committee Decision: {}", statusServiceUrl);

	    try {
	        ResponseEntity<GenericResponse<CommitteeDecisionDTO>> response = restTemplate.exchange(
	                statusServiceUrl, HttpMethod.GET, null, 
	                new ParameterizedTypeReference<GenericResponse<CommitteeDecisionDTO>>() {}
	        );

	        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
	            log.warn("Failed to fetch data from master service for Committee Decision. Status: {} - {}",
	                    response.getStatusCode(), response.getBody());
	            return null;
	        }

	        CommitteeDecisionDTO committeeDecisionDTO = response.getBody().getData();
	        log.debug("Fetched CommitteeDecisionDTO: {}", committeeDecisionDTO);

	        return committeeDecisionDTO;
	    } catch (Exception e) {
	        log.error("Error occurred while fetching data from master service for Committee Decision ID: {}", committeeDecisionId, e);
	        return null;
	    }
	}


}
