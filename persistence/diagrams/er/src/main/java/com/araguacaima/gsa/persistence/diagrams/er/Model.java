package com.araguacaima.gsa.persistence.diagrams.er;

import com.araguacaima.gsa.persistence.diagrams.core.Element;
import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;

import javax.persistence.*;
import java.util.Collection;

@javax.persistence.Entity
@PersistenceUnit(unitName = "diagrams")
@Table(name = "ER_Model", schema = "DIAGRAMS")
public class Model extends Element<Model> {

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Model_Entities",
            joinColumns = {@JoinColumn(name = "Entity_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Entity_Id",
                    referencedColumnName = "Id")})
    private Collection<Entity> entities;
    @Column
    private ElementKind kind = ElementKind.ENTITY_RELATIONSHIP_MODEL;

    public Collection<Entity> getEntities() {
        return entities;
    }

    public void setEntities(Collection<Entity> entities) {
        this.entities = entities;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }
}
