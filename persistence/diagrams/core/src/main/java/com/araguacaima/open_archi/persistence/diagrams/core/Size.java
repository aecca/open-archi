package com.araguacaima.open_archi.persistence.diagrams.core;


import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * A point representing a location in {@code (x,y)} coordinate space,
 * specified in integer precision.
 *
 * @author Sami Shaio
 * @since 1.0
 */
@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "Point", schema = "DIAGRAMS")
@DynamicUpdate
public class Size extends BaseEntity implements Serializable, Cloneable {

    private static final long serialVersionUID = -5276940640259749850L;
    /**
     * The width of the element.
     * If no width is set it will default to 0.
     */
    @Column
    private double width;
    /**
     * The height of this <code>Point</code>.
     * If no height is set it will default to 0.
     */
    @Column
    private double height;


    /**
     * Constructs and initializes a size as 0
     * (0,&nbsp;0) of the width and height.
     */
    public Size() {
        this(0, 0);
    }


    /**
     * Constructs and initializes a size at the specified
     * {@code (width,height)}.
     *
     * @param width the width of the newly constructed <code>Size</code>
     * @param height the height of the newly constructed <code>Size</code>
     */
    public Size(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }


    public void override(Size source) {
        super.override(source);
        this.width = source.getWidth();
        this.height = source.getHeight();
    }

    public void copyNonEmpty(Size source) {
        super.copyNonEmpty(source);
        if (source.getWidth() != 0) {
            this.width = source.getWidth();
        }
        if (source.getHeight() != 0) {
            this.height = source.getHeight();
        }
    }
}
