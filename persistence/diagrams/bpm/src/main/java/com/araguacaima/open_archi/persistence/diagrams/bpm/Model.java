package com.araguacaima.open_archi.persistence.diagrams.bpm;

import com.araguacaima.open_archi.persistence.diagrams.core.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@PersistenceUnit(unitName = "open-archi")
@DiscriminatorValue(value = "BpmModel")
public class Model extends ModelElement implements DiagramableElement<Model> {

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinTable(schema = "DIAGRAMS",
            name = "Bpm_Model_Relationships",
            joinColumns = {@JoinColumn(name = "Bpm_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Relationship_Id",
                    referencedColumnName = "Id")})
    private Set<Relationship> relationships = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.REMOVE)
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

    @Override
    public void override(Model source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        super.override(source, keepMeta, suffix, clonedFrom);
        for (Relationship consumer : source.getRelationships()) {
            Relationship newRelationship = new Relationship();
            newRelationship.override(consumer, keepMeta, suffix, clonedFrom);
            this.relationships.add(newRelationship);
        }
        for (Pool consumer : source.getPools()) {
            Pool newPool = new Pool();
            newPool.override(consumer, keepMeta, suffix, clonedFrom);
            this.pools.add(newPool);
        }
    }

    @Override
    public void copyNonEmpty(Model source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getRelationships() != null && !source.getRelationships().isEmpty()) {
            for (Relationship consumer : source.getRelationships()) {
                Relationship newRelationship = new Relationship();
                newRelationship.copyNonEmpty(consumer, keepMeta);
                this.relationships.add(newRelationship);
            }
        }
        if (source.getPools() != null && !source.getPools().isEmpty()) {
            for (Pool consumer : source.getPools()) {
                Pool newPool = new Pool();
                newPool.copyNonEmpty(consumer, keepMeta);
                this.pools.add(newPool);
            }
        }
    }
}
