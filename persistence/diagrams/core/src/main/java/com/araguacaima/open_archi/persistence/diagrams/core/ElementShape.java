package com.araguacaima.open_archi.persistence.diagrams.core;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "ElementShape", schema = "DIAGRAMS")
@DynamicUpdate
@NamedQueries({@NamedQuery(name = ElementShape.GET_ELEMENT_SHAPE_BY_TYPE,
        query = "select a " +
                "from ElementShape a where a.type=:type"),
        @NamedQuery(name = ElementShape.GET_ALL_ELEMENT_SHAPES,
                query = "select a " +
                        "from ElementShape a")})
public class ElementShape extends BaseEntity {

    public static final String GET_ELEMENT_SHAPE_BY_TYPE = "get.element.shape.by.type";
    public static final String GET_ALL_ELEMENT_SHAPES = "get.all.element.types";
    @Column
    @Enumerated(EnumType.STRING)
    private ElementKind type;
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Size size = new Size();
    @Column(nullable = false)
    private String fill;
    @Column(nullable = false)
    private String stroke;
    @Column
    private boolean input = true;
    @Column
    private boolean output = true;
    @Column
    private String figure;
    @Column
    private boolean isGroup;

    public ElementShape(ElementKind type) {
        this.type = type;
    }

    public ElementShape() {
    }

    public ElementKind getType() {
        return type;
    }

    public void setType(ElementKind type) {
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

    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public boolean isIsGroup() {
        return isGroup;
    }

    public void setIsGroup(boolean group) {
        this.isGroup = group;
    }

    public void override(ElementShape source, boolean keepMeta, String suffix) {
        this.setFill(source.getFill());
        this.setOutput(source.isOutput());
        this.setInput(source.isInput());
        this.setStroke(source.getStroke());
        if (source.getSize() != null) {
            Size size = new Size();
            size.override(source.getSize(), keepMeta, suffix);
            this.setSize(size);
        }
        this.setFigure(source.getFigure());
        this.setIsGroup(source.isIsGroup());
    }

    public void copyNonEmpty(ElementShape source, boolean keepMeta) {
        if (source.getFill() != null) {
            this.setFill(source.getFill());
        }
        this.setOutput(source.isOutput());
        this.setInput(source.isInput());
        if (source.getStroke() != null) {
            this.setStroke(source.getStroke());
        }
        if (source.getSize() != null) {
            Size size = this.size;
            if (size == null) {
                size = new Size();
            }
            size.copyNonEmpty(source.getSize(), keepMeta);
            this.setSize(size);
        }
        if (StringUtils.isNotBlank(source.getFigure())) {
            this.setFigure(source.getFigure());
        }
        this.setIsGroup(source.isIsGroup());
    }
}
