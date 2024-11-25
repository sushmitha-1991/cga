package com.dsp.cga.assurance.implementationrequest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dsp.cga.assurance.implementationrequest.dto.ImplementationRequestDTO;
import com.dsp.cga.assurance.implementationrequest.service.ImplementationRequestService;
import com.dsp.cga.response.GenericResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/cga/implementationRequest")
public class ImplementationRequestController {
	
	@Autowired
	private ImplementationRequestService implementationRequestService;
	
	@PostMapping("/create")
	 @Operation(summary = "Create a new Assurance")
		@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Assurance created successfully"),
				@ApiResponse(responseCode = "400", description = "Invalid input data"),
				@ApiResponse(responseCode = "500", description = "Internal server error") })
	 public ResponseEntity<GenericResponse<ImplementationRequestDTO>> createImplementationRequest(@RequestBody ImplementationRequestDTO implementationRequestDTO) {
		ImplementationRequestDTO createdImplementationRequest = implementationRequestService.createImplementationRequest(implementationRequestDTO);
	        GenericResponse<ImplementationRequestDTO> response = new GenericResponse<>(HttpStatus.CREATED.value(), true,
	                "Assurance item created successfully", createdImplementationRequest);
	        return ResponseEntity.status(HttpStatus.CREATED).body(response);
	    }

}
