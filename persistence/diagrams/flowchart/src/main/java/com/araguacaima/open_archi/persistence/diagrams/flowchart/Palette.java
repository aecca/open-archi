package com.araguacaima.open_archi.persistence.diagrams.flowchart;

import com.araguacaima.open_archi.persistence.diagrams.core.*;

import java.util.LinkedHashSet;
import java.util.Set;

public class Palette implements Palettable {

    private Set<PaletteItem> basicElements = new LinkedHashSet<>();
    private static PaletteKind kind = PaletteKind.FLOWCHART;

    public Palette() {
        PaletteItem box = new PaletteItem();
        box.setShapeType(ShapeType.ROUNDED_BOX);
        box.setRank(0);
        box.setName("Element");
        box.setKind(ElementKind.FLOWCHART);
        basicElements.add(box);
        PaletteItem start = new PaletteItem();
        start.setShapeType(ShapeType.CIRCLE);
        start.setRank(1);
        start.setName("Start");
        start.setKind(ElementKind.FLOWCHART);
        basicElements.add(start);
        PaletteItem end = new PaletteItem();
        end.setShapeType(ShapeType.CIRCLE);
        end.setRank(2);
        end.setName("End");
        end.setKind(ElementKind.FLOWCHART);
        basicElements.add(end);
        PaletteItem diamond = new PaletteItem();
        diamond.setShapeType(ShapeType.DIAMOND);
        diamond.setRank(3);
        diamond.setName("?");
        diamond.setKind(ElementKind.FLOWCHART);
        basicElements.add(diamond);
    }

    @Override
    public PaletteKind getType() {
        return this.kind;
    }

    @Override
    public Set<PaletteItem> getBasicElements() {
        return basicElements;
    }

    public void setBasicElements(Set<PaletteItem> basicElements) {
        this.basicElements = basicElements;
    }

}
