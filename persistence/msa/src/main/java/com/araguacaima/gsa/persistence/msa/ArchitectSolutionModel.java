package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.persistence.meta.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "SM",
        name = "ArchitectSolutionModel")
public class ArchitectSolutionModel extends BaseEntity {

    @OneToMany
    @JoinTable(schema = "SM",
            name = "ArchitectSolutionModel_Diagrams",
            joinColumns = {@JoinColumn(name = "ArchitectSolutionModel_Id",
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
