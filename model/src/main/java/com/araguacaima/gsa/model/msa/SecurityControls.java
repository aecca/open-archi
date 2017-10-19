package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;

public class SecurityControls extends BaseEntity {

    private Markdown agileRiskAssessment;

    private Markdown requirementsMeasures;

    private Markdown securityMeasures;

    public Markdown getAgileRiskAssessment() {
        return agileRiskAssessment;
    }

    public void setAgileRiskAssessment(Markdown agileRiskAssessment) {
        this.agileRiskAssessment = agileRiskAssessment;
    }

    public Markdown getRequirementsMeasures() {
        return requirementsMeasures;
    }

    public void setRequirementsMeasures(Markdown requirementsMeasures) {
        this.requirementsMeasures = requirementsMeasures;
    }

    public Markdown getSecurityMeasures() {
        return securityMeasures;
    }

    public void setSecurityMeasures(Markdown securityMeasures) {
        this.securityMeasures = securityMeasures;
    }
}
