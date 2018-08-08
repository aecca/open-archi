package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.*;

import java.util.LinkedHashSet;
import java.util.Set;

public class Palette implements Palettable {

    private Set<PaletteItem> elements = new LinkedHashSet<>();

    private static PaletteKind kind = PaletteKind.ARCHITECTURE;

    public Palette() {
        PaletteItem element = new PaletteItem();
        Shape shape = new Shape();
        shape.setFill("#0000FF");
        shape.setStroke("transparent");
        shape.setSize(new Size(25, 15));
        shape.setType(ElementKind.DEFAULT);
        element.setName("New Element");
        element.setShape(shape);
        element.setRank(0);
        element.setPrototype(false);
        elements.add(element);
        PaletteItem person = new PaletteItem();
        person.setName("Person");
        Shape shapePerson = new Shape();
        shapePerson.setFill("#ED5656");
        shapePerson.setStroke("transparent");
        shapePerson.setSize(new Size(30, 30));
        shapePerson.setType(ElementKind.PERSON);
        person.setShape(shapePerson);
        person.setKind(ElementKind.CONSUMER);
        person.setRank(1);
        person.setPrototype(false);
        elements.add(person);
        PaletteItem consumer = new PaletteItem();
        Shape shapeConsumer = new Shape();
        shapeConsumer.setFill("#F0AD4B");
        shapeConsumer.setStroke("transparent");
        shapeConsumer.setSize(new Size(15, 15));
        shapeConsumer.setType(ElementKind.CONSUMER);
        consumer.setName("Consumer");
        consumer.setShape(shapeConsumer);
        consumer.setKind(ElementKind.CONSUMER);
        consumer.setRank(2);
        consumer.setPrototype(false);
        elements.add(consumer);
    }

    @Override
    public PaletteKind getType() {
        return kind;
    }

    @Override
    public Set<PaletteItem> getElements() {
        return elements;
    }

    public void setElements(Set<PaletteItem> elements) {
        this.elements = elements;
    }

    public void addElement(PaletteItem element) {
        this.elements.add(element);
    }


}
