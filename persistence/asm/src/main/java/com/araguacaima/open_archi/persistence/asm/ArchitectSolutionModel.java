package com.araguacaima.open_archi.persistence.asm;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Set;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "ASM", name = "ArchitectSolutionModel")
@DynamicUpdate
public class ArchitectSolutionModel extends BaseEntity {

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinTable(schema = "ASM",
            name = "ArchitectSolutionModel_Diagrams",
            joinColumns = {@JoinColumn(name = "ArchitectSolutionModel_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Diagram_Id",
                    referencedColumnName = "Id")})
    private Set<Diagram> diagrams;

    public Set<Diagram> getDiagrams() {
        return diagrams;
    }

    public void setDiagrams(Set<Diagram> diagrams) {
        this.diagrams = diagrams;
    }
}
