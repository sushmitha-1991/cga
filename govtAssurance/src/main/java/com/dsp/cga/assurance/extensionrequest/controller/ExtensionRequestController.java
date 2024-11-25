package com.dsp.cga.assurance.extensionrequest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dsp.cga.assurance.extensionrequest.dto.ExtensionRequestDTO;
import com.dsp.cga.assurance.extensionrequest.service.ExtensionRequestService;
import com.dsp.cga.response.GenericResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/cga/extensionRequest")
public class ExtensionRequestController {
	
	@Autowired
	private ExtensionRequestService extensionRequestService;
	
	@PostMapping("/create")
	 @Operation(summary = "Create a new Assurance")
		@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Assurance created successfully"),
				@ApiResponse(responseCode = "400", description = "Invalid input data"),
				@ApiResponse(responseCode = "500", description = "Internal server error") })
	 public ResponseEntity<GenericResponse<ExtensionRequestDTO>> createExtensionRequest(@RequestBody ExtensionRequestDTO extensionRequestDTO) {
		ExtensionRequestDTO createdExtensionRequest = extensionRequestService.createExtensionRequest(extensionRequestDTO);
	        GenericResponse<ExtensionRequestDTO> response = new GenericResponse<>(HttpStatus.CREATED.value(), true,
	                "Assurance item created successfully", createdExtensionRequest);
	        return ResponseEntity.status(HttpStatus.CREATED).body(response);
	    }

}
