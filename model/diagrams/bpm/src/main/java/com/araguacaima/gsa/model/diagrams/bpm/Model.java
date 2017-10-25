package com.araguacaima.gsa.model.diagrams.bpm;

import com.araguacaima.gsa.model.diagrams.core.Element;
import com.araguacaima.gsa.model.diagrams.core.ElementKind;
import com.araguacaima.gsa.model.diagrams.core.MetaData;

import java.util.Collection;
import java.util.Set;

public class Model extends Element {

    public static final String CANONICAL_NAME_SEPARATOR = "#";
    private Collection<Pool> pools;
    private ElementKind kind = ElementKind.BPM_MODEL;
    private MetaData metaData;

    public Collection<Pool> getPools() {
        return pools;
    }

    public void setPools(Collection<Pool> pools) {
        this.pools = pools;
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
