package com.araguacaima.open_archi.persistence.diagrams.architectural;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;

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
@PersistenceUnit(unitName = "open-archi")
public class Components extends StaticElements {

    @Column
    private String technology;

    @Column
    private long size;

    public Components() {
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

    public void override(Components source) {
        super.override(source);
        this.setTechnology(source.getTechnology());
        this.setSize(source.getSize());
    }

    public void copyNonEmpty(Components source) {
        super.copyNonEmpty(source);
        if (source.getTechnology() != null) {
            this.setTechnology(source.getTechnology());
        }
        if (source.getSize() != 0) {
            this.setSize(source.getSize());
        }
    }
}