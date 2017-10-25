package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.meta.BaseEntity;

import java.util.Collection;

public class ArchitectSolutionModel extends BaseEntity {

    private Collection<Diagram> diagrams;

    public Collection<Diagram> getDiagrams() {
        return diagrams;
    }

    public void setDiagrams(Collection<Diagram> diagrams) {
        this.diagrams = diagrams;
    }
}
