package com.Mapping;

import com.Utils.Reflect;

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
    public String execMethode() throws Exception {
        System.out.println(className);
        Object clazzz=Class.forName(className).getDeclaredConstructor().newInstance();
        return (String)Reflect.execMethode(clazzz,methodeName,null);
    }
}
