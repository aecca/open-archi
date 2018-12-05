package com.araguacaima.open_archi.persistence.diagrams.core;

public class
ElementShapeWrapper {
    public static Shape toShape(ElementShape elementShape) {
        Shape shape = new Shape();
        shape.setKey(elementShape.getKey());
        shape.setId(elementShape.getId());
        shape.setMeta(elementShape.getMeta());
        shape.setType(elementShape.getType());
        shape.setFill(elementShape.getFill());
        shape.setStroke(elementShape.getStroke());
        shape.setInput(elementShape.isInput());
        shape.setOutput(elementShape.isOutput());
        shape.setFigure(elementShape.getFigure());
        return shape;
    }

    public static ElementShape toElementShape(Shape shape) {
        ElementShape elementShape = new ElementShape();
        elementShape.setKey(shape.getKey());
        elementShape.setId(shape.getId());
        elementShape.setMeta(shape.getMeta());
        elementShape.setType(shape.getType());
        elementShape.setFill(shape.getFill());
        elementShape.setStroke(shape.getStroke());
        elementShape.setInput(shape.isInput());
        elementShape.setOutput(shape.isOutput());
        elementShape.setFigure(shape.getFigure());
        return elementShape;
    }
}
