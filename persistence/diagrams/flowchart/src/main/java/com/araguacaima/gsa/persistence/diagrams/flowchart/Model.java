package com.araguacaima.gsa.persistence.diagrams.flowchart;

import com.araguacaima.gsa.persistence.diagrams.core.Element;
import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;

import java.util.Collection;
import java.util.Set;

public class Model extends Element {

    private Collection<Flowchart> flowchart;
    private ElementKind kind = ElementKind.FLOWCHART_MODEL;

    public Collection<Flowchart> getFlowchart() {
        return flowchart;
    }

    public void setFlowchart(Collection<Flowchart> flowchart) {
        this.flowchart = flowchart;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }
}
