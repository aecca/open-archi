package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;

import java.util.Collection;

public class Objectives extends BaseEntity {

    private Markdown currentSituation;

    public Markdown getCurrentSituation() {
        return currentSituation;
    }

    public void setCurrentSituation(Markdown currentSituation) {
        this.currentSituation = currentSituation;
    }
}
