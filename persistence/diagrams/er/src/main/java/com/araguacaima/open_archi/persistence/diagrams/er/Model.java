package com.araguacaima.open_archi.persistence.diagrams.er;

import com.araguacaima.open_archi.persistence.diagrams.core.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@javax.persistence.Entity
@PersistenceUnit(unitName = "open-archi")
@DiscriminatorValue(value = "ERModel")
public class Model extends ModelElement implements DiagramableElement<Model> {

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinTable(schema = "DIAGRAMS",
            name = "ER_Model_Relationships",
            joinColumns = {@JoinColumn(name = "ER_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Relationship_Id",
                    referencedColumnName = "Id")})
    private Set<Relationship> relationships = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Model_Entities",
            joinColumns = {@JoinColumn(name = "ER_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Entity_Id",
                    referencedColumnName = "Id")})
    private Collection<Entity> entities;

    public Model() {
        setKind(ElementKind.ENTITY_RELATIONSHIP_MODEL);
    }

    public Collection<Entity> getEntities() {
        return entities;
    }

    public void setEntities(Collection<Entity> entities) {
        this.entities = entities;
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
        this.setRelationships(source.getRelationships());
        this.setEntities(source.getEntities());
    }

    @Override
    public void copyNonEmpty(Model source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getRelationships() != null && !source.getRelationships().isEmpty()) {
            this.setRelationships(source.getRelationships());
        }
        if (source.getEntities() != null && !source.getEntities().isEmpty()) {
            this.setEntities(source.getEntities());
        }
    }
}
