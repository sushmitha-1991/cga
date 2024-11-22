package com.dsp.cga.assurance.implementationrequest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsp.cga.assurance.implementationrequest.dto.ImplementationRequestDTO;
import com.dsp.cga.assurance.implementationrequest.entity.ImplementationRequest;
import com.dsp.cga.assurance.implementationrequest.repository.ImplementationRequestRepository;
import com.dsp.cga.assurance.implementationrequest.service.ImplementationRequestService;
import com.dsp.cga.mapper.ImplementationRequestMapper;

@Service
public class ImplemantationRequestServiceImpl implements ImplementationRequestService {

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
}
