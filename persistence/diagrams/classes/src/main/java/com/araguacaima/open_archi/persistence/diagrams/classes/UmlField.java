package com.araguacaima.open_archi.persistence.diagrams.classes;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "UmlField", schema = "DIAGRAMS")
@DynamicUpdate
public class UmlField extends BaseEntity {

    @Column
    private String name;
    @Column
    private String type;
    @Column
    @Enumerated(EnumType.STRING)
    private Visibility visibility = Visibility.PACKAGE;

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

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }


    public void override(UmlField source, boolean keepMeta, String suffix) {
        super.override(source, keepMeta, suffix);
        this.name = source.getName();
        this.type = source.getType();
    }

    public void copyNonEmpty(UmlField source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getName() != null) {
            this.name = source.getName();
        }
        if (source.getType() != null) {
            this.type = source.getType();
        }
    }
}
