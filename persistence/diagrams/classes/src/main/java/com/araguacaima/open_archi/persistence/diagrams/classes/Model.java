package com.araguacaima.open_archi.persistence.diagrams.classes;

import com.araguacaima.open_archi.persistence.diagrams.core.DiagramableElement;
import com.araguacaima.open_archi.persistence.diagrams.core.Element;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Relationship;

import javax.persistence.*;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@PersistenceUnit(unitName = "open-archi")
@DiscriminatorValue(value = "ClassesModel")
public class Model extends Element implements DiagramableElement {

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Classes_Model_Relationships",
            joinColumns = {@JoinColumn(name = "Classes_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Relationship_Id",
                    referencedColumnName = "Id")})
    private Set<Relationship> relationships = new LinkedHashSet<>();
    @OneToMany
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


    public void override(Model source) {
        super.override(source);
        this.setRelationships(source.getRelationships());
        this.setClasses(source.getClasses());
    }

    public void copyNonEmpty(Model source) {
        super.copyNonEmpty(source);
        if (source.getRelationships() != null && !source.getRelationships().isEmpty()) {
            this.setRelationships(source.getRelationships());
        }
        if (source.getClasses() != null && !source.getClasses().isEmpty()) {
            this.setClasses(source.getClasses());
        }
    }
}
