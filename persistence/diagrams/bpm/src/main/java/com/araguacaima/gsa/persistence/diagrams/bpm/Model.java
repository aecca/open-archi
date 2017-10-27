package com.araguacaima.gsa.persistence.diagrams.bpm;

import com.araguacaima.gsa.persistence.diagrams.core.Element;
import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;

import java.util.Collection;
import java.util.Set;

public class Model extends Element {

    private Collection<Pool> pools;
    private ElementKind kind = ElementKind.BPM_MODEL;

    public Collection<Pool> getPools() {
        return pools;
    }

    public void setPools(Collection<Pool> pools) {
        this.pools = pools;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }
}
