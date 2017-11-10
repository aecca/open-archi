package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "gsa")
@Table(schema = "MSA",
        name = "SecurityControls")
public class SecurityControls extends BaseEntity {

    @OneToOne(cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST})
    private Markdown agileRiskAssessment;
    @OneToOne(cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST})
    private Markdown requirementsMeasures;
    @OneToOne(cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST})
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
