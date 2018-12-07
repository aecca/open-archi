package com.araguacaima.open_archi.persistence.diagrams.er;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;

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

    public Collection<BaseEntity> override(Attribute source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        super.override(source, keepMeta, suffix);
        this.setName(source.getName());
        this.setType(source.getType());
        this.setKey(source.isKey());
        return overriden;
    }

    public Collection<BaseEntity> copyNonEmpty(Attribute source, boolean keepMeta) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        super.copyNonEmpty(source, keepMeta);
        if (source.getName() != null) {
            this.setName(source.getName());
        }
        if (source.getType() != null) {
            this.setType(source.getType());
        }
        this.setKey(source.isKey());
        return overriden;
    }
}
