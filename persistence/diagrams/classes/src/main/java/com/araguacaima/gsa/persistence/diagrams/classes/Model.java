package com.araguacaima.gsa.persistence.diagrams.classes;

import com.araguacaima.gsa.persistence.diagrams.core.Element;
import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;

import java.util.Collection;
import java.util.Set;

public class Model extends Element {

    private Collection<UmlClass> classes;
    private ElementKind kind = ElementKind.BPM_MODEL;

    public Collection<UmlClass> getClasses() {
        return classes;
    }

    public void setClasses(Collection<UmlClass> classes) {
        this.classes = classes;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }
}
