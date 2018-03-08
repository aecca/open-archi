package com.araguacaima.open_archi.persistence.asm;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractReliability extends BaseEntity implements IReliability {
    @Column
    private Boolean contemplated;
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Markdown description;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Collection<Documentation> documentationList;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
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