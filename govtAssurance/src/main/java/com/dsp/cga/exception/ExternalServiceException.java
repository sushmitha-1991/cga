package com.dsp.cga.exception;

public class ExternalServiceException extends RuntimeException {

    // Default constructor
    public ExternalServiceException() {
        super();
    }

    // Constructor with a message
    public ExternalServiceException(String message) {
        super(message);
    }

    // Constructor with a message and cause
    public ExternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor with a cause
    public ExternalServiceException(Throwable cause) {
        super(cause);
    }
}