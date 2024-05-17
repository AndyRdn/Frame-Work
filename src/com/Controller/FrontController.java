package com.Controller;

import com.Annotation.ToController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class FrontController extends HttpServlet {
    boolean check=false;
    List<String> controllername;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        procesRquest(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        procesRquest(req,resp);
    }

    protected void procesRquest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out=resp.getWriter();
        if (!check){
            String packageName=this.getInitParameter("controller_package");
            controllername=new ArrayList<>();
            try {
                URL url=Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".","/"));
                // Convertir l'URL en chemin de fichier
                File src = new File(URLDecoder.decode(url.getFile(),"UTF-8"));
                // Lire tous les fichiers dans le répertoire
                for (File p : src.listFiles()){
                    if (p.getName().endsWith(".class")){
                        String className = p.getName().replace(".class", "");
                        out.println();
                        // Charger la classe
                        Class<?> clazz = Class.forName(packageName+"."+className);
                        // Vérifier si la classe a l'annotation
                        if (clazz.isAnnotationPresent(ToController.class)) {
                            controllername.add(className);
                        }else {
                            out.println("tsy izy");
                        }
                    }
                }
                check=true;
                out.println(controllername.toString());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else {
            out.println(controllername.toString());
        }
    }
}
