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

import com.dsp.cga.report.dto.ConsolidatedMinistryWiseFimpAssurancesResponseDto;
import com.dsp.cga.report.dto.ConsolidatedMinistryWisePendingAssurancesResponseDto;
import com.dsp.cga.report.dto.DroppedAssurancesReportResponseDto;
import com.dsp.cga.report.dto.ExtensionReportResponseDto;
import com.dsp.cga.report.dto.ImplementationReportResponseDto;
import com.dsp.cga.report.dto.MinistryWisePendingAssurancesResponseDto;
import com.dsp.cga.report.dto.PendingAssurancesListResponseDto;
import com.dsp.cga.report.dto.ReportRequestDto;
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
				HttpStatus.OK.value(), true, "Implementation report fetched successfully", result);

		return ResponseEntity.status(HttpStatus.OK).body(response);
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
		GenericResponse<List<ExtensionReportResponseDto>> response = new GenericResponse<>(HttpStatus.OK.value(),
				true, "Extension report fetched successfully", result);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/get-dropped-assurance-report")
	@Operation(summary = "Get Dropped Assurance report")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Dropped Assurance Report created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<GenericResponse<List<DroppedAssurancesReportResponseDto>>> getDroppedAssuranceReport() {
		System.out.println("------Inside dropped assurance report controller-----");
		List<DroppedAssurancesReportResponseDto> result = reportService.getDroppedAssuranceReport();
		if (result.size() > 0) {
			System.out.println(" result :" + result);
		} else {
			System.out.println(" list empty :" + result);
		}
		GenericResponse<List<DroppedAssurancesReportResponseDto>> response = new GenericResponse<>(HttpStatus.OK.value(),
				true, "Dropped assurance report fetched successfully", result);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/get-pending-assurance-report")
	@Operation(summary = "Get Pending Assurance report")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Pending Assurance Report created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<GenericResponse<List<PendingAssurancesListResponseDto>>> getPendingAssurancesReport() {
		System.out.println("------Inside pending assurance report controller-----");
		List<PendingAssurancesListResponseDto> result = reportService.getPendingAssurancesReport();
		if (result.size() > 0) {
			System.out.println(" result :" + result);
		} else {
			System.out.println(" list empty :" + result);
		}
		GenericResponse<List<PendingAssurancesListResponseDto>> response = new GenericResponse<>(HttpStatus.OK.value(),
				true, "Pending assurance report fetched successfully", result);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/get-ministry-wise-pending-assurance-report")
	@Operation(summary = "Get Ministry Wise Pending Assurance report")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Ministry Wise Pending Assurance Report created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<GenericResponse<List<MinistryWisePendingAssurancesResponseDto>>> getMinistryWisePendingAssurancesReport() {
		System.out.println("------Inside ministry wise pending assurance report controller-----");
		List<MinistryWisePendingAssurancesResponseDto> result = reportService.getMinistryWisePendingAssurancesReport();
		if (result.size() > 0) {
			System.out.println(" result :" + result);
		} else {
			System.out.println(" list empty :" + result);
		}
		GenericResponse<List<MinistryWisePendingAssurancesResponseDto>> response = new GenericResponse<>(HttpStatus.OK.value(),
				true, "Ministry wise pending assurance report fetched successfully", result);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/get-consolidated-ministry-wise-pending-assurance-report")
	@Operation(summary = "Get Consolidated Ministry Wise Pending Assurance report")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Consolidated Ministry Wise Pending Assurance Report created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<GenericResponse<List<ConsolidatedMinistryWisePendingAssurancesResponseDto>>> getConsolidatedMinistryWisePendingAssurancesReport() {
		System.out.println("------Inside Consolidated ministry wise pending assurance report controller-----");
		List<ConsolidatedMinistryWisePendingAssurancesResponseDto> result = reportService.getConsolidatedMinistryWisePendingAssurancesReport();
		if (result.size() > 0) {
			System.out.println(" result :" + result);
		} else {
			System.out.println(" list empty :" + result);
		}
		GenericResponse<List<ConsolidatedMinistryWisePendingAssurancesResponseDto>> response = new GenericResponse<>(HttpStatus.OK.value(),
				true, "Consolidated Ministry wise pending assurance report fetched successfully", result);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/get-consolidated-ministry-wise-fimp-assurance-report")
	@Operation(summary = "Get Consolidated Ministry Wise Fimp Assurance report")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Consolidated Ministry Wise Fimp Assurance Report created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<GenericResponse<List<ConsolidatedMinistryWiseFimpAssurancesResponseDto>>> getConsolidatedMinistryWiseFimpAssurancesReport() {
		System.out.println("------Inside Consolidated ministry wise fimp assurance report controller-----");
		List<ConsolidatedMinistryWiseFimpAssurancesResponseDto> result = reportService.getConsolidatedMinistryWiseFimpAssurancesReport();
		if (result.size() > 0) {
			System.out.println(" result :" + result);
		} else {
			System.out.println(" list empty :" + result);
		}
		GenericResponse<List<ConsolidatedMinistryWiseFimpAssurancesResponseDto>> response = new GenericResponse<>(HttpStatus.OK.value(),
				true, "Consolidated Ministry wise fimp assurance report fetched successfully", result);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
