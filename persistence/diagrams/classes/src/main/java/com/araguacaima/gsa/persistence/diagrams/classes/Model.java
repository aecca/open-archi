package com.araguacaima.gsa.persistence.diagrams.classes;

import com.araguacaima.gsa.persistence.diagrams.core.Element;
import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "gsa")
@DiscriminatorValue(value = "ClassesModel")
public class Model extends Element {

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Model_Classes",
            joinColumns = {@JoinColumn(name = "Classes_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Class_Id",
                    referencedColumnName = "Id")})
    private Collection<UmlClass> classes;
    @Column
    private ElementKind kind = ElementKind.BPM_MODEL;

    public Collection<UmlClass> getClasses() {
        return classes;
    }

    public void setClasses(Collection<UmlClass> classes) {
        this.classes = classes;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }
}
