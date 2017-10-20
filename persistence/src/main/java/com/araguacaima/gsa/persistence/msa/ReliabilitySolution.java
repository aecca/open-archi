package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.persistence.common.BaseEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@PersistenceContext(unitName = "gsa")
@Table(schema = "MSA",
        name = "ReliabilitySolution")
public class ReliabilitySolution extends BaseEntity {

    @OneToOne
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
