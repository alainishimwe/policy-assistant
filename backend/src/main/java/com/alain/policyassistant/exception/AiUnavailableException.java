package com.alain.policyassistant.exception;

public class AiUnavailableException extends RuntimeException {

    public AiUnavailableException(Throwable cause) {
        super("The AI service is temporarily unavailable.", cause);
    }
}