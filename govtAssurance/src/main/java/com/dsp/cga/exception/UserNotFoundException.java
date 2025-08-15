package com.dsp.cga.exception;


public class UserNotFoundException extends RuntimeException {
	public UserNotFoundException(String message) {
		super(message);
	}
}
