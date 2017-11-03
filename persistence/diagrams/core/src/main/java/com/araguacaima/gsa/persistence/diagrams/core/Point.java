package com.araguacaima.gsa.persistence.diagrams.core;


import com.araguacaima.gsa.persistence.meta.BaseEntity;

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
@PersistenceUnit(unitName = "gsa" )
@Table(name = "Point", schema = "DIAGRAMS")
public class Point extends BaseEntity implements Serializable, Cloneable {

    private static final long serialVersionUID = -5276940640259749850L;
    /**
     * The X coordinate of this <code>Point</code>.
     * If no X coordinate is set it will default to 0.
     */
    @Column
    private double x;
    /**
     * The Y coordinate of this <code>Point</code>.
     * If no Y coordinate is set it will default to 0.
     */
    @Column
    private double y;

    /**
     * Constructs and initializes a point at the origin
     * (0,&nbsp;0) of the coordinate space.
     *
     * @since 1.1
     */
    public Point() {
        this(0, 0);
    }


    /**
     * Constructs and initializes a point at the specified
     * {@code (x,y)} location in the coordinate space.
     *
     * @param x the X coordinate of the newly constructed <code>Point</code>
     * @param y the Y coordinate of the newly constructed <code>Point</code>
     * @since 1.0
     */
    Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.2
     */
    public double getX() {
        return x;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.2
     */
    public double getY() {
        return y;
    }


}
