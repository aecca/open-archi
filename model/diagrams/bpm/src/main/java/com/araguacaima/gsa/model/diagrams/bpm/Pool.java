package com.araguacaima.gsa.model.diagrams.bpm;

import java.util.Collection;

public class Pool {

    private Collection<Lane> lanes;

    public Collection<Lane> getLanes() {
        return lanes;
    }

    public void setLanes(Collection<Lane> lanes) {
        this.lanes = lanes;
    }
}
