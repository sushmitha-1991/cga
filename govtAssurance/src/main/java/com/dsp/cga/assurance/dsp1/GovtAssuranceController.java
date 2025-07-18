package com.dsp.cga.assurance.dsp1;

import com.dsp.cga.response.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/cga/dsp1")
public class GovtAssuranceController {

    @Autowired
    private GovtAssuranceResponseService govtAssuranceResponseService;
    @GetMapping("/govtAssuranceList")
    @Operation(
        summary = "Fetch Govt Assurance list by MP Code and filters",
        description = "Returns a list of assurances based on optional filters like loksabha, session, subject, etc."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Assurance list fetched successfully",
            content = @Content(schema = @Schema(implementation = GenericResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<GenericResponse<GovtAssuranceResponseDTO>> getAssuranceList(
            @RequestParam (required = true)String mpCode,
            @RequestParam(required = false) String loksabha,
            @RequestParam(required = false) String session,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String member,
            @RequestParam(required = false) String ministry,
            @RequestParam(required = false) String qType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String catg,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo,
            @RequestParam(required = false) String rsSession
    ) {
    GovtAssuranceResponseDTO dto = govtAssuranceResponseService.getGovtAssuranceList(
            mpCode,
            loksabha,
            session,
            subject,
            member,
            ministry,
            qType,
            status,
            catg,
            dateFrom,
            dateTo,
            rsSession
    );

    GenericResponse<GovtAssuranceResponseDTO> response = new GenericResponse<>(
            HttpStatus.OK.value(),
            true,
            "Assurance list fetched successfully",
            dto
    );

    return ResponseEntity.ok(response);
}
}
