package com.araguacaima.open_archi.persistence.diagrams.core;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "Shape", schema = "DIAGRAMS")
@DynamicUpdate
public class Shape extends BaseEntity {

    @Column
    @Enumerated(EnumType.STRING)
    private ElementKind type;

    @Column
    private String fill;

    @Column
    private String stroke;

    @Column
    private boolean input = true;

    @Column
    private boolean output = true;

    @Column
    private String figure;

    public Shape(ElementKind type) {
        this.type = type;
    }

    public Shape() {
    }

    public ElementKind getType() {
        return type;
    }

    public void setType(ElementKind type) {
        this.type = type;
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

    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public void override(Shape source, boolean keepMeta, String suffix) {
        super.override(source, keepMeta, suffix);
        this.setType(source.getType());
        this.setFill(source.getFill());
        this.setOutput(source.isOutput());
        this.setInput(source.isInput());
        this.setStroke(source.getStroke());
        this.setFigure(source.getFigure());
    }

    public void copyNonEmpty(Shape source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getType() != null) {
            this.setType(source.getType());
        }
        if (source.getFill() != null) {
            this.setFill(source.getFill());
        }
        this.setOutput(source.isOutput());
        this.setInput(source.isInput());
        if (source.getStroke() != null) {
            this.setStroke(source.getStroke());
        }
        if (StringUtils.isNotBlank(source.getFigure())) {
            this.setFigure(source.getFigure());
        }
    }
}
