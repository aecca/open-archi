package com.araguacaima.gsa.persistence.diagrams.er;

import com.araguacaima.gsa.persistence.diagrams.core.Element;
import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;

import java.util.Collection;
import java.util.Set;

public class Model extends Element {

    public static final String CANONICAL_NAME_SEPARATOR = "#";
    private Collection<Entity> entities;
    private ElementKind kind = ElementKind.BPM_MODEL;

    public Collection<Entity> getEntities() {
        return entities;
    }

    public void setEntities(Collection<Entity> entities) {
        this.entities = entities;
    }

    @Override
    protected String getCanonicalNameSeparator() {
        return CANONICAL_NAME_SEPARATOR;
    }

    @Override
    public ElementKind getKind() {
        return kind;
    }

    @Override
    public void setKind(ElementKind kind) {
        this.kind = kind;
    }

    @Override
    protected Set<String> getRequiredTags() {
        return null;
    }

}
