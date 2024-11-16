package com.dsp.govtAssurance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.dsp.govtAssurance.response.GenericResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<GenericResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException ex) {
		GenericResponse<Void> response = new GenericResponse<>(HttpStatus.NOT_FOUND.value(), false, ex.getMessage(),
				null);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	

	
}
