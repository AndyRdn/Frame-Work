package com.Mapping;

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
}
