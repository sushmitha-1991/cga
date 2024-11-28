package com.dsp.cga.assurance.extensionrequest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dsp.cga.assurance.extensionrequest.dto.ExtensionRequestDTO;
import com.dsp.cga.assurance.extensionrequest.service.ExtensionRequestService;
import com.dsp.cga.assurance.implementationrequest.dto.ImplementationRequestDTO;
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

	@GetMapping("/{id}")
	@Operation(summary = "Get ExtensionRequest(s) by Assurance ID")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "ExtensionRequest(s) fetched successfully"),
	        @ApiResponse(responseCode = "404", description = "ExtensionRequest(s) not found"),
	        @ApiResponse(responseCode = "500", description = "Internal server error")
	})
	public ResponseEntity<GenericResponse<List<ExtensionRequestDTO>>> getExtensionRequestByAssuranceId(@PathVariable Long id) {
	    List<ExtensionRequestDTO> requests = extensionRequestService.getExtensionRequestByAssuranceId(id);

	    if (requests.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.OK)
	                .body(new GenericResponse<>(HttpStatus.OK.value(), false, "No ExtensionRequest(s) found", null));
	    }

	    GenericResponse<List<ExtensionRequestDTO>> response = new GenericResponse<>(HttpStatus.OK.value(), true,
	            "ExtensionRequest(s) fetched successfully", requests);

	    return ResponseEntity.ok(response);
	}

	@PostMapping("/create-or-update")
    @Operation(summary = "Create or update an Assurance")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "ExtensionRequest created or updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error") 
    })
    public ResponseEntity<GenericResponse<ExtensionRequestDTO>> createOrUpdateImplementationRequest(
            @RequestBody ExtensionRequestDTO extensionRequestDTO) {

        // Call service method to either create or update the implementation request
        GenericResponse<ExtensionRequestDTO> response = extensionRequestService.createOrUpdateExtensionRequest(extensionRequestDTO);

        // Return the response wrapped in ResponseEntity
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
