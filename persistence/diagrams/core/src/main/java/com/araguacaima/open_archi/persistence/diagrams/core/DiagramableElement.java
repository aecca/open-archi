package com.araguacaima.open_archi.persistence.diagrams.core;

public interface DiagramableElement {

    ElementKind getKind();

    void setKind(ElementKind kind);
}
