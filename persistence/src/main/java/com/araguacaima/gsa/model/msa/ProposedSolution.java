package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceContext(unitName = "gsa")
@Table(schema = "MSA",
        name = "ProposedSolution")
public class ProposedSolution extends BaseEntity {

    @OneToOne
    private Markdown description;
    @OneToMany
    @JoinTable(schema = "MSA",
            name = "ProposedSolution_Functional_Diagrams",
            joinColumns = {@JoinColumn(name = "Diagram_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Diagram_Id",
                    referencedColumnName = "Id")})
    private Collection<Diagram> functionals;
    @OneToOne
    private TechnicalSolution technicals;
    @OneToOne
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
