package com.Utils;

import com.Annotation.Param;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Reflect {
    public static String getClassName(Object zavatra){
        return zavatra.getClass().getSimpleName();
    }

    public static List<String> getAttributes(Object zavatra) throws NoSuchFieldException {
        List<String> tab= new ArrayList<>();
        for (Field elem :zavatra.getClass().getDeclaredFields()){
            tab.add(elem.getName().toString());
        }
        return tab;
    }

    public static Object execMethode(Object zavatra, String methodeName, Object[] params) throws Exception {
        if (params!=null){
            Class[] classes=new Class[params.length];
            for (int i = 0; i <params.length ; i++) {
                classes[i]=params[i].getClass();
            }
            Method method=zavatra.getClass().getMethod(methodeName,classes);
            return method.invoke(zavatra,params);
        }else {
            System.out.println(methodeName);
            Method method = zavatra.getClass().getMethod(methodeName, null);
            return method.invoke(zavatra, null);
        }

    }
    public static Object execMethode(Object zavatra, String methodeName, HttpServletRequest request) throws Exception {
        System.out.println(methodeName);
        Method[] methods=zavatra.getClass().getMethods();
        Object val=null;
        for (Method method : methods){
            if (method.getName().equalsIgnoreCase(methodeName)) {
                Parameter[] params=method.getParameters();
                for (Parameter param : params) {
                    if (param.isAnnotationPresent(Param.class)) {
                        String paramValue= request.getParameter(param.getAnnotation(Param.class).name());
                        System.out.println(paramValue);
                        val = method.invoke(zavatra, Reflect.cast(param.getType(),paramValue) );
                        return val;
                    }
                }
                System.out.println("tsisy");
                val = method.invoke(zavatra, null);
            }
        }
        return val;
    }

    public static Object cast(Class clazz,Object params) {
        if (params == null) {
            throw new IllegalArgumentException("Parameter cannot be null");
        }

        switch (clazz.getClass().getSimpleName()) {
            case "Integer", "int":
                return Integer.parseInt(params.toString());
            case "double","Double":
                return Double.parseDouble(params.toString());
            case "Date":
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return sdf.format((Date) params);
            default:
                return params.toString();
        }
    }

}
