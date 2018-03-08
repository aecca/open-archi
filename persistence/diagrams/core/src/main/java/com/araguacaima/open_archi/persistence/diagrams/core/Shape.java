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
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
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

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        this.fill = fill;
    }

    public String getStroke() {
        return stroke;
    }

    public void setStroke(String stroke) {
        this.stroke = stroke;
    }

    public boolean isInput() {
        return input;
    }

    public void setInput(boolean input) {
        this.input = input;
    }

    public boolean isOutput() {
        return output;
    }

    public void setOutput(boolean output) {
        this.output = output;
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
