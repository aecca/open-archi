package com.araguacaima.open_archi.persistence.diagrams.core;

import java.util.UUID;

public abstract class PaletteInfo {

    protected String id;
    protected String name;
    protected ElementKind kind;

    public PaletteInfo() {

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

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }
}
