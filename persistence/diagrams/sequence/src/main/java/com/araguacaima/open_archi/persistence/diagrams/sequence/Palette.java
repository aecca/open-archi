package com.araguacaima.open_archi.persistence.diagrams.sequence;

import com.araguacaima.open_archi.persistence.diagrams.core.*;

import java.util.LinkedHashSet;
import java.util.Set;

public class Palette extends AbstractPalette {

    private static PaletteKind kind = PaletteKind.SEQUENCE;

    public Palette() {
        PaletteItem box = new PaletteItem();
        box.setShape(new Shape(ElementKind.DEFAULT));
        box.setRank(0);
        box.setName("Activity");
        box.setKind(ElementKind.SEQUENCE);
        addBasicElement(box);
        PaletteItem target = new PaletteItem();
        target.setShape(new Shape(ElementKind.CONSUMER));
        target.setRank(1);
        target.setName("Actor");
        target.setKind(ElementKind.CONSUMER);
        addBasicElement(target);
    }

    @Override
    public PaletteKind getType() {
        return this.kind;
    }

}
