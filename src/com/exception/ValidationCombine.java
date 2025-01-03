package com.exception;

import java.util.List;

public class ValidationCombine extends Exception{
    String message;

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ValidationCombine(List<ValidationException> validationExceptions){
        StringBuilder sb=new StringBuilder();
        for (ValidationException v:validationExceptions){
            sb.append(v.getError());
            sb.append("dans la field");
            sb.append(v.getField());
        }
        this.setMessage(sb.toString());
    }
}
