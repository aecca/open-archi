package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.persistence.common.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceContext(unitName = "gsa")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractReliability extends BaseEntity implements IReliability {
    @Column
    private Boolean contemplated;
    @OneToOne
    private Markdown description;

    @OneToMany
    private Collection<Documentation> documentationList;

    @OneToOne
    private ReliabilitySolution reliabilitySolution;

    @Column
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

    @Override
    public ReliabilitySolution getReliabilitySolution() {
        return reliabilitySolution;
    }

    @Override
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