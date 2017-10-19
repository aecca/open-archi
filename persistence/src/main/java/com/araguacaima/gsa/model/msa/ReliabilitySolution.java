package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(schema = "Msa",
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
