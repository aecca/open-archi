package com.araguacaima.open_archi.persistence.diagrams.gantt;

import com.araguacaima.open_archi.persistence.diagrams.core.*;

import java.util.LinkedHashSet;
import java.util.Set;

public class Palette extends AbstractPalette {

    private static PaletteKind kind = PaletteKind.GANTT;

    public Palette() {
        PaletteItem box = new PaletteItem();
        box.setShape(new Shape(ElementKind.DEFAULT));
        box.setRank(0);
        box.setName("Feature");
        box.setKind(ElementKind.GANTT);
        addBasicElement(box);
    }

    @Override
    public PaletteKind getType() {
        return this.kind;
    }

}
