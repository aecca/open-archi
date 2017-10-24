package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.meta.BaseEntity;
import com.araguacaima.gsa.model.persons.Responsible;

import java.util.Collection;
import java.util.Map;

public class TechnicalDebt extends BaseEntity {

    private Complexity complexity;

    private Map<String, Markdown> description;

    private Effort estimatedEffort;

    private IntermediateSolution intermediateSolution;

    private Collection<Responsible> responsibles;

    private TechnicalDebtScope scope;

    public Complexity getComplexity() {
        return complexity;
    }

    public void setComplexity(Complexity complexity) {
        this.complexity = complexity;
    }

    public Map<String, Markdown> getDescription() {
        return description;
    }

    public void setDescription(Map<String, Markdown> description) {
        this.description = description;
    }

    public Effort getEstimatedEffort() {
        return estimatedEffort;
    }

    public void setEstimatedEffort(Effort estimatedEffort) {
        this.estimatedEffort = estimatedEffort;
    }

    public IntermediateSolution getIntermediateSolution() {
        return intermediateSolution;
    }

    public void setIntermediateSolution(IntermediateSolution intermediateSolution) {
        this.intermediateSolution = intermediateSolution;
    }

    public Collection<Responsible> getResponsibles() {
        return responsibles;
    }

    public void setResponsibles(Collection<Responsible> responsibles) {
        this.responsibles = responsibles;
    }

    public TechnicalDebtScope getScope() {
        return scope;
    }

    public void setScope(TechnicalDebtScope scope) {
        this.scope = scope;
    }
}
