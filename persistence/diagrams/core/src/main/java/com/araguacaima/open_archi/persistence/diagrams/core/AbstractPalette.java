package com.araguacaima.open_archi.persistence.diagrams.core;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class AbstractPalette implements Palettable {

    private Set<PaletteItem> basicElements = new LinkedHashSet<>();
    private Set<PaletteItem> generalElements = new LinkedHashSet<>();

    @Override
    abstract public PaletteKind getType();

    @Override
    public Set<PaletteItem> getBasicElements() {
        return basicElements;
    }

    public void setBasicElements(Set<PaletteItem> basicElements) {
        this.basicElements = basicElements;
    }

    @Override
    public void addBasicElement(PaletteItem element) {
        this.basicElements.add(element);
    }

    @Override
    public Set<PaletteItem> getGeneralElements() {
        return generalElements;
    }

    public void setGeneralElements(Set<PaletteItem> generalElements) {
        this.generalElements = generalElements;
    }

    @Override
    public void addGeneralElement(PaletteItem element) {
        this.generalElements.add(element);
    }

}
