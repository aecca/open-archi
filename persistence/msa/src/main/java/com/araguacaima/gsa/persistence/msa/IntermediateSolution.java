package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "gsa")
@Table(schema = "MSA",
        name = "IntermediateSolution")
public class IntermediateSolution extends BaseEntity {

    @OneToOne(cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST})
    private Markdown description;
    @OneToMany
    @JoinTable(schema = "MSA",
            name = "IntermediateSolution_Functional_Diagrams",
            joinColumns = {@JoinColumn(name = "IntermediateSolution_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Diagram_Id",
                    referencedColumnName = "Id")})
    private Collection<Diagram> functionals;
    @OneToMany
    @JoinTable(schema = "MSA",
            name = "IntermediateSolution_TechnicalDebts",
            joinColumns = {@JoinColumn(name = "IntermediateSolution_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "TechnicalDebt_Id",
                    referencedColumnName = "Id")})
    private Collection<TechnicalDebt> technicalDebts;
    @OneToOne(cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST})
    private TechnicalSolution technicals;
    @OneToOne(cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST})
    private Msa msa;

    public Markdown getDescription() {
        return description;
    }

    public void setDescription(Markdown description) {
        this.description = description;
    }

    public Collection<Diagram> getFunctionals() {
        return functionals;
    }

    public void setFunctionals(Collection<Diagram> functionalSolutions) {
        this.functionals = functionalSolutions;
    }

    public Collection<TechnicalDebt> getTechnicalDebts() {
        return technicalDebts;
    }

    public void setTechnicalDebts(Collection<TechnicalDebt> technicalDebts) {
        this.technicalDebts = technicalDebts;
    }

    public TechnicalSolution getTechnicals() {
        return technicals;
    }

    public void setTechnicals(TechnicalSolution technicalSolution) {
        this.technicals = technicalSolution;

    }

    public Msa getMsa() {
        return msa;
    }

    public void setMsa(Msa msa) {
        this.msa = msa;
    }
}
