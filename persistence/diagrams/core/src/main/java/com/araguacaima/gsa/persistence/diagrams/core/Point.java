package com.araguacaima.gsa.persistence.diagrams.core;


import javax.persistence.Column;
import java.io.Serializable;

/**
 * A point representing a location in {@code (x,y)} coordinate space,
 * specified in integer precision.
 *
 * @author Sami Shaio
 * @since 1.0
 */
public class Point implements Serializable, Cloneable {
    /*
     * JDK 1.1 serialVersionUID
     */
    private static final long serialVersionUID = -5276940640259749850L;
    /**
     * The X coordinate of this <code>Point</code>.
     * If no X coordinate is set it will default to 0.
     *
     * @serial
     * @since 1.0
     */
    @Column
    public double x;
    /**
     * The Y coordinate of this <code>Point</code>.
     * If no Y coordinate is set it will default to 0.
     *
     * @serial
     * @since 1.0
     */
    @Column
    public double y;

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
    public Point(double x, double y) {
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
