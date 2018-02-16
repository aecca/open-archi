package com.araguacaima.open_archi.persistence.diagrams.classes;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "UmlParameter", schema = "DIAGRAMS")
public class UmlParameter extends BaseEntity {

    @Column
    private String name;
    @Column
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
