package com.araguacaima.gsa.persistence.diagrams.bpm;

import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;
import com.araguacaima.gsa.persistence.diagrams.core.Item;

import java.util.Set;

public class Activity extends Item {

    private Lane lane;
    private ElementKind kind;

    public Lane getLane() {
        return lane;
    }

    public void setLane(Lane lane) {
        this.lane = lane;
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
