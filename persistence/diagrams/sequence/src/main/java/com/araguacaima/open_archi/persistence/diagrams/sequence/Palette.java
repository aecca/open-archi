package com.araguacaima.open_archi.persistence.diagrams.sequence;

import com.araguacaima.open_archi.persistence.diagrams.core.*;

import java.util.LinkedHashSet;
import java.util.Set;

public class Palette implements Palettable {

    private Set<PaletteItem> basicElements = new LinkedHashSet<>();
    private static PaletteKind kind = PaletteKind.SEQUENCE;

    public Palette() {
        PaletteItem box = new PaletteItem();
        box.setShapeType(ShapeType.Rectangle);
        box.setRank(0);
        box.setName("Activity");
        box.setKind(ElementKind.SEQUENCE);
        basicElements.add(box);
        PaletteItem target = new PaletteItem();
        target.setShapeType(ShapeType.Consumer);
        target.setRank(1);
        target.setName("Actor");
        target.setKind(ElementKind.CONSUMER);
        basicElements.add(target);
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