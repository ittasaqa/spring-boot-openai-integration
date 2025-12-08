package com.showcase.aiintegration.exception;

/**
 * AI Service Exception
 * 
 * Custom exception for AI service-related errors
 */
public class AIServiceException extends RuntimeException {

    public AIServiceException(String message) {
        super(message);
    }

    public AIServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
