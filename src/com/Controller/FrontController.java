package com.Controller;

import com.Annotation.Get;
import com.Annotation.ToController;
import com.Mapping.Mapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FrontController extends HttpServlet {
    HashMap<String , Mapping> analise=new HashMap<>();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        procesRquest(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        procesRquest(req,resp);
    }

    protected void procesRquest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        String packageName = this.getInitParameter("controller_package");
        String urlweb=req.getRequestURI();

        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".", "/"));
            // Convertir l'URL en chemin de fichier
            File src = new File(URLDecoder.decode(url.getFile(), "UTF-8"));
            // Lire tous les fichiers dans le répertoire
            for (File p : src.listFiles()) {
                if (p.getName().endsWith(".class")) {
                    String className = p.getName().replace(".class", "");
                    // Charger la classe
                    Class<?> clazz = Class.forName(packageName + "." + className);
                    // Vérifier si la classe a l'annotation
                    if (clazz.isAnnotationPresent(ToController.class)) {
                        Method[] methods = clazz.getMethods();
                        // Parcourir les méthodes et vérifier si elles ont l'annotation @Get
                        for (Method method : methods) {
                            if (method.isAnnotationPresent(Get.class)) {
                                analise.put(method.getAnnotation(Get.class).url(),new Mapping(className,method.getName()));
                            }
                        }
                    } else {
//                        out.println("tsy izy");
                    }
                }
            }
            if (analise.containsKey(urlweb)){
                out.println(urlweb);
                out.println(analise.get(urlweb).getMethodeName());
                out.println(analise.get(urlweb).getClassName());
            }else {
                out.println(urlweb);
                out.println("pas de methode associer");
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


}
