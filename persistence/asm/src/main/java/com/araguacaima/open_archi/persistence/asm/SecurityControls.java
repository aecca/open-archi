package com.araguacaima.open_archi.persistence.asm;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "ASM",
        name = "SecurityControls")
public class SecurityControls extends BaseEntity {

    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Markdown agileRiskAssessment;
    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Markdown requirementsMeasures;
    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
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
