package com.araguacaima.open_archi.persistence.asm;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "ASM",
        name = "ArchitectSolutionModel")
public class ArchitectSolutionModel extends BaseEntity {

    @OneToMany
    @JoinTable(schema = "ASM",
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
