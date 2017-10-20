package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;

import java.util.Collection;

public class ProposedSolution extends BaseEntity {

    private Markdown description;

    private Collection<Diagram> functionals;

    private TechnicalSolution technicals;

    public Markdown getDescription() {
        return description;
    }

    public void setDescription(Markdown description) {
        this.description = description;
    }

    public Collection<Diagram> getFunctionals() {
        return functionals;
    }

    public void setFunctionals(Collection<Diagram> functionalSolutions) {
        this.functionals = functionalSolutions;
    }

    public TechnicalSolution getTechnicals() {
        return technicals;
    }

    public void setTechnicals(TechnicalSolution technicalSolution) {
        this.technicals = technicalSolution;
    }

}
