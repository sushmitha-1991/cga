package com.dsp.cga.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.dsp.cga.response.GenericResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<GenericResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException ex) {
		GenericResponse<Void> response = new GenericResponse<>(HttpStatus.NOT_FOUND.value(), false, ex.getMessage(),
				null);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<GenericResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
		// Create a GenericResponse with a failure status
		GenericResponse<Void> response = new GenericResponse<>(HttpStatus.BAD_REQUEST.value(), false, ex.getMessage(),
				null);

		// Return the response with HTTP 400 status
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

}
