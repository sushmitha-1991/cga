package com.dsp.cga.assurance.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.dsp.cga.assurance.dto.AssuranceDTO;
import com.dsp.cga.assurance.dto.AssuranceListDTO;
import com.dsp.cga.assurance.dto.DashboardAssurancesCountResponseDTO;
import com.dsp.cga.assurance.dto.DashboardAssurancesDetailsResponseDTO;
import com.dsp.cga.assurance.dto.DocumentMetadataDTO;
import com.dsp.cga.assurance.service.AssuranceService;
import com.dsp.cga.entity.RequestHistory;
import com.dsp.cga.report.service.ReportService;
import com.dsp.cga.response.GenericResponse;
import com.dsp.cga.util.DateFormatterUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/cga/assurance")
public class AssuranceController {

	private static final Logger log = LoggerFactory.getLogger(AssuranceController.class);


	@Autowired
	private AssuranceService assuranceService;
	
	@Autowired
	private ReportService reportService;


	@PostMapping("/create")
	@Operation(summary = "Create a new Assurance")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Assurance created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<GenericResponse<AssuranceDTO>> createAssurance(@RequestBody AssuranceDTO assuranceDTO) {

		System.out.println("@@@@@@@@@@@@@"+ assuranceDTO);
		AssuranceDTO createdAssurance = assuranceService.createAssurance(assuranceDTO);
		GenericResponse<AssuranceDTO> response = new GenericResponse<>(HttpStatus.CREATED.value(), true,
				"Assurance item created successfully", createdAssurance);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

//	@GetMapping("/getAll")
//	@Operation(summary = "Get all Assurance")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Assurance fetched successfully"),
//			@ApiResponse(responseCode = "500", description = "Internal server error") })
//	public ResponseEntity<GenericResponse<List<AssuranceDTO>>> getAllAssurance() {
//		List<AssuranceDTO> assurance = assuranceService.getAllAssurance();
//		GenericResponse<List<AssuranceDTO>> response = new GenericResponse<>(HttpStatus.OK.value(), true,
//				"assurance fetched successfully", assurance);
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}

	@GetMapping("/getAll")
	@Operation(summary = "Get all Assurance")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Assurance fetched successfully"),
	    @ApiResponse(responseCode = "500", description = "Internal server error")
	})
	public ResponseEntity<Map<String, Object>> getAllAssurance() {
	    List<AssuranceDTO> assuranceList = assuranceService.getAllAssurance();

	    Map<String, Object> response = new HashMap<>();
	    response.put("statusCode", HttpStatus.OK.value());
	    response.put("success", true);
	    response.put("message", "assurance fetched successfully");
	    response.put("count", assuranceList.size()); // ✅ new count field
	    response.put("data", assuranceList);         // ✅ keep data as-is

	    return new ResponseEntity<>(response, HttpStatus.OK);
	}


//	@GetMapping("/getAll")
//	@Operation(summary = "Get all Assurance")
//	@ApiResponses(value = { 
//	    @ApiResponse(responseCode = "200", description = "Assurance fetched successfully"),
//	    @ApiResponse(responseCode = "500", description = "Internal server error") 
//	})
//	public ResponseEntity<GenericResponse<Map<String, Object>>> getAllAssurance() {
//	    List<AssuranceDTO> assuranceList = assuranceService.getAllAssurance();
//
//	    Map<String, Object> responseData = new HashMap<>();
//	    responseData.put("count", assuranceList.size());
//	    responseData.put("assuranceList", assuranceList);
//
//	    GenericResponse<Map<String, Object>> response = new GenericResponse<>(
//	        HttpStatus.OK.value(),
//	        true,
//	        "Assurance fetched successfully",
//	        responseData
//	    );
//
//	    return new ResponseEntity<>(response, HttpStatus.OK);
//	}



//	@GetMapping("/getAllLokSabha")
//	@Operation(summary = "Get all Assurance")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Assurance fetched successfully"),
//			@ApiResponse(responseCode = "500", description = "Internal server error") })
//	public ResponseEntity<GenericResponse<List<AssuranceDTO>>> getAllLokSabhaAssurance() {
//		List<AssuranceDTO> assurance = assuranceService.getAllLokSabhaAssurance();
//		GenericResponse<List<AssuranceDTO>> response = new GenericResponse<>(HttpStatus.OK.value(), true,
//				"assurance fetched successfully", assurance);
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}

//	@GetMapping("/getAllLokSabha")
//	@Operation(summary = "Get all Assurance")
//	@ApiResponses(value = {
//	    @ApiResponse(responseCode = "200", description = "Assurance fetched successfully"),
//	    @ApiResponse(responseCode = "500", description = "Internal server error")
//	})
//	public ResponseEntity<GenericResponse<Map<String, Object>>> getAllLokSabhaAssurance() {
//	    List<AssuranceDTO> assuranceList = assuranceService.getAllLokSabhaAssurance();
//
//	    Map<String, Object> responseData = new HashMap<>();
//	    responseData.put("count", assuranceList.size());
//	    responseData.put("assuranceList", assuranceList);
//
//	    GenericResponse<Map<String, Object>> response = new GenericResponse<>(
//	        HttpStatus.OK.value(),
//	        true,
//	        "Assurance fetched successfully",
//	        responseData
//	    );
//
//	    return new ResponseEntity<>(response, HttpStatus.OK);
//	}

	@GetMapping("/getAllLokSabha")
	@Operation(summary = "Get all Assurance")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Assurance fetched successfully"),
	    @ApiResponse(responseCode = "500", description = "Internal server error")
	})
	public ResponseEntity<Map<String, Object>> getAllLokSabhaAssurance() {
	    List<AssuranceDTO> assuranceList = assuranceService.getAllLokSabhaAssurance();

	    Map<String, Object> response = new HashMap<>();
	    response.put("statusCode", HttpStatus.OK.value());
	    response.put("success", true);
	    response.put("message", "assurance fetched successfully");
	    response.put("count", assuranceList.size()); // ✅ new count field
	    response.put("data", assuranceList);         // ✅ keep data as-is

	    return new ResponseEntity<>(response, HttpStatus.OK);
	}



	@GetMapping("/{id}")
	@Operation(summary = "Get Assurance by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Assurance fetched successfully"),
			@ApiResponse(responseCode = "404", description = "Assurance not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<GenericResponse<AssuranceDTO>> getAssuranceById(@PathVariable Long id) {
//		try {
			AssuranceDTO assurance = assuranceService.getAssuranceById(id);

			if (assurance == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new GenericResponse<>(HttpStatus.NOT_FOUND.value(), false, "Assurance not found", null));
			}

			GenericResponse<AssuranceDTO> response = new GenericResponse<>(HttpStatus.OK.value(), true,
					"Assurance fetched successfully", assurance);

			return ResponseEntity.ok(response);

//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericResponse<>(
//					HttpStatus.INTERNAL_SERVER_ERROR.value(), false, "Error fetching assurance", null));
//		}
	}



//	@PutMapping("/update/{id}")
//	@Operation(summary = "Update an existing Assurance by ID")
//	@ApiResponses(value = {
//	        @ApiResponse(responseCode = "200", description = "Assurance updated successfully"),
//	        @ApiResponse(responseCode = "400", description = "Invalid input data"),
//	        @ApiResponse(responseCode = "404", description = "Assurance not found"),
//	        @ApiResponse(responseCode = "500", description = "Internal server error")
//	})
//	public ResponseEntity<GenericResponse<AssuranceDTO>> updateAssuranceById(
//	        @PathVariable Long id, 
//	        @RequestBody AssuranceDTO assuranceDTO) {
//
//	    AssuranceDTO updatedAssurance = assuranceService.updateAssuranceById(id, assuranceDTO);
//	    GenericResponse<AssuranceDTO> response = new GenericResponse<>(
//	            HttpStatus.OK.value(), 
//	            true, 
//	            "Assurance updated successfully", 
//	            updatedAssurance);
//	    return ResponseEntity.ok(response);
//	}

	@PutMapping("/update/{id}")
	@Operation(summary = "Update an existing Assurance by ID")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Assurance updated successfully"),
	        @ApiResponse(responseCode = "400", description = "Invalid input data"),
	        @ApiResponse(responseCode = "404", description = "Assurance not found"),
	        @ApiResponse(responseCode = "500", description = "Internal server error")
	})
	public ResponseEntity<GenericResponse<AssuranceDTO>> updateAssuranceById(
	        @PathVariable Long id,
	        @RequestBody AssuranceDTO assuranceDTO) {

	    AssuranceDTO updatedAssurance = assuranceService.updateAssuranceById(id, assuranceDTO);
	    GenericResponse<AssuranceDTO> response = new GenericResponse<>(
	            HttpStatus.OK.value(),
	            true,
	            "Assurance updated successfully",
	            updatedAssurance);
	    return ResponseEntity.ok(response);
	}


	@GetMapping("/getAllAssuranceList")
	@Operation(summary = "Get all Assurance")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Assurance fetched successfully"),
	        @ApiResponse(responseCode = "500", description = "Internal server error")
	})
	public ResponseEntity<GenericResponse<List<AssuranceListDTO>>> getAllAssuranceList() {
	    List<AssuranceListDTO> assurances = assuranceService.getAllAssurancesList();
	    GenericResponse<List<AssuranceListDTO>> response = new GenericResponse<>(
	            HttpStatus.OK.value(),
	            true,
	            "Assurance fetched successfully",
	            assurances
	    );
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/filters")
	@Operation(summary = "Get filtered Assurances")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Filtered assurances fetched successfully"),
	        @ApiResponse(responseCode = "500", description = "Internal server error")
	})
	public ResponseEntity<GenericResponse<List<AssuranceDTO>>> getFilteredAssurances(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
	        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
	        @RequestParam(required = false) Long ministryId,
	        @RequestParam(required = false) Long rajyaSabhaSessionNo,
	        @RequestParam(required = false) Long memberId) {

		 log.info("fromDate: {}", fromDate);
		    log.info("toDate: {}", toDate);
		    log.info("ministryId: {}", ministryId);
		    log.info("memberId: {}", memberId);
	    List<AssuranceDTO> assurances = assuranceService.getFilteredAssurances(fromDate, toDate, ministryId, rajyaSabhaSessionNo, memberId);
	    GenericResponse<List<AssuranceDTO>> response = new GenericResponse<>(
	            HttpStatus.OK.value(),
	            true,
	            "Filtered assurances fetched successfully",
	            assurances
	    );
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}


	@GetMapping("/filters/ls")
	@Operation(summary = "Get filtered Assurances")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Filtered assurances fetched successfully"),
	        @ApiResponse(responseCode = "500", description = "Internal server error")
	})
	public ResponseEntity<GenericResponse<List<AssuranceDTO>>> getFilteredAssurancesLs(
	        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
	        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
	        @RequestParam(required = false) Long ministryId,
	        @RequestParam(required = false) Long lokSabhaNo,
	        @RequestParam(required = false) Long lokSabhaSessionNo,
	        @RequestParam(required = false) Long memberId) {

	    List<AssuranceDTO> assurances = assuranceService.getFilteredAssurancesLs(fromDate, toDate, ministryId, lokSabhaNo, lokSabhaSessionNo, memberId);
	    GenericResponse<List<AssuranceDTO>> response = new GenericResponse<>(
	            HttpStatus.OK.value(),
	            true,
	            "Filtered assurances fetched successfully",
	            assurances
	    );
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}


//	@GetMapping("/assurance-numbers")
//	@Operation(summary = "Get assurance numbers")
//	@ApiResponses(value = {
//	        @ApiResponse(responseCode = "200", description = "Assurance numbers fetched successfully"),
//	        @ApiResponse(responseCode = "500", description = "Internal server error")
//	})
//	public ResponseEntity<GenericResponse<List<String>>> getAssuranceNumbers(
//	        @RequestParam(required = false) Integer ministryCode,
//	        @RequestParam(required = false) Long sessionNo,
//	        @RequestParam(required = false) Integer houseId) {
//
//	    List<String> assuranceNos = assuranceService.getAssuranceNos(ministryCode, sessionNo, houseId);
//
//	    GenericResponse<List<String>> response = new GenericResponse<>(
//	            HttpStatus.OK.value(),
//	            true,
//	            "Assurance numbers fetched successfully",
//	            assuranceNos
//	    );
//
//	    return new ResponseEntity<>(response, HttpStatus.OK);
//	}

	 @GetMapping("/assurance-numbers")
	    @Operation(summary = "Get assurance numbers")
	    @ApiResponses(value = {
	            @ApiResponse(responseCode = "200", description = "Assurance numbers fetched successfully"),
	            @ApiResponse(responseCode = "500", description = "Internal server error")
	    })
	    public ResponseEntity<GenericResponse<List<AssuranceListDTO>>> getAssuranceNumbers(
	            @RequestParam(required = false) Integer ministryCode,
	            @RequestParam(required = false) Long sessionNo,
	            @RequestParam(required = false) Long houseId) {

	        List<AssuranceListDTO> assuranceNos = assuranceService.getAssuranceNos(ministryCode, sessionNo, houseId);

	        GenericResponse<List<AssuranceListDTO>> response = new GenericResponse<>(
	                HttpStatus.OK.value(),
	                true,
	                "Assurance numbers fetched successfully",
	                assuranceNos
	        );

	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }

	@GetMapping("/by-assurance-no")
    public ResponseEntity<AssuranceDTO> getByAssuranceNo(@RequestParam String assuranceNo) {
        AssuranceDTO dto = assuranceService.getByAssuranceNo(assuranceNo);
        return ResponseEntity.ok(dto);
    }

//	 @GetMapping("/request-count")
//	    public ResponseEntity<AssuranceRequestCountDTO> getRequestCount(
//	            @RequestParam Long sessionNo,
//	            @RequestParam(required = false) Integer ministryCode) {
//
//	        AssuranceRequestCountDTO result = assuranceService.getAssuranceRequestCounts(sessionNo, ministryCode);
//	        return ResponseEntity.ok(result);
//	    }
	
	/*
	 * Get the Request History
	 */
	@GetMapping("/get-request-history")
	public ResponseEntity<GenericResponse<List<RequestHistory>>> getInputHistory(
			@RequestParam Long requestId) {
		System.out.println("inside history service in assurance");
		List<RequestHistory> histories = assuranceService.getInputHistoryByRequestId(requestId);
		
		if (histories.isEmpty()) {
			System.out.println("no records found");
			GenericResponse<List<RequestHistory>> response = new GenericResponse<>(false, "No records found",
					histories, HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		
		System.out.println("records found");
		GenericResponse<List<RequestHistory>> response = new GenericResponse<>(true, "Records found", histories,
				HttpStatus.OK.value());

		return ResponseEntity.ok(response);
	}

	
	@GetMapping("/get-request-history-documents")
	public ResponseEntity<GenericResponse<List<DocumentMetadataDTO>>> getInputDocuments (
															@RequestParam Long requestId) {
		System.out.println("inside history service in assurance");
		List<DocumentMetadataDTO> histories = assuranceService.getDocuments(requestId);

		if (histories.isEmpty()) {
			System.out.println("no records found");
			GenericResponse<List<DocumentMetadataDTO>> response = new GenericResponse<>(false, "No records found",
					histories, HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		System.out.println("records found");
		GenericResponse<List<DocumentMetadataDTO>> response = new GenericResponse<>(true, "Records found", histories,
				HttpStatus.OK.value());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/management-dashboard-assurances-count")
	public ResponseEntity<GenericResponse<DashboardAssurancesCountResponseDTO>> getMgmtDashboardAssurancesCount (
//			@RequestParam(required = true) String abbr,
			@RequestParam(required = false) Long lokSabhaNoId,
    		@RequestParam(required = false) Long sessionNoId,
    		@RequestParam(required = false) Long ministryId,
    		@RequestParam(required = false) Long memberId,
    		@RequestParam(required = false) Long statusId,
    		@RequestParam(required = false) Long categoryId) {
		System.out.println("inside dashboard assurance count service in assurance");
		DashboardAssurancesCountResponseDTO result = assuranceService.getMgmtDashboardAssurancesCount(lokSabhaNoId,
				sessionNoId,ministryId,memberId,statusId,categoryId);

		if (result==null) {
			GenericResponse<DashboardAssurancesCountResponseDTO> response = new GenericResponse<>(false, "No records found",
					result, HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		GenericResponse<DashboardAssurancesCountResponseDTO> response = new GenericResponse<>(true, "Mgmt Dashboard Assurances counts fetched successfully", result,
				HttpStatus.OK.value());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/management-dashboard-assurances-details")
	public ResponseEntity<GenericResponse<List<DashboardAssurancesDetailsResponseDTO>>> getMgmtDashboardAssurancesDetails (
			@RequestParam(required = false) Long lokSabhaNoId,
    		@RequestParam(required = false) Long sessionNoId,
    		@RequestParam(required = false) LocalDate fromDate,
    		@RequestParam(required = false) LocalDate toDate,
    		@RequestParam(required = false) Long categoryId,
    		@RequestParam(required = false) Long memberId,
    		@RequestParam(required = false) Long ministryId,
    		@RequestParam(required = false) Long departmentId,
    		@RequestParam(required = false) Long questionTypeId,
    		@RequestParam(required = false) Long statusId) {
		System.out.println("inside dashboard assurance details service in assurance");
		List<DashboardAssurancesDetailsResponseDTO> result = assuranceService.getMgmtDashboardAssurancesDetails(lokSabhaNoId, 
				  sessionNoId, fromDate, toDate,
				  categoryId,  memberId,  ministryId,
				  departmentId,  questionTypeId,  statusId);

		if (result.isEmpty()) {
			GenericResponse<List<DashboardAssurancesDetailsResponseDTO>> response = new GenericResponse<>(false, "No records found",
					result, HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		GenericResponse<List<DashboardAssurancesDetailsResponseDTO>> response = new GenericResponse<>(true, "Mgmt Dashboard Assurances Records fetched successfully", result,
				HttpStatus.OK.value());

		return ResponseEntity.ok(response);
	}
	
	
}
