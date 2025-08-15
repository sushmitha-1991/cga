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

import com.dsp.cga.assurance.dto.AssuranceDTO;
import com.dsp.cga.assurance.dto.AssuranceListDTO;
import com.dsp.cga.report.dto.ExtensionReportResponseDto;
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

	@PostMapping("/get-implementation-report")
	@Operation(summary = "Get Implementation report")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Implementation Report created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<GenericResponse<List<ImplementationReportResponseDto>>> getImplementationReport(
			@RequestBody ReportRequestDto dto) {

		List<ImplementationReportResponseDto> result = reportService.getImplementationReport(dto.getFromDate(),
				dto.getToDate());
		GenericResponse<List<ImplementationReportResponseDto>> response = new GenericResponse<>(
				HttpStatus.FOUND.value(), true, "Implementation report fetched successfully", result);

		return ResponseEntity.status(HttpStatus.FOUND).body(response);
	}

	@GetMapping("/get-extension-report")
	@Operation(summary = "Get Extension report")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Extension Report created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<GenericResponse<List<ExtensionReportResponseDto>>> getExtensionReport() {
		System.out.println("------Inside extension report controller-----");
		List<ExtensionReportResponseDto> result = reportService.getExtensionReport();
		if (result.size() > 0) {
			System.out.println(" result :" + result);
		} else {
			System.out.println(" list empty :" + result);
		}
		GenericResponse<List<ExtensionReportResponseDto>> response = new GenericResponse<>(HttpStatus.FOUND.value(),
				true, "Extension report fetched successfully", result);

		return ResponseEntity.status(HttpStatus.FOUND).body(response);
	}

}
