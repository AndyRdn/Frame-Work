package com.Utils;

import com.Annotation.Authentification;
import com.Annotation.Param;
import com.Annotation.PublicMethode;
import com.Annotation.valide.Length;
import com.Annotation.valide.Numeric;
import com.Annotation.valide.Range;
import com.Annotation.valide.Requiered;
import com.Mapping.CustomFile;
import com.Mapping.CustomSession;
import com.Mapping.VerbAction;
import com.exception.FieldException;
import com.exception.ValidationCombine;
import com.exception.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public static Object execMethode(Object zavatra, VerbAction verbActions, HttpServletRequest request,String user_key) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ServletException, IOException {
//        System.out.println(methodeName);
        Method[] methods=zavatra.getClass().getMethods();
        Object val=null;
        List<String> error=new ArrayList<>();
        for (Method method : methods){
            if (method.getName().equalsIgnoreCase(verbActions.getAction())) {
                Parameter[] params=method.getParameters();
                List<Object> paramval=new ArrayList<>();


                CustomSession session=CustomSession.HttpToCustomeSession(request.getSession());

                //check l'autorisation
                if (method.isAnnotationPresent(PublicMethode.class)){
                    System.out.println("Public behh"+user_key);
                    String user=(String) session.get(user_key);
                    System.out.println("USER:"+user);
                }else if (zavatra.getClass().isAnnotationPresent(Authentification.class)){
                    String user=(String) session.get(user_key);
                    System.out.println("USER:"+user);
                    if (!checkUser(user,zavatra.getClass())){
                        throw new IllegalAccessException("Authentification incorrecte");
                    }
                }
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
                                FieldException fieldException=new FieldException();
                                for (Field field : fields) {
                                    System.out.println(field.getName());
                                    if (request.getParameter(param.getAnnotation(Param.class).name() + "." + field.getName()) != null) {
                                        Object paramValue = Reflect.cast(field.getType(), request.getParameter(param.getAnnotation(Param.class).name() + "." + field.getName()));
                                        System.out.println("ckeck");
                                        System.out.println(request.getParameter(param.getAnnotation(Param.class).name() + "." + field.getName()));

                                        try{
                                            checkParam(paramValue, field);
                                            Method temp = clazzz.getClass().getMethod("set" + capitalize(field.getName()), field.getType());
                                            temp.invoke(clazzz, paramValue);
                                            System.out.println("niditra");
                                        }catch (ValidationCombine v){
                                            System.out.println("error");
                                            fieldException.addError(v);
                                        }

                                    }
                                }
                                if (!fieldException.getValidationCombines().isEmpty()) request.setAttribute("ListError", fieldException);
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

    public static boolean checkParam(Object object,Field field) throws ValidationCombine {
        List<ValidationException> validationExceptions=new ArrayList<>();
        if (field.isAnnotationPresent(Numeric.class)){
            try{
                Integer.parseInt(object.toString());
            }catch (Exception e){
                validationExceptions.add(new ValidationException("Une Case doit etre Numeric",field.getName()));
            }
        }
        if (field.isAnnotationPresent(Length.class)){
            if (object.toString().length()>field.getAnnotation(Length.class).max()) validationExceptions.add(new ValidationException("Limite Depasser",field.getName()));;
        }
        if (field.isAnnotationPresent(Range.class)){
            if (Double.parseDouble(object.toString())>field.getAnnotation(Range.class).max() || Double.parseDouble(object.toString())<field.getAnnotation(Range.class).min()) validationExceptions.add(new ValidationException("Range non respecter",field.getName()));
        }
        if (field.isAnnotationPresent(Requiered.class)){
            System.out.println(object);
            if (object==null || object.toString().equals("")){
                System.out.println("null ve");
                validationExceptions.add(new ValidationException("La case est null",field.getName()));;
            }
        }

        if (!validationExceptions.isEmpty()) throw new ValidationCombine(validationExceptions);

        return true;
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

    public static boolean checkUser(String userRole, Class<?> clazz) {
        // 1. Vérifier si la classe a l'annotation
        if (!clazz.isAnnotationPresent(Authentification.class)) {
            return false; // Politique : accès refusé si l'annotation est absente
        }

        // 2. Récupérer les rôles autorisés
        Authentification auth = clazz.getAnnotation(Authentification.class);
        String[] authorizedRoles = auth.roles();

        // 3. Comparer le rôle utilisateur avec la liste
        for (String role : authorizedRoles) {
            if (userRole.equals(role)) {
                return true;
            }
        }

        return false; // Aucun match trouvé
    }

}
