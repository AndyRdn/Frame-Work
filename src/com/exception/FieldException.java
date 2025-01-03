package com.exception;

import java.util.ArrayList;
import java.util.List;

public class FieldException {
    List<ValidationCombine> validationCombines=new ArrayList<>();

    public List<ValidationCombine> getValidationCombines() {
        return validationCombines;
    }

    public void setValidationCombines(List<ValidationCombine> validationCombines) {
        this.validationCombines = validationCombines;
    }

    public void addError(ValidationCombine validationCombine){
            getValidationCombines().add(validationCombine);
    }

    public String getMessage(){
        StringBuilder sb=new StringBuilder();
        sb.append("Il y a des erreur dans votre Formulaire");
        for (ValidationCombine v: validationCombines){
            sb.append(v.getMessage());
        }
        return sb.toString();
    }
}
