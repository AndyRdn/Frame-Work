package com.Mapping;

import jakarta.servlet.http.HttpSession;

import java.util.Enumeration;
import java.util.HashMap;

public class CustomSession {
    HashMap<String,Object> session=new HashMap<>();

    public Object get(String key){
        return session.get(key);
    }

    public void add(String key, Object obj){
        session.put(key,obj);
    }

    public void remove(String key){
        session.remove(key);
    }

    public static CustomSession HttpToCustomeSession(HttpSession session) {
        CustomSession resultMap = new CustomSession();

        Enumeration<?> attrNames = session.getAttributeNames();
        while (attrNames.hasMoreElements()) {
            String attributeName = (String) attrNames.nextElement();
            Object attributeValue = session.getAttribute(attributeName);
            resultMap.add(attributeName, attributeValue);
            session.removeAttribute(attributeName);
        }

        return resultMap;
    }

    public void customeToHttpSession(HttpSession hsession) {
        for (String key : session.keySet()) {
            hsession.setAttribute(key, session.get(key));
        }
    }
}
