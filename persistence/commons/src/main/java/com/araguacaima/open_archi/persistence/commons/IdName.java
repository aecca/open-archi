package com.araguacaima.open_archi.persistence.commons;

public class IdName implements Comparable<IdName> {

    private String id;
    private String name;
    private Class clazz;
    private String kind;

    public IdName(String id, String name, Class clazz) {
        this.id = id;
        this.name = name;
        this.clazz = clazz;
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

    @Override
    public int compareTo(IdName idName) {
        if (idName == null) return -1;
        if (clazz.getName().compareTo(idName.clazz.getName()) >= 0) {
            if (kind.compareTo(idName.getKind()) >= 0) {
                return name.compareTo(idName.getName());
            } else {
                return kind.compareTo(idName.getKind());
            }
        } else {
            return clazz.getName().compareTo(idName.clazz.getName());
        }
    }
}
