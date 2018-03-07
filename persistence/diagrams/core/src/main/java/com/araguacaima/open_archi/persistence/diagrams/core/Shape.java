package com.araguacaima.open_archi.persistence.diagrams.core;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "Shape", schema = "DIAGRAMS")
@DynamicUpdate
public class Shape extends BaseEntity {

    @Column
    @Enumerated(EnumType.STRING)
    private ShapeType type;
    @OneToOne
    private Size size = new Size();
    @Column
    private String fill = "#ffffff";
    @Column
    private String stroke = "#333333";
    @Column
    private boolean input = true;
    @Column
    private boolean output = true;

    public ShapeType getType() {
        return type;
    }

    public void setType(ShapeType type) {
        this.type = type;
    }

    public void override(Shape source) {
        super.override(source);
        this.setType(source.getType());
    }

    public void copyNonEmpty(Shape source) {
        super.copyNonEmpty(source);
        if (source.getType() != null) {
            this.setType(source.getType());
        }
    }
}
