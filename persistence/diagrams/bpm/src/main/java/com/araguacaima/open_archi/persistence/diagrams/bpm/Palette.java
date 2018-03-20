package com.araguacaima.open_archi.persistence.diagrams.bpm;

import com.araguacaima.open_archi.persistence.diagrams.core.*;

import java.util.LinkedHashSet;
import java.util.Set;

public class Palette implements Palettable {

    private Set<PaletteItem> basicElements = new LinkedHashSet<>();
    private static PaletteKind kind = PaletteKind.BPM;

    public Palette() {
        PaletteItem box = new PaletteItem();
        box.setShape(new Shape(ShapeType.RoundedRectangle));
        box.setRank(0);
        box.setName("Activity");
        box.setKind(ElementKind.BPM);
        basicElements.add(box);
        PaletteItem start = new PaletteItem();
        start.setShape(new Shape(ShapeType.Ellipse));
        start.setRank(1);
        start.setName("Start");
        start.setKind(ElementKind.BPM);
        basicElements.add(start);
        PaletteItem end = new PaletteItem();
        end.setShape(new Shape(ShapeType.Ellipse));
        end.setRank(2);
        end.setName("End");
        end.setKind(ElementKind.BPM);
        basicElements.add(end);
        PaletteItem diamond = new PaletteItem();
        diamond.setShape(new Shape(ShapeType.Diamond));
        diamond.setRank(3);
        diamond.setName("?");
        diamond.setKind(ElementKind.BPM);
        basicElements.add(diamond);
        PaletteItem lane = new PaletteItem();
        lane.setShape(new Shape(ShapeType.Lane));
        lane.setRank(4);
        lane.setName("Lane");
        lane.setKind(ElementKind.BPM);
        basicElements.add(lane);
        PaletteItem pool = new PaletteItem();
        pool.setShape(new Shape(ShapeType.Pool));
        pool.setRank(5);
        pool.setName("Pool");
        pool.setKind(ElementKind.BPM);
        basicElements.add(pool);
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
