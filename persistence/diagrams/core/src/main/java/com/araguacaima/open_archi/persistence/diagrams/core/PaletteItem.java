package com.araguacaima.open_archi.persistence.diagrams.core;

import org.springframework.stereotype.Component;

@Component
public class PaletteItem extends PaletteInfo {

    protected ShapeType shapeType;
    protected int rank;

    public ShapeType getShapeType() {
        return shapeType;
    }

    public void setShapeType(ShapeType shapeType) {
        this.shapeType = shapeType;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
