package com.araguacaima.gsa.persistence.diagrams.core;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@PersistenceUnit(unitName = "gsa")
@Table(name = "CompositeElement", schema = "DIAGRAMS")
public class CompositeElement<T extends ElementKind> {

    @Id
    @NotNull
    @Column(name = "Id")
    protected String id;

    @Column
    @Type(type = "com.araguacaima.gsa.persistence.diagrams.core.ElementKind")
    protected T type;

    @Column
    protected String link;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T getType() {
        return type;
    }

    public void setType(T type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
