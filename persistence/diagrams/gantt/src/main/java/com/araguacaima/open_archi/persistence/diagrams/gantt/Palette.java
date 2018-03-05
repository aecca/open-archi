package com.araguacaima.open_archi.persistence.diagrams.gantt;

import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Palettable;
import com.araguacaima.open_archi.persistence.diagrams.core.PaletteItem;
import com.araguacaima.open_archi.persistence.diagrams.core.ShapeType;

import java.util.LinkedHashSet;
import java.util.Set;

public class Palette implements Palettable {

    private Set<PaletteItem> basicElements = new LinkedHashSet<>();

    public Palette() {
        PaletteItem box = new PaletteItem();
        box.setShapeType(ShapeType.BOX);
        box.setRank(0);
        box.setName("Feature");
        box.setKind(ElementKind.GANTT);
        basicElements.add(box);
    }

    @Override
    public Set<PaletteItem> getBasicElements() {
        return basicElements;
    }

    public void setBasicElements(Set<PaletteItem> basicElements) {
        this.basicElements = basicElements;
    }

}
