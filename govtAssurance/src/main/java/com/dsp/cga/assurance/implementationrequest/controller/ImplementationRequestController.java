package com.dsp.cga.assurance.implementationrequest.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dsp.cga.assurance.dto.AssuranceDTO;
import com.dsp.cga.assurance.implementationrequest.dto.AssuranceImplementationDTO;
import com.dsp.cga.assurance.implementationrequest.dto.AssuranceWithImplementationRequestDTO;
import com.dsp.cga.assurance.implementationrequest.dto.ImplementationRequestDTO;
import com.dsp.cga.assurance.implementationrequest.service.ImplementationRequestService;
import com.dsp.cga.assurance.service.AssuranceService;
import com.dsp.cga.response.GenericResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/cga/implementationRequest")
public class ImplementationRequestController {
	
	@Autowired
	private ImplementationRequestService implementationRequestService;
	
	@Autowired
	private AssuranceService assuranceService;
	
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
	 
	 @GetMapping("/implementation-details")
	 @Operation(summary = "Get Implementation Details by filters")
	 @ApiResponses(value = {
	     @ApiResponse(responseCode = "200", description = "Implementation details fetched successfully"),
	     @ApiResponse(responseCode = "404", description = "No implementation details found"),
	     @ApiResponse(responseCode = "500", description = "Internal server error")
	 })
	 public ResponseEntity<GenericResponse<List<AssuranceImplementationDTO>>> getAssuranceImplementations(
	         @RequestParam(required = false) Long sessionNo,
	         @RequestParam(required = false) LocalDate layingDate,
	         @RequestParam(required = false) Long implementationTypeId,
	         @RequestParam(required = false) Long statusId,
	         @RequestParam(required = false) Integer ministryCode
	 ) {
	     List<AssuranceImplementationDTO> implementations = implementationRequestService.getAssuranceImplementations(
	             sessionNo, layingDate, implementationTypeId, statusId, ministryCode);

	     if (implementations.isEmpty()) {
	         return ResponseEntity.status(HttpStatus.OK)
	                 .body(new GenericResponse<>(HttpStatus.OK.value(), false, "No implementation details found", null));
	     }

	     GenericResponse<List<AssuranceImplementationDTO>> response = new GenericResponse<>(HttpStatus.OK.value(),
	             true, "Implementation details fetched successfully", implementations);

	     return ResponseEntity.ok(response);
	 }

	 @GetMapping("/implementation-details/{id}")
	 @Operation(summary = "Get Implementation Detail by ID")
	 @ApiResponses(value = {
	     @ApiResponse(responseCode = "200", description = "Implementation detail fetched successfully"),
	     @ApiResponse(responseCode = "404", description = "Implementation detail not found"),
	     @ApiResponse(responseCode = "500", description = "Internal server error")
	 })
	 public ResponseEntity<GenericResponse<AssuranceImplementationDTO>> getImplementationDetailById(
	         @PathVariable Long id) {

	     AssuranceImplementationDTO dto = implementationRequestService.getImplementationDetailById(id);

	     if (dto == null) {
	         return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                 .body(new GenericResponse<>(HttpStatus.NOT_FOUND.value(), false, "Implementation detail not found", null));
	     }

	     return ResponseEntity.ok(new GenericResponse<>(HttpStatus.OK.value(), true,
	             "Implementation detail fetched successfully", dto));
	 }

	 @PutMapping("/implementation-details/{id}")
	 @Operation(summary = "Update Implementation Detail by ID")
	 @ApiResponses(value = {
	     @ApiResponse(responseCode = "200", description = "Implementation detail updated successfully"),
	     @ApiResponse(responseCode = "404", description = "Implementation detail not found"),
	     @ApiResponse(responseCode = "500", description = "Internal server error")
	 })
	 public ResponseEntity<GenericResponse<AssuranceImplementationDTO>> updateImplementationRequestById(
	         @PathVariable Long id,
	         @RequestBody ImplementationRequestDTO dto) {

	     AssuranceImplementationDTO updatedDto = implementationRequestService.updateImplementationRequest(id, dto);

	     if (updatedDto == null) {
	         return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                 .body(new GenericResponse<>(HttpStatus.NOT_FOUND.value(), false, "Implementation detail not found", null));
	     }

	     return ResponseEntity.ok(new GenericResponse<>(
	             HttpStatus.OK.value(), true, "Implementation detail updated successfully", updatedDto));
	 }
	 
	 @PutMapping("/updateMinistryStatus/{id}")
	 @Operation(summary = "Update Implementation Detail by ID")
	 @ApiResponses(value = {
	     @ApiResponse(responseCode = "200", description = "Implementation detail updated successfully"),
	     @ApiResponse(responseCode = "404", description = "Implementation detail not found"),
	     @ApiResponse(responseCode = "500", description = "Internal server error")
	 })
	 public ResponseEntity<GenericResponse<AssuranceImplementationDTO>> updateImplementationRequestforMinistryStatusById(
	         @PathVariable Long id,
	         @RequestBody ImplementationRequestDTO dto) {

	     AssuranceImplementationDTO updatedDto = implementationRequestService.updateImplementationRequestforMinistryStatusById(id, dto);
	     System.out.println("@@@@@@@@@@@"+ dto);

	     if (updatedDto == null) {
	         return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                 .body(new GenericResponse<>(HttpStatus.NOT_FOUND.value(), false, "Implementation detail not found", null));
	     }

	     return ResponseEntity.ok(new GenericResponse<>(
	             HttpStatus.OK.value(), true, "Implementation detail updated successfully", updatedDto));
	 }

	 
	 @GetMapping("/count")
	    public ResponseEntity<Map<String, Object>> getRequestCounts(
	            @RequestParam Integer ministryCode,
	            @RequestParam Long houseId) {
	        return ResponseEntity.ok(implementationRequestService.getRequestCounts(ministryCode, houseId));
	    }
	 
//	 @GetMapping("/getAll")
//	 @Operation(summary = "Get all Implementation Details")
//	 @ApiResponses(value = {
//	     @ApiResponse(responseCode = "200", description = "Implementation details fetched successfully"),
//	     @ApiResponse(responseCode = "500", description = "Internal server error")
//	 })
//	 public ResponseEntity<GenericResponse<List<AssuranceImplementationDTO>>> getAllImplementationDetails() {
//	     List<AssuranceImplementationDTO> list = implementationRequestService.getAllImplementationDetails();
//
//	     return ResponseEntity.ok(
//	         new GenericResponse<>(HttpStatus.OK.value(), true, "Implementation details fetched successfully", list)
//	     );
//	 }
	 
	 @GetMapping("/getAll")
	 @Operation(summary = "Get all Implementation Details")
	 @ApiResponses(value = {
	     @ApiResponse(responseCode = "200", description = "Implementation details fetched successfully"),
	     @ApiResponse(responseCode = "500", description = "Internal server error")
	 })
	 public ResponseEntity<GenericResponse<List<AssuranceImplementationDTO>>> getAllImplementationDetails(
	         @RequestParam(required = false) String houseAbbr) {

	     List<AssuranceImplementationDTO> list = implementationRequestService.getAllImplementationDetails(houseAbbr);

	     return ResponseEntity.ok(
	         new GenericResponse<>(HttpStatus.OK.value(), true, "Implementation details fetched successfully", list)
	     );
	 }




}
