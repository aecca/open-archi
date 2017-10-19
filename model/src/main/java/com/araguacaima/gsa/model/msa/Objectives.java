package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;

import java.util.Collection;

public class Objectives extends BaseEntity {

    private Markdown currentSituation;

    private Collection<Diagram> diagrams;

    public Markdown getCurrentSituation() {
        return currentSituation;
    }

    public void setCurrentSituation(Markdown currentSituation) {
        this.currentSituation = currentSituation;
    }

    public Collection<Diagram> getDiagrams() {
        return diagrams;
    }

    public void setDiagrams(Collection<Diagram> currentSituationDiagrams) {
        this.diagrams = currentSituationDiagrams;
    }
}
