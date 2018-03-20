package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.*;

import java.util.LinkedHashSet;
import java.util.Set;

public class Palette implements Palettable {

    private Set<PaletteItem> basicElements = new LinkedHashSet<>();
    private Set<PaletteItem> softwareSystems = new LinkedHashSet<>();
    private Set<PaletteItem> containers = new LinkedHashSet<>();
    private Set<PaletteItem> components = new LinkedHashSet<>();
    private static PaletteKind kind = PaletteKind.ARCHITECTURE;

    public Palette() {
        PaletteItem element = new PaletteItem();
        Shape shape = new Shape();
        shape.setFill("#0000FF");
        shape.setSize(new Size(40,40));
        shape.setType(ShapeType.RoundedRectangle);
        element.setName("Element");
        element.setShape(shape);
        element.setRank(0);
        basicElements.add(element);
        PaletteItem person = new PaletteItem();
        person.setName("Person");
        Shape shapePerson = new Shape();
        shapePerson.setFill("#ED5656");
        shapePerson.setSize(new Size(40,40));
        shapePerson.setType(ShapeType.Rectangle);
        person.setShape(shapePerson);
        person.setKind(ElementKind.CONSUMER);
        person.setRank(1);
        basicElements.add(person);
        PaletteItem consumer = new PaletteItem();
        Shape shapeConsumer = new Shape();
        shapeConsumer.setFill("#49CC90");
        shapeConsumer.setSize(new Size(40,40));
        shapeConsumer.setType(ShapeType.Ellipse);
        consumer.setName("Consumer");
        consumer.setShape(shapeConsumer);
        consumer.setKind(ElementKind.CONSUMER);
        consumer.setRank(2);
        basicElements.add(consumer);
    }

    @Override
    public PaletteKind getType() {
        return kind;
    }

    @Override
    public Set<PaletteItem> getBasicElements() {
        return basicElements;
    }

    public void setBasicElements(Set<PaletteItem> basicElements) {
        this.basicElements = basicElements;
    }

    public Set<PaletteItem> getSoftwareSystems() {
        return softwareSystems;
    }

    public void setSoftwareSystems(Set<PaletteItem> softwareSystems) {
        this.softwareSystems = softwareSystems;
    }

    public Set<PaletteItem> getContainers() {
        return containers;
    }

    public void setContainers(Set<PaletteItem> containers) {
        this.containers = containers;
    }

    public Set<PaletteItem> getComponents() {
        return components;
    }

    public void setComponents(Set<PaletteItem> components) {
        this.components = components;
    }
}
