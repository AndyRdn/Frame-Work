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
        Object clazzz=Class.forName(className).getDeclaredConstructor().newInstance();
        Method method=clazzz.getClass().getMethod(methodeName,null);
        if (method.isAnnotationPresent(Param.class)){
            Object params=request.getParameter(method.getAnnotation(Param.class).name());
            return  Reflect.execMethode(clazzz,methodeName,params);
        }else {
            return Reflect.execMethode(clazzz, methodeName, null);
        }
    }

    public Object execMethode(Object[] params) throws Exception {
        System.out.println(className);
        Object clazzz=Class.forName(className).getDeclaredConstructor().newInstance();
        return  Reflect.execMethode(clazzz,methodeName,params);
    }


}
