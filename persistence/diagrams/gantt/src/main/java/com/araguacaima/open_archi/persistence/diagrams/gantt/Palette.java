package com.araguacaima.open_archi.persistence.diagrams.gantt;

import com.araguacaima.open_archi.persistence.diagrams.core.*;

import java.util.LinkedHashSet;
import java.util.Set;

public class Palette implements Palettable {

    private Set<PaletteItem> elements = new LinkedHashSet<>();
    private static PaletteKind kind = PaletteKind.GANTT;

    public Palette() {
        PaletteItem box = new PaletteItem();
        box.setShape(new Shape(ShapeType.Rectangle));
        box.setRank(0);
        box.setName("Feature");
        box.setKind(ElementKind.GANTT);
        elements.add(box);
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
