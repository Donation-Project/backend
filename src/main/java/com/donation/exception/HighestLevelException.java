package com.donation.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class HighestLevelException extends RuntimeException{

    private final Map<String, String> validation = new HashMap<>();

    public HighestLevelException(String message) {
        super(message);
    }

    public abstract String statusCode();

    public void addValidation(String fieldName, String errorMessage) {
        validation.put(fieldName,errorMessage);
    }
}
