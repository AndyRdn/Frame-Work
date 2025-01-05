package com.Utils;

import com.Annotation.*;
import com.Mapping.Mapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.net.http.HttpRequest;
import java.util.HashMap;

public class ScanFile {

    public static void scanner(String packageName, HashMap analise) throws ServletException {
        File src=null;
        try {
            if (packageName.isEmpty()){
                throw new ServletException("Controller Introuvable");
            }
            URL url = Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".", "/"));
            // Convertir l'URL en chemin de fichier
            src = new File(URLDecoder.decode(url.getFile(), "UTF-8"));

            // Lire tous les fichiers dans le répertoire
            for (File p : src.listFiles()) {
                if (p.getName().endsWith(".class")) {
                    String className = p.getName().replace(".class", "");
                    try{
                        // Charger la classe
                        Class<?> clazz = Class.forName(packageName + "." + className);
                        // Vérifier si la classe a l'annotation
                        if (clazz.isAnnotationPresent(ToController.class)) {
                            Method[] methods = clazz.getMethods();
                            // Parcourir les méthodes et vérifier si elles ont l'annotation @Get
                            for (Method method : methods) {
                                System.out.println(methods.length);
                                System.out.println(method.getAnnotation(Url.class).url());

                                if (method.isAnnotationPresent(Post.class)){
                                    System.out.println(method.getAnnotation(Url.class).url());
                                    if (analise.containsKey(method.getAnnotation(Url.class).url())){
                                        Mapping temp=(Mapping) analise.get(method.getAnnotation(Url.class).url());
                                        temp.addVerbActions(method.getName(),"POST");
                                    }else {
                                        analise.put(method.getAnnotation(Url.class).url(),new Mapping(packageName+"."+className,method.getName(),"POST"));
                                    }
                                }else {
                                    if (method.isAnnotationPresent(Restapi.class)) {
                                        System.out.println("Rest beee");
                                        analise.put("Restapi",new Mapping(packageName+"."+className,method.getName(),"GET"));

                                    }else if (analise.containsKey(method.getAnnotation(Url.class).url())){
                                        Mapping temp=(Mapping) analise.get(method.getAnnotation(Url.class).url());
                                        temp.addVerbActions(method.getName(),"GET");
                                    }else {
                                        analise.put(method.getAnnotation(Url.class).url(),new Mapping(packageName+"."+className,method.getName(),"GET"));
                                    }
                                }
                            }
                        }else {
                            throw new Exception("Url Inconnue");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
    }
}
