package com.team5.demo.dto;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorResponse {
    private String message;
    private List<FieldError> errors;

    public ValidationErrorResponse() {
        this.errors = new ArrayList<>();
    }

    public ValidationErrorResponse(String message) {
        this.message = message;
        this.errors = new ArrayList<>();
    }

    public ValidationErrorResponse(String message, List<FieldError> errors) {
        this.message = message;
        this.errors = errors;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FieldError> getErrors() {
        return errors;
    }

    public void setErrors(List<FieldError> errors) {
        this.errors = errors;
    }

    public void addError(String field, String message) {
        this.errors.add(new FieldError(field, message));
    }

    // Inner class for field-specific errors
    public static class FieldError {
        private String field;
        private String message;

        public FieldError() {
        }

        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
