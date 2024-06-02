package com.Controller;

import com.Annotation.Get;
import com.Annotation.ToController;
import com.Mapping.Mapping;
import com.Mapping.ModelView;
import com.Utils.ScanFile;
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
            ScanFile.scanner(packageName,analise);
            if (analise.containsKey(urlweb)){
                if (analise.get(urlweb).execMethode() instanceof ModelView){
                    ModelView temp=(ModelView)analise.get(urlweb).execMethode();
                    for (String key : temp.getData().keySet()) {
                        req.setAttribute(key,temp.getData().get(key));
                    }
                    req.getRequestDispatcher(temp.getUrl()).forward(req,resp);
                }else {
                    System.out.println((String)analise.get(urlweb).execMethode());
                }
            }else {
                out.println("URl_Inconnue");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
