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
        element.setShapeType(ShapeType.RoundedRectangle);
        element.setRank(0);
        element.setName("Element");
        element.setFill("#0000FF");
        element.setSize(new Size(40,40));
        basicElements.add(element);
/*        PaletteItem person = new PaletteItem();
        person.setShapeType(ShapeType.Triangle);
        person.setRank(1);
        person.setName("Person");
        person.setFill("#ED5656");
        person.setSize(new Size(40,40));
        person.setKind(ElementKind.CONSUMER);
        basicElements.add(person);
        PaletteItem consumer = new PaletteItem();
        consumer.setShapeType(ShapeType.Diamond);
        consumer.setRank(2);
        consumer.setName("Consumer");
        consumer.setFill("#49CC90");
        consumer.setSize(new Size(40,40));
        consumer.setKind(ElementKind.CONSUMER);
        basicElements.add(consumer);*/
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
