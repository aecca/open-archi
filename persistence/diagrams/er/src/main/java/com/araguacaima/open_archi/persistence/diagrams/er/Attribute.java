package com.araguacaima.open_archi.persistence.diagrams.er;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "Attribute", schema = "DIAGRAMS")
@DynamicUpdate
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


    public void override(Attribute source, boolean keepMeta) {
        super.override(source, keepMeta);
        this.setName(source.getName());
        this.setType(source.getType());
        this.setKey(source.isKey());
    }

    public void copyNonEmpty(Attribute source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getName() != null) {
            this.setName(source.getName());
        }
        if (source.getType() != null) {
            this.setType(source.getType());
        }
        this.setKey(source.isKey());
    }
}
