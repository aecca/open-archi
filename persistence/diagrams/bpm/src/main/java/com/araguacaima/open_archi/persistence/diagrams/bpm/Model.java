package com.araguacaima.open_archi.persistence.diagrams.bpm;

import com.araguacaima.open_archi.persistence.diagrams.core.DiagramableElement;
import com.araguacaima.open_archi.persistence.diagrams.core.Element;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;

import javax.persistence.*;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@PersistenceUnit(unitName = "open-archi")
@DiscriminatorValue(value = "BpmModel")
public class Model extends Element implements DiagramableElement {

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Bpm_Model_Relationships",
            joinColumns = {@JoinColumn(name = "Bpm_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Relationship_Id",
                    referencedColumnName = "Id")})
    private Set<Relationship> relationships = new LinkedHashSet<>();

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Model_Pools",
            joinColumns = {@JoinColumn(name = "Bpm_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Pool_Id",
                    referencedColumnName = "Id")})
    private Collection<Pool> pools;

    public Model() {
        setKind(ElementKind.BPM_MODEL);
    }

    public Collection<Pool> getPools() {
        return pools;
    }

    public void setPools(Collection<Pool> pools) {
        this.pools = pools;
    }

    public Set<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(Set<Relationship> relationships) {
        this.relationships = relationships;
    }
}
