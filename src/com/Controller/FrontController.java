package com.Controller;

import com.Annotation.Get;
import com.Annotation.ToController;
import com.Mapping.Mapping;
import com.Mapping.ModelView;
import com.Utils.LocalDateString;
import com.Utils.ScanFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ietf.jgss.GSSContext;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FrontController extends HttpServlet {
    HashMap<String , Mapping> analise=new HashMap<>();

    @Override
    public void init() throws ServletException {
        String packageName = this.getInitParameter("controller_package");
        ScanFile.scanner(packageName,analise);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            procesRquest(req,resp);
        } catch (ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            procesRquest(req,resp);
        } catch (ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }

    protected void procesRquest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException, ClassNotFoundException {
        PrintWriter out = resp.getWriter();
        String urlweb=req.getRequestURI();
            try {
                if (analise.containsKey(urlweb)) {
                    System.out.println(req.getMethod());
                    System.out.println("verbe:"+analise.get(urlweb).getVerb());

                    if (!analise.get(urlweb).getVerb().equalsIgnoreCase(req.getMethod())){
                        throw new Exception("Methode invalide");
                    }
                    if (analise.get(urlweb).execMethode(req) instanceof ModelView) {
                        ModelView temp = (ModelView) analise.get(urlweb).execMethode(req);
                        for (String key : temp.getData().keySet()) {
                            req.setAttribute(key, temp.getData().get(key));
                        }
                        req.getRequestDispatcher(temp.getUrl()).forward(req, resp);
                    } else if (analise.get(urlweb).execMethode(req) instanceof String){
                        System.out.println((String) analise.get(urlweb).execMethode(req));
                    }else {
                        throw new ServletException("type de retour Inconnue");
                    }
                } else if (analise.containsKey("Restapi")) {
                    System.out.println(req.getProtocol());
                    if (!analise.get(urlweb).getVerb().equalsIgnoreCase(req.getMethod())){
                        throw new Exception("Methode invalide");
                    }
                    System.out.println("execRest");
                    Gson gson=new GsonBuilder()
                            .registerTypeAdapter(LocalDate.class,new LocalDateString())
                            .create();

                    if (analise.get("Restapi").execMethode(req) instanceof ModelView) {
                        ModelView temp = (ModelView) analise.get("Restapi").execMethode(req);
                        out.println(gson.toJson(temp.getData()));
                    }else {
                        Object val=analise.get("Restapi").execMethode(req);
                        out.println(gson.toJson(val));
                    }
                } else {
                    throw new ServletException("URl_Inconnue");
                }
            }catch (Exception e){
                throw new ServletException(e.getMessage());
            }

    }


}
