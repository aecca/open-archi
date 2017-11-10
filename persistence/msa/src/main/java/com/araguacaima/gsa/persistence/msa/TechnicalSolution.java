package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Map;

@Entity
@PersistenceUnit(unitName = "gsa")
@Table(schema = "MSA",
        name = "TechnicalSolution")
public class TechnicalSolution extends BaseEntity {

    @OneToOne(cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST})
    private ArchitectSolutionModel architectSolutionModel;
    @OneToMany
    @CollectionTable(name = "TechnicalSolution_Impacts",
            schema = "MSA")
    @MapKeyColumn(name = "impacts")
    private Map<String, Markdown> impacts;
    @OneToOne(cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST})
    private IntermediateSolution intermediate;
    @OneToOne(cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST})
    private ProposedSolution proposed;
    @OneToOne(cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST})
    private ReliabilitySolution reliability;
    @OneToOne(cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST})
    private SecurityControls securityControls;

    public ArchitectSolutionModel getArchitectSolutionModel() {
        return architectSolutionModel;
    }

    public void setArchitectSolutionModel(ArchitectSolutionModel architectSolutionModel) {
        this.architectSolutionModel = architectSolutionModel;
    }

    public Map<String, Markdown> getImpacts() {
        return impacts;
    }

    public void setImpacts(Map<String, Markdown> impacts) {
        this.impacts = impacts;
    }

    public IntermediateSolution getIntermediate() {
        return intermediate;
    }

    public void setIntermediate(IntermediateSolution intermediateSolution) {
        this.intermediate = intermediateSolution;
    }

    public ProposedSolution getProposed() {
        return proposed;
    }

    public void setProposed(ProposedSolution proposedSolutionSolution) {
        this.proposed = proposedSolutionSolution;
    }

    public ReliabilitySolution getReliability() {
        return reliability;
    }

    public void setReliability(ReliabilitySolution reliabilitySolution) {
        this.reliability = reliabilitySolution;
    }

    public SecurityControls getSecurityControls() {
        return securityControls;
    }

    public void setSecurityControls(SecurityControls securityControls) {
        this.securityControls = securityControls;
    }
}
