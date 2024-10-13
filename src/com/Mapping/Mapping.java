package com.Mapping;

import com.Annotation.Param;
import com.Utils.Reflect;
import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;
import java.net.http.HttpRequest;
import java.util.*;

public class Mapping {
    String className;
    Set<VerbAction> verbActions = new HashSet<>();

    public Set<VerbAction> getVerbActions() {
        return verbActions;
    }

    public void setVerbActions(Set<VerbAction> verbActions) {
        this.verbActions = verbActions;
    }

    public void addVerbActions(String action, String verb) {
        this.verbActions.add(new VerbAction(action, verb));
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Mapping(String className, String methodeName, String verb) {
        this.className = className;
        this.addVerbActions(methodeName, verb);
    }

    public boolean containsVerb(String verb) {
        for (VerbAction verbAction : verbActions) {
            if (verbAction.getVerb().equalsIgnoreCase(verb)) {
                return true;
            }
        }
        return false;
    }

    public VerbAction getByVerb(String verb){
        for (VerbAction verbAction: verbActions){
            if (verbAction.getVerb().equalsIgnoreCase(verb)) {
                return verbAction;
            }
        }
        return null;
    }
    public Object execMethode(HttpServletRequest request) throws Exception {
        System.out.println(className);
        Object clazzz = Class.forName(className).getDeclaredConstructor().newInstance();
        return Reflect.execMethode(clazzz, this.getByVerb(request.getMethod()), request);
    }

}
