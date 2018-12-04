package com.araguacaima.open_archi.persistence.diagrams.classes;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "UmlParameter", schema = "DIAGRAMS")
@DynamicUpdate
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


    public void override(UmlParameter source, boolean keepMeta, String suffix) {
        super.override(source, keepMeta, suffix);
        this.name = source.getName();
        this.type = source.getType();
    }

    public void copyNonEmpty(UmlParameter source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getName() != null) {
            this.name = source.getName();
        }
        if (source.getType() != null) {
            this.type = source.getType();
        }
    }

}
