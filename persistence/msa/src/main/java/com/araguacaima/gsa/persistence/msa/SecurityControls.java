package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.persistence.meta.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

@Entity
@PersistenceUnit(unitName = "msa")
@Table(schema = "MSA",
        name = "SecurityControls")
public class SecurityControls extends BaseEntity {

    @OneToOne
    private Markdown agileRiskAssessment;
    @OneToOne
    private Markdown requirementsMeasures;
    @OneToOne
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
