package com.Mapping;

import com.Annotation.Param;
import com.Utils.Reflect;
import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;
import java.net.http.HttpRequest;

public class Mapping {
    String className;
    String methodeName;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodeName() {
        return methodeName;
    }

    public void setMethodeName(String methodeName) {
        this.methodeName = methodeName;
    }

    public Mapping(String className, String methodeName) {
        this.className = className;
        this.methodeName = methodeName;

    }
    public Object execMethode(HttpServletRequest request) throws Exception {
        System.out.println(className);
        Object clazzz = Class.forName(className).getDeclaredConstructor().newInstance();
        return Reflect.execMethode(clazzz, methodeName, request);
    }

}
