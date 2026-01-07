package com.team5.demo.validation;

import com.team5.demo.dto.CreateSessionRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SessionTimeValidator implements ConstraintValidator<ValidSessionTime, CreateSessionRequest> {

    @Override
    public void initialize(ValidSessionTime annotation) {
    }

    @Override
    public boolean isValid(CreateSessionRequest request, ConstraintValidatorContext context) {
        // Allow null values for optional fields
        if (request.getStartTime() == null || request.getEndTime() == null) {
            return true;
        }

        // Check if end time is after start time
        boolean isValid = request.getEndTime().isAfter(request.getStartTime());

        if (!isValid) {
            // Add custom error message to end time field
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("End time must be after start time")
                    .addPropertyNode("endTime")
                    .addConstraintViolation();
        }

        return isValid;
    }
}
