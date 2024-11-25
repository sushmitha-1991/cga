package com.dsp.cga.assurance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dsp.cga.assurance.dto.AssuranceDTO;
import com.dsp.cga.assurance.service.AssuranceService;
import com.dsp.cga.response.GenericResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/cga/assurance")
public class AssuranceController {

	@Autowired
	private AssuranceService assuranceService;

	@PostMapping("/create")
	@Operation(summary = "Create a new Assurance")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Assurance created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<GenericResponse<AssuranceDTO>> createAssurance(@RequestBody AssuranceDTO assuranceDTO) {
		AssuranceDTO createdAssurance = assuranceService.createAssurance(assuranceDTO);
		GenericResponse<AssuranceDTO> response = new GenericResponse<>(HttpStatus.CREATED.value(), true,
				"Assurance item created successfully", createdAssurance);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/getAll")
	@Operation(summary = "Get all Assurance")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Assurance fetched successfully"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<GenericResponse<List<AssuranceDTO>>> getAllAssurance() {
		List<AssuranceDTO> assurance = assuranceService.getAllAssurance();
		GenericResponse<List<AssuranceDTO>> response = new GenericResponse<>(HttpStatus.OK.value(), true,
				"assurance fetched successfully", assurance);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get Assurance by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Assurance fetched successfully"),
			@ApiResponse(responseCode = "404", description = "Assurance not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<GenericResponse<AssuranceDTO>> getAssuranceById(@PathVariable Long id) {
		try {
			AssuranceDTO assurance = assuranceService.getAssuranceById(id);

			if (assurance == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new GenericResponse<>(HttpStatus.NOT_FOUND.value(), false, "Assurance not found", null));
			}

			GenericResponse<AssuranceDTO> response = new GenericResponse<>(HttpStatus.OK.value(), true,
					"Assurance fetched successfully", assurance);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericResponse<>(
					HttpStatus.INTERNAL_SERVER_ERROR.value(), false, "Error fetching assurance", null));
		}
	}
}
