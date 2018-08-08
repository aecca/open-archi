package com.araguacaima.open_archi.persistence.diagrams.flowchart;

import com.araguacaima.open_archi.persistence.diagrams.core.*;

import java.util.LinkedHashSet;
import java.util.Set;

public class Palette implements Palettable {

    private Set<PaletteItem> elements = new LinkedHashSet<>();
    private static PaletteKind kind = PaletteKind.FLOWCHART;

    public Palette() {
        PaletteItem box = new PaletteItem();
        box.setShape(new Shape(ElementKind.DEFAULT));
        box.setRank(0);
        box.setName("Element");
        box.setKind(ElementKind.FLOWCHART);
        elements.add(box);
        PaletteItem start = new PaletteItem();
        start.setShape(new Shape(ElementKind.INITIATOR));
        start.setRank(1);
        start.setName("Start");
        start.setKind(ElementKind.FLOWCHART);
        elements.add(start);
        PaletteItem end = new PaletteItem();
        end.setShape(new Shape(ElementKind.FINISHER));
        end.setRank(2);
        end.setName("End");
        end.setKind(ElementKind.FLOWCHART);
        elements.add(end);
        PaletteItem diamond = new PaletteItem();
        diamond.setShape(new Shape(ElementKind.CONDITION));
        diamond.setRank(3);
        diamond.setName("?");
        diamond.setKind(ElementKind.FLOWCHART);
        elements.add(diamond);
    }

    @Override
    public PaletteKind getType() {
        return this.kind;
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
