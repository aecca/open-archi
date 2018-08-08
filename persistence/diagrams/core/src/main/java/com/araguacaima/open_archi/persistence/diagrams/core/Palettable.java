package com.araguacaima.open_archi.persistence.diagrams.core;

import java.util.Set;

public interface Palettable {

    PaletteKind getType();

    Set<PaletteItem> getElements();

    void addElement(PaletteItem element);
}
