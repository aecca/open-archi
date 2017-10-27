package com.araguacaima.gsa.persistence.diagrams.gantt;

import com.araguacaima.gsa.persistence.diagrams.core.Element;
import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;

import java.util.Collection;
import java.util.Set;

public class Model extends Element {

    private Collection<Gantt> gantt;
    private ElementKind kind = ElementKind.GANTT_MODEL;

    public Collection<Gantt> getGantt() {
        return gantt;
    }

    public void setGantt(Collection<Gantt> gantt) {
        this.gantt = gantt;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }
}
