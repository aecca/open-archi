package com.araguacaima.open_archi.persistence.asm;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import com.araguacaima.open_archi.persistence.persons.Responsible;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Collection;
import java.util.Map;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "ASM", name = "TechnicalDebt")
@DynamicUpdate
public class TechnicalDebt extends BaseEntity {

    @Column
    @Enumerated(EnumType.STRING)
    private Complexity complexity;
    @OneToMany
    @CollectionTable(name = "TechnicalDebt_Descriptions",
            schema = "ASM")
    @MapKeyColumn(name = "description")
    private Map<String, Markdown> description;
    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Effort estimatedEffort;
    @ManyToOne
    private IntermediateSolution intermediateSolution;
    @OneToMany
    @JoinTable(schema = "ASM",
            name = "TechnicalDebt_Responsibles",
            joinColumns = {@JoinColumn(name = "TechnicalDebt_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Responsible_Id",
                    referencedColumnName = "Id")})
    private Collection<Responsible> responsibles;
    @Column
    @Enumerated(EnumType.STRING)
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
