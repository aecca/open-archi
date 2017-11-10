package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "gsa")
@Table(schema = "MSA",
        name = "ProposedSolution")
public class ProposedSolution extends BaseEntity {

    @OneToOne(cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST})
    private Markdown description;
    @OneToMany
    @JoinTable(schema = "MSA",
            name = "ProposedSolution_Functional_Diagrams",
            joinColumns = {@JoinColumn(name = "ProposedSolution_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Diagram_Id",
                    referencedColumnName = "Id")})
    private Collection<Diagram> functionals;
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
