package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.meta.BaseEntity;

import java.util.Collection;

public class IntermediateSolution extends BaseEntity {

    private Markdown description;

    private Collection<Diagram> functionals;

    private Collection<TechnicalDebt> technicalDebts;

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

    public Collection<TechnicalDebt> getTechnicalDebts() {
        return technicalDebts;
    }

    public void setTechnicalDebts(Collection<TechnicalDebt> technicalDebts) {
        this.technicalDebts = technicalDebts;
    }

    public TechnicalSolution getTechnicals() {
        return technicals;
    }

    public void setTechnicals(TechnicalSolution technicalSolution) {
        this.technicals = technicalSolution;

    }
}
