package com.araguacaima.gsa.model.diagrams.sequence;

import com.araguacaima.gsa.model.diagrams.core.Element;
import com.araguacaima.gsa.model.diagrams.core.ElementKind;

import java.util.Collection;
import java.util.Set;

public class Model extends Element {

    public static final String CANONICAL_NAME_SEPARATOR = "/";
    private Collection<Sequence> sequence;
    private ElementKind kind = ElementKind.SEQUENCE_MODEL;

    public Collection<Sequence> getSequence() {
        return sequence;
    }

    public void setSequence(Collection<Sequence> sequence) {
        this.sequence = sequence;
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
