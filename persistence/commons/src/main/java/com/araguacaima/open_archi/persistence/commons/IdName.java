package com.araguacaima.open_archi.persistence.commons;

public class IdName {

    private String id;
    private String name;
    private Class clazz;
    private String kind;

    public IdName(String id, String name, Class clazz) {
        this.id = id;
        this.name = name;
        this.clazz = clazz;
    }

    public IdName(String id, String name, Class clazz, String kind) {
        this.id = id;
        this.name = name;
        this.clazz = clazz;
        this.kind = kind;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
