package com.araguacaima.open_archi.persistence.asm;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Map;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "ASM", name = "TechnicalSolution")
@DynamicUpdate
public class TechnicalSolution extends BaseEntity {

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private ArchitectSolutionModel architectSolutionModel;
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @CollectionTable(name = "TechnicalSolution_Impacts",
            schema = "ASM")
    @MapKeyColumn(name = "impacts")
    private Map<String, Markdown> impacts;
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private IntermediateSolution intermediate;
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private ProposedSolution proposed;
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private ReliabilitySolution reliability;
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
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
