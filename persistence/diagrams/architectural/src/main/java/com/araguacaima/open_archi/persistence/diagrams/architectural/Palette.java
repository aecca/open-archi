package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.*;

public class Palette extends AbstractPalette {

    private static PaletteKind kind = PaletteKind.ARCHITECTURE;

    public Palette() {
        PaletteItem element = new PaletteItem();
        Shape shape = new Shape();
        shape.setFill("#0000FF");
        shape.setStroke("transparent");
        shape.setType(ElementKind.DEFAULT);
        element.setName("New Element");
        element.setShape(shape);
        element.setRank(0);
        element.setPrototype(false);
        addBasicElement(element);
        PaletteItem person = new PaletteItem();
        person.setName("Person");
        Shape shapePerson = new Shape();
        shapePerson.setFill("#ED5656");
        shapePerson.setStroke("transparent");
        shapePerson.setType(ElementKind.PERSON);
        person.setShape(shapePerson);
        person.setKind(ElementKind.CONSUMER);
        person.setRank(1);
        person.setPrototype(false);
        addBasicElement(person);
        PaletteItem consumer = new PaletteItem();
        Shape shapeConsumer = new Shape();
        shapeConsumer.setFill("#F0AD4B");
        shapeConsumer.setStroke("transparent");
        shapeConsumer.setType(ElementKind.CONSUMER);
        consumer.setName("Consumer");
        consumer.setShape(shapeConsumer);
        consumer.setKind(ElementKind.CONSUMER);
        consumer.setRank(2);
        consumer.setPrototype(false);
        addBasicElement(consumer);
    }

    @Override
    public PaletteKind getType() {
        return kind;
    }

}
