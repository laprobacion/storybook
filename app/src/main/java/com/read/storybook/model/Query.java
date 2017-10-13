package com.read.storybook.model;


import java.io.Serializable;

public class Query implements Serializable{
    private String query;
    private Serializable obj;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Serializable getObj() {
        return obj;
    }

    public void setObj(Serializable obj) {
        this.obj = obj;
    }
}
