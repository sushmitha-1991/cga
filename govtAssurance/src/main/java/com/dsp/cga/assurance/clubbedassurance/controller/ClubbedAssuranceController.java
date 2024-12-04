package com.dsp.cga.assurance.clubbedassurance.controller;

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

import com.dsp.cga.assurance.clubbedassurance.dto.ClubbedAssuranceDTO;
import com.dsp.cga.assurance.clubbedassurance.service.ClubbedAssuranceService;
import com.dsp.cga.response.GenericResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/cga/clubbedAssurance")
public class ClubbedAssuranceController {
	
	@Autowired
	private ClubbedAssuranceService clubbedAssuranceService;
	
	 @PostMapping("/create-or-update")
	 @Operation(summary = "Create a new Assurance")
		@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "ClubbedAssurance created successfully"),
				@ApiResponse(responseCode = "400", description = "Invalid input data"),
				@ApiResponse(responseCode = "500", description = "Internal server error") })
	    
	 public ResponseEntity<GenericResponse<ClubbedAssuranceDTO>> createOrUpdateClubbedAssurance(
		        @RequestBody ClubbedAssuranceDTO clubbedAssuranceDTO) {
		    // Log the received DTO
		    System.out.println("Received request body: " + clubbedAssuranceDTO);

		    GenericResponse<ClubbedAssuranceDTO> response = clubbedAssuranceService.createOrUpdateClubbedAssurance(clubbedAssuranceDTO);
		    return ResponseEntity.status(HttpStatus.OK).body(response);
		}
	 
//	 @GetMapping("/{id}")
//	 @Operation(summary = "Get ClubbedAssurance by ID")
//		@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "ClubbedAssurance fetched successfully"),
//				@ApiResponse(responseCode = "404", description = "Assurance not found"),
//				@ApiResponse(responseCode = "500", description = "Internal server error") })
//	 public ResponseEntity<GenericResponse<ClubbedAssuranceDTO>> getClubbedAssuranceByAssuranceId(@PathVariable Long id) {
//	     ClubbedAssuranceDTO clubbedAssurance = clubbedAssuranceService.getClubbedAssuranceByAssuranceId(id);
//
//	     if (clubbedAssurance == null) {
//	         return ResponseEntity.status(HttpStatus.OK)
//	                 .body(new GenericResponse<>(HttpStatus.OK.value(), false, "ClubbedAssurance not found", null));
//	     }
//
//	     GenericResponse<ClubbedAssuranceDTO> response = new GenericResponse<>(
//	             HttpStatus.OK.value(), 
//	             true, 
//	             "ClubbedAssurance fetched successfully", 
//	             clubbedAssurance
//	     );
//
//	     return ResponseEntity.ok(response);
//	 }

	 @GetMapping("/get-by-assurance-id/{assuranceId}")
	 @Operation(summary = "Get Clubbed Assurances by Assurance ID")
	 @ApiResponses(value = {
	     @ApiResponse(responseCode = "200", description = "Clubbed assurances retrieved successfully"),
	     @ApiResponse(responseCode = "404", description = "No clubbed assurances found for the given assurance ID"),
	     @ApiResponse(responseCode = "500", description = "Internal server error")
	 })
	 public ResponseEntity<GenericResponse<List<ClubbedAssuranceDTO>>> getClubbedAssurancesByAssuranceId(
	         @PathVariable Long assuranceId) {
	     List<ClubbedAssuranceDTO> clubbedAssurances = clubbedAssuranceService.getClubbedAssurancesByAssuranceId(assuranceId);

	     if (clubbedAssurances.isEmpty()) {
	         return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                 .body(new GenericResponse<>(
	                         HttpStatus.NOT_FOUND.value(),
	                         false,
	                         "No clubbed assurances found for assurance ID " + assuranceId,
	                         null
	                 ));
	     }

	     return ResponseEntity.status(HttpStatus.OK)
	             .body(new GenericResponse<>(
	                     HttpStatus.OK.value(),
	                     true,
	                     "Clubbed assurances retrieved successfully",
	                     clubbedAssurances
	             ));
	 }

}
