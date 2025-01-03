package com.exception;

public class ValidationException extends Exception{
    String field;
    String error;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ValidationException (String error, String field){
        this.setError(error);
        this.setField(field);
    }
}
