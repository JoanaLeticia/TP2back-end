package br.com.gameverse.validation;

import java.util.ArrayList;
import java.util.List;

import br.com.gameverse.application.Error;

public class ValidationError extends Error {

    record FieldError(String fieldName, String message) {};
    private List<FieldError> errors = null;
    
    public ValidationError(String code, String message) {
        super(code, message);
    }

    public void addFieldError(String fieldName, String message) {
        if (errors == null)
            errors = new ArrayList<FieldError>();
        errors.add(new FieldError(fieldName, message));
    }

    public List<FieldError> getErrors() {
        return errors.stream().toList();
    }
}