package com.araguacaima.open_archi.persistence.asm;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Set;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "ASM",
        name = "ReliabilitySolution")
public class ReliabilitySolution extends BaseEntity {

    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Markdown description;

    @OneToMany(targetEntity = AbstractReliability.class)
    private Set<IReliability> elements;

    public Markdown getDescription() {
        return description;
    }

    public void setDescription(Markdown description) {
        this.description = description;
    }

    public Set<IReliability> getElements() {
        return elements;
    }

    public void setElements(Set<IReliability> elements) {
        this.elements = elements;
    }
}
