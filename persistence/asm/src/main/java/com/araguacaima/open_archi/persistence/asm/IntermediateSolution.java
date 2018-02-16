package com.araguacaima.open_archi.persistence.asm;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "SM",
        name = "IntermediateSolution")
public class IntermediateSolution extends BaseEntity {

    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Markdown description;
    @OneToMany
    @JoinTable(schema = "SM",
            name = "IntermediateSolution_Functional_Diagrams",
            joinColumns = {@JoinColumn(name = "IntermediateSolution_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Diagram_Id",
                    referencedColumnName = "Id")})
    private Collection<Diagram> functionals;
    @OneToMany
    @JoinTable(schema = "SM",
            name = "IntermediateSolution_TechnicalDebts",
            joinColumns = {@JoinColumn(name = "IntermediateSolution_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "TechnicalDebt_Id",
                    referencedColumnName = "Id")})
    private Collection<TechnicalDebt> technicalDebts;
    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private TechnicalSolution technicals;
    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Asm asm;

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

    public Asm getAsm() {
        return asm;
    }

    public void setAsm(Asm asm) {
        this.asm = asm;
    }
}
