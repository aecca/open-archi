package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;

import java.util.Set;

public class ReliabilitySolution extends BaseEntity {

    private Markdown description;

    private Set<IReliability> elements;

    public Markdown getDescription() {
        return description;
    }

    public void setDescription(Markdown description) {
        this.description = description;
    }

    public Set<IReliability> getElements() {
        return elements;
    }

    public void setElements(Set<IReliability> elements) {
        this.elements = elements;
    }
}
