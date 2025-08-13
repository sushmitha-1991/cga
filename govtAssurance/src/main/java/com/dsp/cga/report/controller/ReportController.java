package com.dsp.cga.report.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dsp.cga.report.dto.ImplementationReportResponseDto;
import com.dsp.cga.report.dto.ReportRequestDto;
import com.dsp.cga.report.repository.ReportRepository;
import com.dsp.cga.report.service.ReportService;
import com.dsp.cga.response.GenericResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/cga/reports")
public class ReportController {

	@Autowired
	private ReportService reportService;
	
	@Autowired
	private ReportRepository reportRepo;

	
	 @PostMapping("/get-implementation-report")
	    @Operation(summary = "Get Implementation report")
	    @ApiResponses(value = {
	        @ApiResponse(responseCode = "201", description = "Implementation Report created successfully"),
	        @ApiResponse(responseCode = "400", description = "Invalid input data"),
	        @ApiResponse(responseCode = "500", description = "Internal server error")
	    })
	    public ResponseEntity<GenericResponse<List<ImplementationReportResponseDto>>> getImplementationReport(
	            @RequestBody ReportRequestDto dto) {

		 List<ImplementationReportResponseDto> result = reportService.getImplementationReport(dto.getFromDate(),dto.getToDate());
	        GenericResponse<List<ImplementationReportResponseDto>> response = new GenericResponse<>(
	            HttpStatus.FOUND.value(),
	            true,
	            "Implementation report fetched successfully",
	            result
	        );

	        return ResponseEntity.status(HttpStatus.FOUND).body(response);
	    }
	 
	 @GetMapping("/test-id")
	 public List<Long> testId() {
	     return reportRepo.testId();
	 }
	 
	 @GetMapping("/check-db")
	 public String checkDb() {
	     Object[] result = reportRepo.findCurrentDbAndSchema();
	     return "Database: " + result[0] + ", Schema: " + result[1];
	 }
}
