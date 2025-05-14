package com.dsp.cga.assurance.clubbedassurance.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dsp.cga.assurance.clubbedassurance.dto.AssuranceResponseDTO;
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
	
	 @PostMapping("/create")
	    @Operation(summary = "Create a new Clubbed Assurance")
	    @ApiResponses(value = {
	        @ApiResponse(responseCode = "201", description = "Clubbed Assurance created successfully"),
	        @ApiResponse(responseCode = "400", description = "Invalid input data"),
	        @ApiResponse(responseCode = "500", description = "Internal server error")
	    })
	    public ResponseEntity<GenericResponse<ClubbedAssuranceDTO>> createClubbedAssurance(
	            @RequestBody ClubbedAssuranceDTO dto) {

	        ClubbedAssuranceDTO created = clubbedAssuranceService.createClubbedAssurance(dto);
	        GenericResponse<ClubbedAssuranceDTO> response = new GenericResponse<>(
	            HttpStatus.CREATED.value(),
	            true,
	            "Clubbed Assurance created successfully",
	            created
	        );

	        return ResponseEntity.status(HttpStatus.CREATED).body(response);
	    }
	
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
	     //@ApiResponse(responseCode = "404", description = "No clubbed assurances found for the given assurance ID"),
	     @ApiResponse(responseCode = "500", description = "Internal server error")
	 })
	 public ResponseEntity<GenericResponse<List<ClubbedAssuranceDTO>>> getClubbedAssurancesByAssuranceId(
		        @PathVariable Long assuranceId) {
		    // Fetch clubbed assurances by assuranceId using the service layer
		    List<ClubbedAssuranceDTO> clubbedAssurances = clubbedAssuranceService.getClubbedAssurancesByAssuranceId(assuranceId);

		    // If no clubbed assurances are found, return a response with an appropriate message
		    if (clubbedAssurances.isEmpty()) {
		        return ResponseEntity.status(HttpStatus.OK)
		                .body(new GenericResponse<>(HttpStatus.OK.value(), false, "No Clubbed Assurance(s) found", null));
		    }

		    // If clubbed assurances are found, return a success response with the data
		    GenericResponse<List<ClubbedAssuranceDTO>> response = new GenericResponse<>(
		            HttpStatus.OK.value(), true, "Clubbed assurance(s) retrieved successfully", clubbedAssurances);

		    return ResponseEntity.ok(response);
		}

	 @GetMapping("/clubbedAssurance")
	 @Operation(summary = "Get Filtered Clubbed Assurances")
	 @ApiResponses(value = {
	     @ApiResponse(responseCode = "200", description = "Assurances retrieved successfully"),
	     @ApiResponse(responseCode = "500", description = "Internal server error")
	 })
	 public ResponseEntity<GenericResponse<List<AssuranceResponseDTO>>> getAssurances(
	         @RequestParam(required = false) Long sessionNo,
	         @RequestParam(required = false) LocalDate assuranceDate,
	         @RequestParam(required = false) Long statusId,
	         @RequestParam(required = false) Integer ministryCode) {

	     List<AssuranceResponseDTO> response = clubbedAssuranceService.getFilteredClubbedAssurances(
	             sessionNo, assuranceDate, statusId, ministryCode
	     );

	     if (response.isEmpty()) {
	         return ResponseEntity.ok(
	                 new GenericResponse<>(HttpStatus.OK.value(), false, "No assurances found", null)
	         );
	     }

	     GenericResponse<List<AssuranceResponseDTO>> genericResponse = new GenericResponse<>(
	             HttpStatus.OK.value(), true, "Assurances retrieved successfully", response
	     );

	     return ResponseEntity.ok(genericResponse);
	 }

	 @GetMapping("/clubbedAssurance/{id}")
	 @Operation(summary = "Get Clubbed Assurance by ID")
	 @ApiResponses(value = {
	     @ApiResponse(responseCode = "200", description = "Clubbed assurance retrieved successfully"),
	     @ApiResponse(responseCode = "404", description = "Clubbed assurance not found"),
	     @ApiResponse(responseCode = "500", description = "Internal server error")
	 })
	 public ResponseEntity<GenericResponse<AssuranceResponseDTO>> getClubbedAssuranceById(@PathVariable Long id) {
	     AssuranceResponseDTO dto = clubbedAssuranceService.getClubbedAssuranceById(id);

	     if (dto == null) {
	         return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                 .body(new GenericResponse<>(HttpStatus.NOT_FOUND.value(), false, "Clubbed assurance not found", null));
	     }

	     return ResponseEntity.ok(
	             new GenericResponse<>(HttpStatus.OK.value(), true, "Clubbed assurance retrieved successfully", dto)
	     );
	 }


}
