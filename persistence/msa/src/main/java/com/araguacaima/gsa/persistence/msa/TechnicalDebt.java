package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.persistence.meta.BaseEntity;
import com.araguacaima.gsa.persistence.persons.Responsible;

import javax.persistence.*;
import java.util.Collection;
import java.util.Map;

@Entity
@PersistenceUnit(unitName = "gsa" )
@Table(schema = "MSA",
        name = "TechnicalDebt")
public class TechnicalDebt extends BaseEntity {

    @Column
    private Complexity complexity;
    @OneToMany
    @CollectionTable(name = "TechnicalDebt_Descriptions",
            schema = "MSA")
    @MapKeyColumn(name = "description")
    private Map<String, Markdown> description;
    @OneToOne
    private Effort estimatedEffort;
    @ManyToOne
    private IntermediateSolution intermediateSolution;
    @OneToMany
    @JoinTable(schema = "MSA",
            name = "TechnicalDebt_Responsibles",
            joinColumns = {@JoinColumn(name = "Responsible_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Responsible_Id",
                    referencedColumnName = "Id")})
    private Collection<Responsible> responsibles;
    @Column
    private TechnicalDebtScope scope;

    public Complexity getComplexity() {
        return complexity;
    }

    public void setComplexity(Complexity complexity) {
        this.complexity = complexity;
    }

    public Map<String, Markdown> getDescription() {
        return description;
    }

    public void setDescription(Map<String, Markdown> description) {
        this.description = description;
    }

    public Effort getEstimatedEffort() {
        return estimatedEffort;
    }

    public void setEstimatedEffort(Effort estimatedEffort) {
        this.estimatedEffort = estimatedEffort;
    }

    public IntermediateSolution getIntermediateSolution() {
        return intermediateSolution;
    }

    public void setIntermediateSolution(IntermediateSolution intermediateSolution) {
        this.intermediateSolution = intermediateSolution;
    }

    public Collection<Responsible> getResponsibles() {
        return responsibles;
    }

    public void setResponsibles(Collection<Responsible> responsibles) {
        this.responsibles = responsibles;
    }

    public TechnicalDebtScope getScope() {
        return scope;
    }

    public void setScope(TechnicalDebtScope scope) {
        this.scope = scope;
    }
}
