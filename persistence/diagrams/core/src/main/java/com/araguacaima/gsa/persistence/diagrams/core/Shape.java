package com.araguacaima.gsa.persistence.diagrams.core;

import javax.persistence.Column;

public class Shape {

    @Column
    private ShapeType type;

    public ShapeType getType() {
        return type;
    }

    public void setType(ShapeType type) {
        this.type = type;
    }
}
