package com.araguacaima.open_archi.persistence.diagrams.architectural;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;

import javax.persistence.*;

/**
 * The word "component" is a hugely overloaded term in the system development
 * industry, but in this context a component is simply a grouping of related
 * functionality encapsulated behind a well-defined interface. If you're using a
 * language like Java or C#, the simplest way to think of a component is that
 * it's a collection of implementation classes behind an interface. Aspects such
 * as how those components are packaged (e.g. one component vs many components
 * per JAR file, DLL, shared library, etc) is a separate and orthogonal concern.
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
@NamedQueries({
        @NamedQuery(name = Component.GET_ALL_COMPONENTS,
                query = "select a from Component a")        ,
        @NamedQuery(name = Component.GET_COMPONENTS_USAGE_BY_ELEMENT_ID_LIST,
                query = "select c from Component c where c.id in :" + Item.ELEMENTS_USAGE_PARAM)})
public class Component extends LeafStaticElement {


    public static final String GET_ALL_COMPONENTS = "get.all.components";
    public static final String GET_COMPONENTS_USAGE_BY_ELEMENT_ID_LIST = "get.components.usage.by.element.id.list";
    @Column
    private String technology;

    @Column
    private long size;

    public Component() {
        setKind(ElementKind.COMPONENT);
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

    public void override(Component source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        super.override(source, keepMeta, suffix, clonedFrom);
        this.setTechnology(source.getTechnology());
        this.setSize(source.getSize());
    }

    public void copyNonEmpty(Component source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getTechnology() != null) {
            this.setTechnology(source.getTechnology());
        }
        if (source.getSize() != 0) {
            this.setSize(source.getSize());
        }
    }

}