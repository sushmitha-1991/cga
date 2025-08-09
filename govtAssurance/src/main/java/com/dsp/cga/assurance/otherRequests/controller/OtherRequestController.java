package com.dsp.cga.assurance.otherRequests.controller;

import java.text.MessageFormat;
import java.time.LocalDate;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

import com.dsp.cga.assurance.otherRequests.dto.OtherRequestDTO;
import com.dsp.cga.assurance.otherRequests.service.OtherRequestService;
import com.dsp.cga.assurance.repository.AssuranceRepository;
import com.dsp.cga.response.GenericResponse;
import com.dsp.cga.util.Utils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController  
@RequestMapping("cga/otherRequest")
public class OtherRequestController {
	
	@Autowired
	private OtherRequestService otherRequestService;
	
	@Autowired
	private AssuranceRepository assuranceRepository;
	
	@PostMapping("/create")
    @Operation(summary = "Create a new Other Request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<GenericResponse<OtherRequestDTO>> create(@RequestBody OtherRequestDTO dto) {
		System.out.println(dto);
        OtherRequestDTO created = otherRequestService.createOtherRequest(dto);
        GenericResponse<OtherRequestDTO> response = new GenericResponse<>(
                HttpStatus.CREATED.value(),
                true,
                "Other Request created successfully",
                created
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

	@GetMapping("/fillter")
	@Operation(summary = "Get filtered list of OtherRequests")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "OtherRequests fetched successfully"),
	    @ApiResponse(responseCode = "500", description = "Internal server error")
	})
	public ResponseEntity<GenericResponse<List<OtherRequestDTO>>> getOtherRequests(
	        @RequestParam(required = false) Long sessionNo,
	        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestedDate,
	        @RequestParam(required = false) String statusForMinistry,
	        @RequestParam(required = false) Integer ministryCode
	) {
	    List<OtherRequestDTO> results = otherRequestService.getFilteredOtherRequests(sessionNo, requestedDate, statusForMinistry, ministryCode);

	    if (results.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.OK)
	            .body(new GenericResponse<>(HttpStatus.OK.value(), false, "No OtherRequest(s) found", null));
	    }

	    GenericResponse<List<OtherRequestDTO>> response = new GenericResponse<>(
	        HttpStatus.OK.value(), true, "OtherRequest(s) fetched successfully", results
	    );

	    return ResponseEntity.ok(response);
	}

	  @GetMapping("/{id}")
	    @Operation(summary = "Get OtherRequest by ID")
	    @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "OtherRequest fetched successfully"),
	        @ApiResponse(responseCode = "404", description = "OtherRequest not found"),
	        @ApiResponse(responseCode = "500", description = "Internal server error")
	    })
	    public ResponseEntity<GenericResponse<OtherRequestDTO>> getOtherRequestById(@PathVariable Long id) {
	        OtherRequestDTO dto = otherRequestService.getOtherRequestById(id);

	        if (dto == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new GenericResponse<>(HttpStatus.NOT_FOUND.value(), false, "OtherRequest not found", null));
	        }

	        return ResponseEntity.ok(
	            new GenericResponse<>(HttpStatus.OK.value(), true, "OtherRequest fetched successfully", dto)
	        );
	    }
	  
	  @PutMapping("/update/{id}")
	  @Operation(summary = "Update an existing Other Request by ID")
	  @ApiResponses(value = {
	      @ApiResponse(responseCode = "200", description = "Other Request updated successfully"),
	      @ApiResponse(responseCode = "404", description = "Other Request not found"),
	      @ApiResponse(responseCode = "400", description = "Invalid input")
	  })
	  public ResponseEntity<GenericResponse<OtherRequestDTO>> updateOtherRequestById(
	          @PathVariable Long id,
	          @RequestBody OtherRequestDTO dto) {

	      OtherRequestDTO updated = otherRequestService.updateOtherRequestById(id, dto);

	      if (updated == null) {
	          return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                  .body(new GenericResponse<>(HttpStatus.NOT_FOUND.value(), false, "Other Request not found", null));
	      }

	      return ResponseEntity.ok(
	              new GenericResponse<>(HttpStatus.OK.value(), true, "Other Request updated successfully", updated)
	      );
	  }

		@PostMapping("/create-or-update")
		@Operation(summary = "Update an existing Other Request by ID")
		@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Other Request updated successfully"),
				@ApiResponse(responseCode = "404", description = "Other Request not found"),
				@ApiResponse(responseCode = "400", description = "Invalid input") })
		public ResponseEntity<GenericResponse<OtherRequestDTO>> updateOtherRequestById(
				@RequestBody OtherRequestDTO dto) {

			if (dto == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
						new GenericResponse<>(HttpStatus.NOT_FOUND.value(), false, "Other Request not found", null));
			}

			OtherRequestDTO savedDto = null;

			if (dto.getId() == null) { // create
				savedDto = otherRequestService.createOtherRequest(dto);
			} else { // update
				savedDto = otherRequestService.updateOtherRequestById(dto.getId(), dto);
			}

			if (savedDto == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
						new GenericResponse<>(HttpStatus.NOT_FOUND.value(), false, "Other Request not found", null));
			}

			return ResponseEntity
					.ok(new GenericResponse<>(HttpStatus.OK.value(), true, generateResponseMsg(dto, savedDto), savedDto));
		}
	  
		
	private String generateResponseMsg(OtherRequestDTO input, OtherRequestDTO saved) {

		log.debug("REQ for MSG: " + input);

		if (input == null)
			return "Success";
		boolean isDraft = input.getDraftStatus();
		String response = isDraft ? Utils.draftMsg : Utils.createMsg;

		if (!isDraft && input.getId() != null) response = Utils.updateMsg;
		
		if (StringUtils.isEmpty(saved.getAssuranceNo())) {
			String assuranceNo = assuranceRepository.findAssuranceNoById(input.getAssuranceId());
			input.setAssuranceNo(assuranceNo);
		}
		log.debug("Assurance No: " + input.getAssuranceNo());

		String finalMsg = MessageFormat.format(response, "Other request", input.getAssuranceNo());

		return finalMsg;
	}

	@GetMapping("/accepted-records/{assuranceId}")
	@Operation(summary = "Get Accepted Other Requests by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Assurance fetched successfully"),
			@ApiResponse(responseCode = "404", description = "Assurance not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<GenericResponse<OtherRequestDTO>> getAcceptedOtherRequest(
											@PathVariable Long assuranceId) {

		OtherRequestDTO request = otherRequestService.getAcceptedRequest(assuranceId);

		if (request == null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new GenericResponse<>(HttpStatus.OK.value(), false, "Other Request not found", null));
		}

		GenericResponse<OtherRequestDTO> response = new GenericResponse<>(HttpStatus.OK.value(), true,
				"DroppingRequest fetched successfully", request);

		return ResponseEntity.ok(response);

	}	 	  
}
