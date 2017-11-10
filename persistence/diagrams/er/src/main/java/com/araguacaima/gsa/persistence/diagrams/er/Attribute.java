package com.araguacaima.gsa.persistence.diagrams.er;

import com.araguacaima.gsa.persistence.meta.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

@Entity
@PersistenceUnit(unitName = "gsa")
@Table(name = "Attribute", schema = "DIAGRAMS")
public class Attribute extends BaseEntity {
    @Column
    private String name;
    @Column
    private String type;
    @Column
    private boolean key;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isKey() {
        return key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
