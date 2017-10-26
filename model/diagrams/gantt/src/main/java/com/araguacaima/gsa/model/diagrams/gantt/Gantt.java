package com.araguacaima.gsa.model.diagrams.gantt;

import com.araguacaima.gsa.model.diagrams.core.Element;
import com.araguacaima.gsa.model.diagrams.core.ElementKind;
import com.araguacaima.gsa.model.diagrams.core.Item;

import java.util.Set;

public class Gantt extends Item {

    public static final String CANONICAL_NAME_SEPARATOR = "/";
    private Category category;
    private int start;
    private int end;
    private ElementKind kind = ElementKind.GANTT;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    protected String getCanonicalNameSeparator() {
        return CANONICAL_NAME_SEPARATOR;
    }

    @Override
    public ElementKind getKind() {
        return kind;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public void setKind(ElementKind kind) {
        this.kind = kind;
    }

    @Override
    protected Set<String> getRequiredTags() {
        return null;
    }
}
