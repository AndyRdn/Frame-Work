package com.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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
    public static Object execMethode(Object zavatra, String methodeName, Object params) throws Exception {
        System.out.println(methodeName);

        if (params!=null){
            Class classes=params.getClass();

            Method method=zavatra.getClass().getMethod(methodeName,classes);
            return method.invoke(zavatra,params);
        }else {
            Method method = zavatra.getClass().getMethod(methodeName, null);
            return method.invoke(zavatra, null);
        }

    }
}
