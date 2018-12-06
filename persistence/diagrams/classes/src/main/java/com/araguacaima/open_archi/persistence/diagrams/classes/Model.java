package com.araguacaima.open_archi.persistence.diagrams.classes;

import com.araguacaima.open_archi.persistence.diagrams.core.*;
import com.araguacaima.open_archi.persistence.diagrams.core.Relationship;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@PersistenceUnit(unitName = "open-archi")
@DiscriminatorValue(value = "ClassesModel")
public class Model extends ModelElement implements DiagramableElement<Model> {

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinTable(schema = "DIAGRAMS",
            name = "Classes_Model_Relationships",
            joinColumns = {@JoinColumn(name = "Classes_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Relationship_Id",
                    referencedColumnName = "Id")})
    private Set<Relationship> relationships = new LinkedHashSet<>();
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Model_Classes",
            joinColumns = {@JoinColumn(name = "Classes_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Class_Id",
                    referencedColumnName = "Id")})
    private Collection<UmlClass> classes;

    public Model() {
        setKind(ElementKind.UML_CLASS_MODEL);
    }

    public Collection<UmlClass> getClasses() {
        return classes;
    }

    public void setClasses(Collection<UmlClass> classes) {
        this.classes = classes;
    }

    public Set<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(Set<Relationship> relationships) {
        this.relationships = relationships;
    }

    @Override
    public Collection<BaseEntity> override(Model source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.override(source, keepMeta, suffix, clonedFrom));
        this.setRelationships(source.getRelationships());
        this.setClasses(source.getClasses());
        return overriden;
    }

    @Override
    public Collection<BaseEntity> copyNonEmpty(Model source, boolean keepMeta) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.copyNonEmpty(source, keepMeta));
        if (source.getRelationships() != null && !source.getRelationships().isEmpty()) {
            this.setRelationships(source.getRelationships());
        }
        if (source.getClasses() != null && !source.getClasses().isEmpty()) {
            this.setClasses(source.getClasses());
        }
        return overriden;
    }
}
