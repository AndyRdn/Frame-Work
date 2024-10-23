package com.Utils;

import com.Annotation.Param;
import com.Mapping.CustomFile;
import com.Mapping.CustomSession;
import com.Mapping.VerbAction;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

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
    public static Object execMethode(Object zavatra, VerbAction verbActions, HttpServletRequest request) throws Exception {
//        System.out.println(methodeName);
        Method[] methods=zavatra.getClass().getMethods();
        Object val=null;
        for (Method method : methods){
            if (method.getName().equalsIgnoreCase(verbActions.getAction())) {
                Parameter[] params=method.getParameters();
                List<Object> paramval=new ArrayList<>();
                CustomSession session=CustomSession.HttpToCustomeSession(request.getSession());
                for (Parameter param : params) {

                    if (param.isAnnotationPresent(Param.class)) {

                        if (isPrimitive(param.getType().getSimpleName())){
                            String paramValue= request.getParameter(param.getAnnotation(Param.class).name());
                            paramval.add(paramValue);
                        }else {
                            System.out.println("Object");
                            Object clazzz = param.getType().getDeclaredConstructor().newInstance();
                            if (param.getType().equals(CustomFile.class)){
                                Part file= request.getPart(param.getAnnotation(Param.class).name());
                                System.out.println(file.getName());
                                CustomFile cFile= new CustomFile(file);
                                paramval.add(cFile);
                            }else {
                                Field[] fields=clazzz.getClass().getDeclaredFields();
                                for (int i = 0; i <fields.length ; i++) {
                                    Field field=fields[i];
                                    if (request.getParameter(param.getAnnotation(Param.class).name()+"."+field.getName())!= null){
                                        Object paramValue= Reflect.cast(field.getType() , request.getParameter(param.getAnnotation(Param.class).name()+"."+field.getName()));
                                        Method temp=clazzz.getClass().getMethod("set"+capitalize(field.getName()), field.getType());
                                        temp.invoke(clazzz,paramValue);
                                    }
                                }
                                paramval.add(clazzz);
                            }


                        }

                    }else if (param.getType().equals(CustomSession.class)){

                        paramval.add(session);
                    }else {
                        throw new ServletException("Param inexistant");
                    }

                }
                System.out.println("execute");
                Object result= method.invoke(zavatra, paramval.toArray());
                session.customeToHttpSession(request.getSession());
                return result;
            }
        }
        return val;
    }

    public static Object cast(Class clazz,Object params) {
        if (params == null) {
            throw new IllegalArgumentException("Parameter cannot be null");
        }

        switch (clazz.getSimpleName()) {
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

    public static boolean isPrimitive(String nameClass){
        return switch (nameClass) {
            case "Integer", "int", "double", "Double", "String" -> true;
            default -> false;
        };
    }

    public static String capitalize(String original){
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

}
