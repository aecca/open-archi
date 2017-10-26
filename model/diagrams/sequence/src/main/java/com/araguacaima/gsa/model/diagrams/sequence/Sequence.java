package com.araguacaima.gsa.model.diagrams.sequence;

import com.araguacaima.gsa.model.diagrams.core.ElementKind;
import com.araguacaima.gsa.model.diagrams.core.Item;

import java.util.Set;

public class Sequence extends Item {

    public static final String CANONICAL_NAME_SEPARATOR = ".";
    private int start;
    private int duration;
    private ElementKind kind = ElementKind.SEQUENCE_MODEL;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    protected String getCanonicalNameSeparator() {
        return CANONICAL_NAME_SEPARATOR;
    }

    @Override
    public ElementKind getKind() {
        return kind;
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
