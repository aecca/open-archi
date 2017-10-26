package com.araguacaima.gsa.persistence.diagrams.gantt;

import com.araguacaima.gsa.persistence.diagrams.core.Element;
import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;
import com.araguacaima.gsa.persistence.diagrams.core.Relationship;

import java.util.Collection;
import java.util.Set;

public class Model extends Element {

    public static final String CANONICAL_NAME_SEPARATOR = "/";
    private Collection<Gantt> gantt;
    private ElementKind kind = ElementKind.GANTT_MODEL;

    public Collection<Gantt> getGantt() {
        return gantt;
    }

    public void setGantt(Collection<Gantt> gantt) {
        this.gantt = gantt;
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
