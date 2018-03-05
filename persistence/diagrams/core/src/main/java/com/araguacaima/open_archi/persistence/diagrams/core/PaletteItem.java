package com.araguacaima.open_archi.persistence.diagrams.core;

import org.springframework.stereotype.Component;

@Component
public class PaletteItem extends PaletteInfo {

    protected ShapeType shapeType;
    protected int rank;
    protected Size size = new Size();
    protected String fill = "#ffffff";
    protected String stroke = "#333333";
    protected boolean input = true;
    protected boolean output = true;

    public ShapeType getShapeType() {
        return shapeType;
    }

    public void setShapeType(ShapeType shapeType) {
        this.shapeType = shapeType;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        this.fill = fill;
    }

    public String getStroke() {
        return stroke;
    }

    public void setStroke(String stroke) {
        this.stroke = stroke;
    }

    public boolean isInput() {
        return input;
    }

    public void setInput(boolean input) {
        this.input = input;
    }

    public boolean isOutput() {
        return output;
    }

    public void setOutput(boolean output) {
        this.output = output;
    }
}
