package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "SM",
        name = "Objectives")
public class Objectives extends BaseEntity {

    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Markdown currentSituation;
    @OneToMany
    @JoinTable(schema = "SM",
            name = "Objectives_Diagrams",
            joinColumns = {@JoinColumn(name = "Objectives_Id",
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
