package com.araguacaima.open_archi.persistence.diagrams.core;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import java.util.Collection;

public interface DiagramableElement<T> {

    ElementKind getKind();

    void setKind(ElementKind kind);

    Collection<BaseEntity> override(T source, boolean keepMeta, String suffix, CompositeElement clonedFrom);

    Collection<BaseEntity> copyNonEmpty(T source, boolean keepMeta);
}
