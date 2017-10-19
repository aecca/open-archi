package com.araguacaima.gsa.model.msa;

import java.util.Collection;

public abstract class AbstractReliability implements IReliability {

    private Boolean contemplated;

    private Markdown description;

    private Collection<String> documentationList;

    private Long id;

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
    public Collection<String> getDocumentationList() {
        return documentationList;
    }

    @Override
    public void setDocumentationList(Collection<String> documentationList) {
        this.documentationList = documentationList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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