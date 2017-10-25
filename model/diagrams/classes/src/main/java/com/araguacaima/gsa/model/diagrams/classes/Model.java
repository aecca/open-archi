package com.araguacaima.gsa.model.diagrams.classes;

import com.araguacaima.gsa.model.diagrams.core.Element;
import com.araguacaima.gsa.model.diagrams.core.ElementKind;

import java.util.Collection;
import java.util.Set;

public class Model extends Element {

    public static final String CANONICAL_NAME_SEPARATOR = "#";
    private Collection<UmlClass> classes;
    private ElementKind kind = ElementKind.BPM_MODEL;

    public Collection<UmlClass> getClasses() {
        return classes;
    }

    public void setClasses(Collection<UmlClass> classes) {
        this.classes = classes;
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
