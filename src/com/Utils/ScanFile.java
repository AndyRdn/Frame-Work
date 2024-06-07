package com.Utils;

import com.Annotation.Get;
import com.Annotation.ToController;
import com.Mapping.Mapping;
import jakarta.servlet.ServletException;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
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
                                if (method.isAnnotationPresent(Get.class)) {
                                    System.out.println(method.getAnnotation(Get.class).url());
                                    if (!analise.containsKey(method.getAnnotation(Get.class).url())){
                                        analise.put(method.getAnnotation(Get.class).url(),new Mapping(packageName+"."+className,method.getName()));
                                    }else {
                                        throw new ServletException("UrlDoublant");
                                    }
                                }
                            }
                        } else {
    //                        out.println("tsy izy");
                        }
                    }catch (ClassNotFoundException e){
                        continue;
                    }

                }
            }
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
    }
}
