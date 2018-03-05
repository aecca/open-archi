package com.araguacaima.open_archi.persistence.diagrams.gantt;

import com.araguacaima.open_archi.persistence.diagrams.core.*;

import java.util.LinkedHashSet;
import java.util.Set;

public class Palette implements Palettable {

    private Set<PaletteItem> basicElements = new LinkedHashSet<>();
    private static PaletteKind kind = PaletteKind.GANTT;

    public Palette() {
        PaletteItem box = new PaletteItem();
        box.setShapeType(ShapeType.Rectangle);
        box.setRank(0);
        box.setName("Feature");
        box.setKind(ElementKind.GANTT);
        basicElements.add(box);
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
