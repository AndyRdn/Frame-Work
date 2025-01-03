package com.Mapping;

import java.util.HashMap;

public class ModelView {
    String url;
    HashMap<String,Object> data=new HashMap<>();

    String Error;

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }
    public void add(String key,Object elemnt){
        this.getData().put(key,elemnt);
    }
}
