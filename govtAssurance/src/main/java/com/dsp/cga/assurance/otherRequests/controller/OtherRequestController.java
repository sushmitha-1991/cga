package com.dsp.cga.assurance.otherRequests.controller;

import java.time.LocalDate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dsp.cga.assurance.otherRequests.dto.OtherRequestDTO;
import com.dsp.cga.assurance.otherRequests.service.OtherRequestService;
import com.dsp.cga.response.GenericResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("cga/otherRequest")
public class OtherRequestController {
	
	@Autowired
	private OtherRequestService otherRequestService;
	
	@PostMapping("/create")
//    @Operation(summary = "Create a new Other Request")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Created successfully"),
//            @ApiResponse(responseCode = "400", description = "Invalid input")
//    })
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
	public List<OtherRequestDTO> getOtherRequests(
	        @RequestParam(required = false) Long sessionNo,
	        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestedDate,
	        @RequestParam(required = false) Long statusId,
	        @RequestParam(required = false) Integer ministryCode  // 🔄 changed from ministryId
	) {
	    return otherRequestService.getFilteredOtherRequests(sessionNo, requestedDate, statusId, ministryCode);
	}

}
