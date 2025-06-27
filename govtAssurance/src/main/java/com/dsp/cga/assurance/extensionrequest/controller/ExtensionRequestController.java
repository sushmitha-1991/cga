package com.dsp.cga.assurance.extensionrequest.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import com.dsp.cga.assurance.dto.CommonRequestResponseDTO;
import com.dsp.cga.assurance.extensionrequest.dto.ExtensionRequestDTO;
import com.dsp.cga.assurance.extensionrequest.dto.ExtensionRequestResponseDTO;
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
	@Operation(summary = "Create or update one or more ExtensionRequests")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "ExtensionRequest(s) created or updated successfully"),
	    @ApiResponse(responseCode = "400", description = "Invalid input data"),
	    @ApiResponse(responseCode = "500", description = "Internal server error")
	})
	public ResponseEntity<GenericResponse<List<ExtensionRequestDTO>>> createOrUpdateExtensionRequests(
	        @RequestBody List<ExtensionRequestDTO> extensionRequestDTOList) {

	    if (extensionRequestDTOList == null || extensionRequestDTOList.isEmpty()) {
	        throw new IllegalArgumentException("Invalid input: List of ExtensionRequestDTO cannot be null or empty.");
	    }

	    // Call service method to process each request
	    GenericResponse<List<ExtensionRequestDTO>> response = 
	            extensionRequestService.createOrUpdateExtensionRequests(extensionRequestDTOList);

	    // Return the response wrapped in ResponseEntity
	    return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/extension-requests")
	@Operation(summary = "Get filtered list of ExtensionRequests")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "ExtensionRequest(s) fetched successfully"),
	    @ApiResponse(responseCode = "500", description = "Internal server error")
	})
	public ResponseEntity<GenericResponse<List<ExtensionRequestResponseDTO>>> getExtensionRequests(
	    @RequestParam(required = false) Long sessionNo,
	    @RequestParam(required = false) String statusForMinistry,
	    @RequestParam(required = false) Integer ministryCode,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate extRequestedTillDate,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate extGrantedTillDate,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ministryRefDate
	) {
	    List<ExtensionRequestResponseDTO> results = extensionRequestService.getFilteredExtensions(
	        sessionNo, statusForMinistry, extRequestedTillDate, extGrantedTillDate, ministryRefDate, ministryCode
	    );

	    if (results.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.OK)
	            .body(new GenericResponse<>(HttpStatus.OK.value(), false, "No ExtensionRequest(s) found", null));
	    }

	    GenericResponse<List<ExtensionRequestResponseDTO>> response = new GenericResponse<>(
	        HttpStatus.OK.value(), true, "ExtensionRequest(s) fetched successfully", results
	    );

	    return ResponseEntity.ok(response);
	}

	@GetMapping("/extension-requests/{id}")
	@Operation(summary = "Get ExtensionRequest by ID")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "ExtensionRequest fetched successfully"),
	    @ApiResponse(responseCode = "404", description = "ExtensionRequest not found"),
	    @ApiResponse(responseCode = "500", description = "Internal server error")
	})
	public ResponseEntity<GenericResponse<ExtensionRequestResponseDTO>> getExtensionRequestById(@PathVariable Long id) {
	    ExtensionRequestResponseDTO dto = extensionRequestService.getExtensionRequestById(id);

	    if (dto == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new GenericResponse<>(HttpStatus.NOT_FOUND.value(), false, "ExtensionRequest not found", null));
	    }

	    GenericResponse<ExtensionRequestResponseDTO> response = new GenericResponse<>(
	            HttpStatus.OK.value(), true, "ExtensionRequest fetched successfully", dto);

	    return ResponseEntity.ok(response);
	}

	@PutMapping("/extension-requests/{id}")
	@Operation(summary = "Update ExtensionRequest by ID")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "ExtensionRequest updated successfully"),
	    @ApiResponse(responseCode = "404", description = "ExtensionRequest not found"),
	    @ApiResponse(responseCode = "500", description = "Internal server error")
	})
	public ResponseEntity<GenericResponse<ExtensionRequestResponseDTO>> updateExtensionRequest(
	        @PathVariable Long id,
	        @RequestBody ExtensionRequestDTO requestDTO) {

	    ExtensionRequestResponseDTO dto = extensionRequestService.updateExtensionRequest(id, requestDTO);

	    if (dto == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new GenericResponse<>(HttpStatus.NOT_FOUND.value(), false, "ExtensionRequest not found", null));
	    }

	    GenericResponse<ExtensionRequestResponseDTO> response = new GenericResponse<>(
	            HttpStatus.OK.value(), true, "ExtensionRequest updated successfully", dto);

	    return ResponseEntity.ok(response);
	}

//	@GetMapping("getAllRequests")
//	@Operation(summary = "Get all requests")
//	@ApiResponses(value = {
//	    @ApiResponse(responseCode = "200", description = "Requests fetched successfully"),
//	    @ApiResponse(responseCode = "500", description = "Internal server error")
//	})
//	public ResponseEntity<GenericResponse<List<CommonRequestResponseDTO>>> getAllRequests() {
//	    List<CommonRequestResponseDTO> responseList = extensionRequestService.getAllRequests();
//	    GenericResponse<List<CommonRequestResponseDTO>> response = new GenericResponse<>(
//	            HttpStatus.OK.value(),
//	            true,
//	            "Requests fetched successfully",
//	            responseList
//	    );
//	    return ResponseEntity.ok(response);
//	}

	@GetMapping("getAllRequests")
	@Operation(summary = "Get all requests")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Requests fetched successfully"),
	    @ApiResponse(responseCode = "500", description = "Internal server error")
	})
	public ResponseEntity<GenericResponse<List<CommonRequestResponseDTO>>> getAllRequests(
		    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
		    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,		   
		    @RequestParam(required = false) Long ministryId,
			@RequestParam(required = false) Long memberId,
		    @RequestParam(required = false) String status) {
	
		List<CommonRequestResponseDTO> responseList = extensionRequestService.getAllRequests(
															fromDate, toDate, ministryId, memberId, status);
		
	    GenericResponse<List<CommonRequestResponseDTO>> response = new GenericResponse<>(
	            HttpStatus.OK.value(),
	            true,
	            "Requests fetched successfully",
	            responseList
	    );
	    return ResponseEntity.ok(response);
	}

	@GetMapping("getRequestById")
	@Operation(summary = "Get request by ID and type")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Request fetched successfully"),
	    @ApiResponse(responseCode = "404", description = "Request not found"),
	    @ApiResponse(responseCode = "500", description = "Internal server error")
	})
	public ResponseEntity<GenericResponse<CommonRequestResponseDTO>> getRequestById(
	        @RequestParam("id") Long id,
	        @RequestParam("type") String requestType) {

	    CommonRequestResponseDTO dto = extensionRequestService.getRequestById(id, requestType);

	    if (dto == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new GenericResponse<>(HttpStatus.NOT_FOUND.value(), false, "Request not found", null));
	    }

	    GenericResponse<CommonRequestResponseDTO> response = new GenericResponse<>(
	            HttpStatus.OK.value(),
	            true,
	            "Request fetched successfully",
	            dto
	    );

	    return ResponseEntity.ok(response);
	}

//	@PutMapping("/{requestType}/{id}")
//	@Operation(summary = "Update request by ID and type")
//	@ApiResponses(value = {
//	    @ApiResponse(responseCode = "200", description = "Request updated successfully"),
//	    @ApiResponse(responseCode = "404", description = "Request not found"),
//	    @ApiResponse(responseCode = "500", description = "Internal server error")
//	})
//	public ResponseEntity<GenericResponse<CommonRequestResponseDTO>> updateRequest(
//	        @PathVariable String requestType,
//	        @PathVariable Long id,
//	        @RequestBody CommonRequestResponseDTO updateDto
//	) {
//	    CommonRequestResponseDTO updatedRequest = extensionRequestService.updateRequest(id, requestType, updateDto);
//
//
//	    return ResponseEntity.ok(
//	            new GenericResponse<>(
//	                    HttpStatus.OK.value(),
//	                    true,
//	                    "Request updated successfully",
//	                    updatedRequest
//	            )
//	    );
//	}
	
	@PutMapping("/{requestType}/{id}")
	@Operation(summary = "Update request by ID and type")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Request updated successfully"),
	    @ApiResponse(responseCode = "404", description = "Request not found"),
	    @ApiResponse(responseCode = "500", description = "Internal server error")
	})
	public ResponseEntity<GenericResponse<CommonRequestResponseDTO>> updateRequest(
	        @PathVariable String requestType,
	        @PathVariable Long id,
	        @RequestBody CommonRequestResponseDTO updateDto
	) {
	    CommonRequestResponseDTO updatedRequest = extensionRequestService.updateRequest(id, requestType, updateDto);

	    return ResponseEntity.ok(
	            new GenericResponse<>(
	                    HttpStatus.OK.value(),
	                    true,
	                    "Request updated successfully",
	                    updatedRequest
	            )
	    );
	}


}
