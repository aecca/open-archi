package com.araguacaima.open_archi.persistence.diagrams.core;

import java.util.Set;

public class PaletteItem extends PaletteInfo implements Comparable<PaletteItem> {

    private int rank;

    private String description;

    private Shape shape;

    private Set<ConnectTrigger> canBeConnectedFrom;

    private Set<ConnectTrigger> canBeConnectedTo;

    private boolean prototype;

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public Set<ConnectTrigger> getCanBeConnectedFrom() {
        return canBeConnectedFrom;
    }

    public void setCanBeConnectedFrom(Set<ConnectTrigger> canBeConnectedFrom) {
        this.canBeConnectedFrom = canBeConnectedFrom;
    }

    public Set<ConnectTrigger> getCanBeConnectedTo() {
        return canBeConnectedTo;
    }

    public void setCanBeConnectedTo(Set<ConnectTrigger> canBeConnectedTo) {
        this.canBeConnectedTo = canBeConnectedTo;
    }

    public boolean isPrototype() {
        return prototype;
    }

    public void setPrototype(boolean prototype) {
        this.prototype = prototype;
    }

    @Override
    public int compareTo(PaletteItem o) {
        if (o == null) {
            return 0;
        }
        if (o.getKind().equals(ElementKind.DEFAULT)) {
            return -1000;
        } else if (o.getKind().equals(ElementKind.CONSUMER)) {
            return -999;
        } else if (o.getKind().equals(ElementKind.ARCHITECTURE_MODEL)) {
            return -998;
        } else if (o.getKind().equals(ElementKind.LAYER)) {
            return -997;
        } else if (o.getKind().equals(ElementKind.SYSTEM)) {
            return -996;
        } else if (o.getKind().equals(ElementKind.CONTAINER)) {
            return -995;
        } else if (o.getKind().equals(ElementKind.COMPONENT)) {
            return -994;
        } else if (o.getKind().equals(this.getKind())) {
            return o.getRank() - this.getRank();
        }
        return 1;
    }
}
