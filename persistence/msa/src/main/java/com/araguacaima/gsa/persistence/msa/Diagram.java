package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.persistence.diagrams.architectural.Model;
import com.araguacaima.gsa.persistence.meta.BaseEntity;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "msa")
@Table(schema = "MSA",
        name = "Diagram")
public class Diagram extends BaseEntity {

    @ManyToOne
    private ArchitectSolutionModel architectSolutionModel;
    @OneToOne
    private Markdown description;
    @OneToOne
    private Model diagram;
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

    public Model getDiagram() {
        return diagram;
    }

    public void setDiagram(Model diagram) {
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
