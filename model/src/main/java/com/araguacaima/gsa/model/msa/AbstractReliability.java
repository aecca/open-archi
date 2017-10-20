package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;

import java.util.Collection;

public abstract class AbstractReliability extends BaseEntity implements IReliability {

    private Boolean contemplated;

    private Markdown description;

    private Collection<Documentation> documentationList;

    private ReliabilitySolution reliabilitySolution;

    private Boolean standardSolution;

    @Override
    public Boolean getContemplated() {
        return contemplated;
    }

    @Override
    public void setContemplated(Boolean contemplated) {
        this.contemplated = contemplated;
    }

    @Override
    public Markdown getDescription() {
        return description;
    }

    @Override
    public void setDescription(Markdown description) {
        this.description = description;
    }

    @Override
    public Collection<Documentation> getDocumentationList() {
        return documentationList;
    }

    @Override
    public void setDocumentationList(Collection<Documentation> documentationList) {
        this.documentationList = documentationList;
    }

    public ReliabilitySolution getReliabilitySolution() {
        return reliabilitySolution;
    }

    public void setReliabilitySolution(ReliabilitySolution reliabilitySolution) {
        this.reliabilitySolution = reliabilitySolution;
    }

    @Override
    public Boolean getStandardSolution() {
        return standardSolution;
    }

    @Override
    public void setStandardSolution(Boolean standardSolution) {
        this.standardSolution = standardSolution;
    }

}