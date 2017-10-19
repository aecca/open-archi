package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceContext(unitName = "gsa")
@Table(schema = "MSA",
        name = "ArchitectSolutionModel")
public class ArchitectSolutionModel extends BaseEntity {

    @OneToMany
    @JoinTable(schema = "MSA",
            name = "ArchitectSolutionModel_Diagrams",
            joinColumns = {@JoinColumn(name = "Diagram_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Diagram_Id",
                    referencedColumnName = "Id")})
    private Collection<Diagram> diagrams;

    public Collection<Diagram> getDiagrams() {
        return diagrams;
    }

    public void setDiagrams(Collection<Diagram> diagrams) {
        this.diagrams = diagrams;
    }
}
