package com.araguacaima.open_archi.persistence.diagrams.core;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "Connector", schema = "DIAGRAMS")
@DynamicUpdate
public class Connector extends BaseEntity {

    @Column
    @Enumerated(EnumType.STRING)
    private ElementKind type;
    @Column
    private String stroke = "#333333";

    public ElementKind getType() {
        return type;
    }

    public void setType(ElementKind type) {
        this.type = type;
    }

    public String getStroke() {
        return stroke;
    }

    public void setStroke(String stroke) {
        this.stroke = stroke;
    }

    public void override(Connector source, boolean keepMeta, String suffix) {
        super.override(source, keepMeta, suffix);
        this.setType(source.getType());
        this.setStroke(source.getStroke());
    }

    public void copyNonEmpty(Connector source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getType() != null) {
            this.setType(source.getType());
        }
        if (source.getStroke() != null) {
            this.setStroke(source.getStroke());
        }
    }
}
