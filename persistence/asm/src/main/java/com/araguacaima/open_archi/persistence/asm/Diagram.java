package com.araguacaima.open_archi.persistence.asm;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "SM",
        name = "Diagram")
public class Diagram extends BaseEntity {

    @ManyToOne
    private ArchitectSolutionModel architectSolutionModel;
    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Markdown description;
    /*    @OneToOne
        @JoinColumn(table = "Architecture_Model", name = "Architecture_Model_Id", referencedColumnName = "Id")
        private Model diagram;*/
    @ManyToOne
    private IntermediateSolution intermediateSolution;
    @Column
    private String name;
    @ManyToOne
    private Objectives objectives;
    @ManyToOne
    private ProposedSolution proposedSolution;

    public ArchitectSolutionModel getArchitectSolutionModel() {
        return architectSolutionModel;
    }

    public void setArchitectSolutionModel(ArchitectSolutionModel architectSolutionModel) {
        this.architectSolutionModel = architectSolutionModel;
    }

    public Markdown getDescription() {
        return description;
    }

    public void setDescription(Markdown description) {
        this.description = description;
    }
/*
    public Model getDiagram() {
        return diagram;
    }

    public void setDiagram(Model diagram) {
        this.diagram = diagram;
    }*/

    public IntermediateSolution getIntermediateSolution() {
        return intermediateSolution;
    }

    public void setIntermediateSolution(IntermediateSolution intermediateSolution) {
        this.intermediateSolution = intermediateSolution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Objectives getObjectives() {
        return objectives;
    }

    public void setObjectives(Objectives objectives) {
        this.objectives = objectives;
    }

    public ProposedSolution getProposedSolution() {
        return proposedSolution;
    }

    public void setProposedSolution(ProposedSolution proposedSolution) {
        this.proposedSolution = proposedSolution;
    }
}
