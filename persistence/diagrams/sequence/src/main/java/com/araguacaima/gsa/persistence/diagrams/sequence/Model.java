package com.araguacaima.gsa.persistence.diagrams.sequence;

import com.araguacaima.gsa.persistence.diagrams.core.Element;
import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;

import java.util.Collection;
import java.util.Set;

public class Model extends Element {

    private Collection<Sequence> sequence;
    private ElementKind kind = ElementKind.SEQUENCE_MODEL;

    public Collection<Sequence> getSequence() {
        return sequence;
    }

    public void setSequence(Collection<Sequence> sequence) {
        this.sequence = sequence;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }
}
