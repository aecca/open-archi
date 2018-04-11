package com.araguacaima.open_archi.persistence.diagrams.core;

public interface DiagramableElement<T> {

    ElementKind getKind();

    void setKind(ElementKind kind);

    void override(T source, boolean keepMeta, String suffix);

    void copyNonEmpty(T source, boolean keepMeta);
}
