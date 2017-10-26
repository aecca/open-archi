package com.araguacaima.gsa.persistence.diagrams.flowchart;

import com.araguacaima.gsa.persistence.diagrams.core.Element;
import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;

import java.util.Collection;
import java.util.Set;

public class Model extends Element {

    public static final String CANONICAL_NAME_SEPARATOR = "#";
    private Collection<Flowchart> flowchart;
    private ElementKind kind = ElementKind.FLOWCHART_MODEL;

    public Collection<Flowchart> getFlowchart() {
        return flowchart;
    }

    public void setFlowchart(Collection<Flowchart> flowchart) {
        this.flowchart = flowchart;
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
