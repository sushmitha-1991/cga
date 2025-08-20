package com.dsp.cga.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponse<T> {
	private int statusCode;
	private boolean success;
	private String message;
	private T data;
	
	public GenericResponse(boolean success, String message, T data, int statusCode) {
		this.statusCode = statusCode;
		this.success = success;
		this.message = message;
		this.data = data;
	}
}
