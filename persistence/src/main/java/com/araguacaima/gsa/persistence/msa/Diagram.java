package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.persistence.common.BaseEntity;

import javax.persistence.*;

@Entity
@PersistenceContext(unitName = "gsa")
@Table(schema = "MSA",
        name = "Diagram")
public class Diagram extends BaseEntity {

    @ManyToOne
    private ArchitectSolutionModel architectSolutionModel;
    @OneToOne
    private Markdown description;
    @Column
    private Byte[] diagram;
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

    public Byte[] getDiagram() {
        return diagram;
    }

    public void setDiagram(Byte[] diagram) {
        this.diagram = diagram;
    }

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
