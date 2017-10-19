package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;

import java.util.Map;

public class TechnicalSolution extends BaseEntity {

    private ArchitectSolutionModel architectSolutionModel;

    private Map<String, Markdown> impacts;

    private IntermediateSolution intermediate;

    private ProposedSolution proposed;

    private ReliabilitySolution reliability;

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
