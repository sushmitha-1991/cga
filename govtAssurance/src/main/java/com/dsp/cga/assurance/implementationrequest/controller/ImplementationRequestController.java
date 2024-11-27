package com.dsp.cga.assurance.implementationrequest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dsp.cga.assurance.dto.AssuranceDTO;
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

	 @PostMapping("/create-or-update")
	    @Operation(summary = "Create or update an Assurance")
	    @ApiResponses(value = { 
	        @ApiResponse(responseCode = "200", description = "Assurance created or updated successfully"),
	        @ApiResponse(responseCode = "400", description = "Invalid input data"),
	        @ApiResponse(responseCode = "500", description = "Internal server error") 
	    })
	    public ResponseEntity<GenericResponse<ImplementationRequestDTO>> createOrUpdateImplementationRequest(
	            @RequestBody ImplementationRequestDTO implementationRequestDTO) {

	        // Call service method to either create or update the implementation request
	        GenericResponse<ImplementationRequestDTO> response = implementationRequestService.createOrUpdateImplementationRequest(implementationRequestDTO);

	        // Return the response wrapped in ResponseEntity
	        return ResponseEntity.status(HttpStatus.OK).body(response);
	    }
	 
	 @GetMapping("/{id}")
		@Operation(summary = "Get Assurance by ID")
		@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Assurance fetched successfully"),
				@ApiResponse(responseCode = "404", description = "Assurance not found"),
				@ApiResponse(responseCode = "500", description = "Internal server error") })
		public ResponseEntity<GenericResponse<ImplementationRequestDTO>> getImplementationRequestByAssuranceId(@PathVariable Long id) {
			//try {
				ImplementationRequestDTO request = implementationRequestService.getImplementationRequestByAssuranceId(id);

				if (request == null) {
					return ResponseEntity.status(HttpStatus.OK)
							.body(new GenericResponse<>(HttpStatus.OK.value(), false, "ImplementationRequest not found", null));
				}

				GenericResponse<ImplementationRequestDTO> response = new GenericResponse<>(HttpStatus.OK.value(), true,
						"ImplemetationRequest fetched successfully", request);

				return ResponseEntity.ok(response);

//			} catch (Exception e) {
//				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericResponse<>(
//						HttpStatus.INTERNAL_SERVER_ERROR.value(), false, "Error fetching assurance", null));
//			}
		}
}
