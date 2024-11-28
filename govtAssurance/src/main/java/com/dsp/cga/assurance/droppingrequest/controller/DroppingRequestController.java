package com.dsp.cga.assurance.droppingrequest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dsp.cga.assurance.droppingrequest.dto.DroppingRequestDTO;
import com.dsp.cga.assurance.droppingrequest.service.DroppingRequestService;
import com.dsp.cga.assurance.implementationrequest.dto.ImplementationRequestDTO;
import com.dsp.cga.response.GenericResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/cga/droppingRequest")
public class DroppingRequestController {
	
	@Autowired
	private DroppingRequestService droppingRequestService;
	
	@GetMapping("/{id}")
	@Operation(summary = "Get Assurance by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Assurance fetched successfully"),
			@ApiResponse(responseCode = "404", description = "Assurance not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<GenericResponse<DroppingRequestDTO>> getImplementationRequestByAssuranceId(@PathVariable Long id) {
		//try {
			DroppingRequestDTO request = droppingRequestService.getDroppingRequestByAssuranceId(id);

			if (request == null) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new GenericResponse<>(HttpStatus.OK.value(), false, "DroppingRequest not found", null));
			}

			GenericResponse<DroppingRequestDTO> response = new GenericResponse<>(HttpStatus.OK.value(), true,
					"DroppingRequest fetched successfully", request);

			return ResponseEntity.ok(response);


	}

	 @PostMapping("/create-or-update")
	    @Operation(summary = "Create or update a Dropping Request")
	    @ApiResponses(value = { 
	        @ApiResponse(responseCode = "200", description = "DroppingRequest created or updated successfully"),
	        @ApiResponse(responseCode = "400", description = "Invalid input data"),
	        @ApiResponse(responseCode = "500", description = "Internal server error") 
	    })
	    public ResponseEntity<GenericResponse<DroppingRequestDTO>> createOrUpdateDroppingRequest(
	            @RequestBody DroppingRequestDTO droppingRequestDTO) {

	        GenericResponse<DroppingRequestDTO> response = droppingRequestService.createOrUpdateDroppingRequest(droppingRequestDTO);
	        return ResponseEntity.status(HttpStatus.OK).body(response);
	    }
	 
}
