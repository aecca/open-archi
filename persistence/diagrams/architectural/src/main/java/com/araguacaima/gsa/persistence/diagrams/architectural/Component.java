package com.araguacaima.gsa.persistence.diagrams.architectural;

import com.araguacaima.gsa.persistence.diagrams.core.Feature;
import com.araguacaima.gsa.persistence.diagrams.core.Item;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * The word "component" is a hugely overloaded term in the software development
 * industry, but in this context a component is simply a grouping of related
 * functionality encapsulated behind a well-defined interface. If you're using a
 * language like Java or C#, the simplest way to think of a component is that
 * it's a collection of implementation classes behind an interface. Aspects such
 * as how those components are packaged (e.g. one component vs many components
 * per JAR file, DLL, shared library, etc) is a separate and orthogonal concern.
 */
@Entity
@PersistenceUnit(unitName = "diagrams")
@Table(name = "Component", schema = "DIAGRAMS")
public class Component<T extends Item> extends StaticElement {

    @Column
    private String technology;

    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Component_Features",
            joinColumns = {@JoinColumn(name = "Feature_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Feature_Id",
                    referencedColumnName = "Id")})
    private Set<Feature> features = new HashSet<>();

    @Column
    private long size;

    public Component() {
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}