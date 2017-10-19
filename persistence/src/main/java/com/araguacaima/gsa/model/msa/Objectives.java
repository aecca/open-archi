package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceContext(unitName = "gsa")
@Table(schema = "MSA",
        name = "Objectives")
public class Objectives extends BaseEntity {

    @OneToOne
    private Markdown currentSituation;
    @OneToMany
    @JoinTable(schema = "MSA",
            name = "Objectives_Diagrams",
            joinColumns = {@JoinColumn(name = "Diagram_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Diagram_Id",
                    referencedColumnName = "Id")})
    private Collection<Diagram> diagrams;

    public Markdown getCurrentSituation() {
        return currentSituation;
    }

    public void setCurrentSituation(Markdown currentSituation) {
        this.currentSituation = currentSituation;
    }

    public Collection<Diagram> getDiagrams() {
        return diagrams;
    }

    public void setDiagrams(Collection<Diagram> currentSituationDiagrams) {
        this.diagrams = currentSituationDiagrams;
    }
}
