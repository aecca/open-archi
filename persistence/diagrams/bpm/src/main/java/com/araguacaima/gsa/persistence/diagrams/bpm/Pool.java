package com.araguacaima.gsa.persistence.diagrams.bpm;

import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;
import com.araguacaima.gsa.persistence.diagrams.core.Item;

import java.util.Collection;
import java.util.Set;

public class Pool extends Item {

    private Collection<Lane> lanes;
    private ElementKind kind;

    public Collection<Lane> getLanes() {
        return lanes;
    }

    public void setLanes(Collection<Lane> lanes) {
        this.lanes = lanes;
    }
    @Override
    protected String getCanonicalNameSeparator() {
        return null;
    }

    @Override
    public ElementKind getKind() {
        return this.kind;
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
