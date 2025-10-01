package com.dsp.cga.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.dsp.cga.response.GenericResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<GenericResponse<Void>> handleGeneralException(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericResponse<>(
				HttpStatus.INTERNAL_SERVER_ERROR.value(), false, "An unexpected error occurred.", null));
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<GenericResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException ex) {
		GenericResponse<Void> response = new GenericResponse<>(HttpStatus.NOT_FOUND.value(), false, ex.getMessage(),
				null);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<GenericResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
		// Create a GenericResponse with a failure status
		GenericResponse<Void> response = new GenericResponse<>(HttpStatus.OK.value(), false, ex.getMessage(), null);

		// Return the response with HTTP 400 status
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<String> handleIllegalState(IllegalStateException ex) {
		return ResponseEntity.badRequest().body(ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<GenericResponse<Map<String, String>>> handleValidationExceptions(
			MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}

		GenericResponse<Map<String, String>> response = new GenericResponse<>(HttpStatus.BAD_REQUEST.value(), false,
				"Validation failed", errors);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<GenericResponse<String>> handleUserNotFoundException(UserNotFoundException ex) {
		GenericResponse<String> response = new GenericResponse<>(HttpStatus.UNAUTHORIZED.value(), false,
				ex.getMessage(), null);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	// @ExceptionHandler(IllegalArgumentException.class)
//	public ResponseEntity<GenericResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
//	    GenericResponse<Object> response = new GenericResponse<>(HttpStatus.BAD_REQUEST.value(), false, ex.getMessage(), null);
//	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//	}

}
