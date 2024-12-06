package com.dsp.cga.assurance.droppingrequest.service.impl;

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

import com.dsp.cga.assurance.droppingrequest.dto.DroppingRequestDTO;
import com.dsp.cga.assurance.droppingrequest.entity.DroppingRequest;
import com.dsp.cga.assurance.droppingrequest.repository.DroppingRequestRepositiry;
import com.dsp.cga.assurance.droppingrequest.service.DroppingRequestService;
import com.dsp.cga.assurance.dto.AcceptedDTO;
import com.dsp.cga.assurance.dto.DroppingTypeDTO;
import com.dsp.cga.assurance.dto.StatusDTO;
import com.dsp.cga.assurance.entity.Assurance;
import com.dsp.cga.assurance.implementationrequest.service.impl.ImplemantationRequestServiceImpl;
import com.dsp.cga.assurance.repository.AssuranceRepository;
import com.dsp.cga.mapper.DroppingRequestMapper;
import com.dsp.cga.response.GenericResponse;

@Service
public class DroppingRequestServiceImpl implements DroppingRequestService {

	private static final Logger log = LoggerFactory.getLogger(ImplemantationRequestServiceImpl.class);

	@Value("${master.codeServiceUrl}")
	private String codeServiceUrl;

	@Autowired
	private DroppingRequestRepositiry droppingRequestRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private AssuranceRepository assuranceRepository;

	@Override
	public DroppingRequestDTO getDroppingRequestByAssuranceId(Long id) {
		DroppingRequest Request = droppingRequestRepository.findByAssuranceId(id).orElse(null);

		// If no record is found, return a null DTO and log the message
		if (Request == null) {
			log.debug("No ImplementationRequest found for Assurance ID: {}", id);
			return null; // Return null to handle it at the controller level with custom message
		}

		// Log and map the entity to DTO if found
		log.debug("Fetched ImplementationRequest: {}", Request);

		Assurance assurance = assuranceRepository.findById(id).orElse(null);
		String assuranceNo = (assurance != null) ? assurance.getAssuranceNo() : null;
		Long lokshabaNo = (assurance != null) ? assurance.getLokSabhaNo() : null;
		

		DroppingRequestDTO dto = DroppingRequestMapper.toDTO(Request);
		dto.setAssuranceNo(assuranceNo);

		// Populate additional data if available
		// dto.setStatusDTO(fetchStatusData(Request.getDropStatusId()));
		// System.out.println("$$$$$$$$$$$" + Request.getDropStatusId());
		if (Request.getDroppingType() != null) {
		    dto.setDroppingTypeDTO(fetchDroppingTypeData(Request.getDroppingType()));
		}

		if (Request.getAcceptedId() != null) {
			dto.setAcceptedDTO(fetchAcceptdData(Request.getAcceptedId()));
		}

		log.debug("Returning ImplementationRequestDTO: {}", dto);
		return dto;
	}

	private StatusDTO fetchStatusData(Long dropStatusId) {
		String statusServiceUrl = codeServiceUrl + dropStatusId;
		log.debug("Calling master service URL for Status: {}", statusServiceUrl);

		ResponseEntity<GenericResponse<StatusDTO>> response = restTemplate.exchange(statusServiceUrl, HttpMethod.GET,
				null, new ParameterizedTypeReference<GenericResponse<StatusDTO>>() {
				});
		System.out.println(response);

		if (!response.getStatusCode().is2xxSuccessful()) {
			log.error("Failed to fetch data from master service for Status: {} - {}", response.getStatusCode(),
					response.getBody());
			throw new RuntimeException(
					"Failed to fetch data from master service for Status. Status: " + response.getStatusCode());
		}

		StatusDTO statusDTO = response.getBody().getData();
		log.debug("Fetched StatusDTO: {}", statusDTO);

		return statusDTO;
	}

	private DroppingTypeDTO fetchDroppingTypeData(Long droppingType) {
	    String droppingTypeServiceUrl = codeServiceUrl + droppingType;
	    log.debug("Calling master service URL for Dropping Type: {}", droppingTypeServiceUrl);

	    ResponseEntity<GenericResponse<DroppingTypeDTO>> response = restTemplate.exchange(
	            droppingTypeServiceUrl, 
	            HttpMethod.GET,
	            null, 
	            new ParameterizedTypeReference<GenericResponse<DroppingTypeDTO>>() {}
	    );

	    if (!response.getStatusCode().is2xxSuccessful()) {
	        log.error("Failed to fetch data from master service for Dropping Type: {} - {}", 
	                  response.getStatusCode(), response.getBody());
	        throw new RuntimeException(
	                "Failed to fetch data from master service for Dropping Type. Status: " + response.getStatusCode());
	    }

	    DroppingTypeDTO droppingTypeDTO = response.getBody().getData();
	    log.debug("Fetched DroppingTypeDTO: {}", droppingTypeDTO);

	    return droppingTypeDTO;
	}

	private AcceptedDTO fetchAcceptdData(Long acceptedId) {
		String statusServiceUrl = codeServiceUrl + acceptedId;
		log.debug("Calling master service URL for Status: {}", statusServiceUrl);

		ResponseEntity<GenericResponse<AcceptedDTO>> response = restTemplate.exchange(statusServiceUrl, HttpMethod.GET,
				null, new ParameterizedTypeReference<GenericResponse<AcceptedDTO>>() {
				});
		System.out.println(response);

		if (!response.getStatusCode().is2xxSuccessful()) {
			log.error("Failed to fetch data from master service for Status: {} - {}", response.getStatusCode(),
					response.getBody());
			throw new RuntimeException(
					"Failed to fetch data from master service for Status. Status: " + response.getStatusCode());
		}

		AcceptedDTO acceptedDTO = response.getBody().getData();
		log.debug("Fetched StatusDTO: {}", acceptedDTO);

		return acceptedDTO;
	}

	@Override
	public GenericResponse<DroppingRequestDTO> createOrUpdateDroppingRequest(DroppingRequestDTO droppingRequestDTO) {
		Optional<DroppingRequest> existingRequestOpt = droppingRequestRepository
				.findByAssuranceId(droppingRequestDTO.getAssuranceId());

		DroppingRequest savedRequest;

		if (existingRequestOpt.isPresent()) {
			DroppingRequest existingRequest = existingRequestOpt.get();
			DroppingRequestMapper.updateEntityFromDTO(droppingRequestDTO, existingRequest);
			savedRequest = droppingRequestRepository.save(existingRequest);
		} else {
			DroppingRequest newRequest = DroppingRequestMapper.toEntity(droppingRequestDTO);
			savedRequest = droppingRequestRepository.save(newRequest);
		}

		DroppingRequestDTO savedDTO = DroppingRequestMapper.toDTO(savedRequest);
		return new GenericResponse<>(HttpStatus.CREATED.value(), true,
				"DroppingRequest created or updated successfully", savedDTO);
	}
}
