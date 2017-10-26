package com.araguacaima.gsa.persistence.am;


import com.araguacaima.gsa.persistence.common.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A point representing a location in {@code (x,y)} coordinate space,
 * specified in integer precision.
 */
@Entity
@PersistenceContext(unitName = "gsa")
@Table(name = "Point", schema = "AM")
public class Point extends BaseEntity implements Serializable, Cloneable {
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

    /*
     * JDK 1.1 serialVersionUID
     */
    private static final long serialVersionUID = -5276940640259749850L;

    /**
     * Constructs and initializes a point at the origin
     * (0,&nbsp;0) of the coordinate space.
     */
    public Point() {
        this(0, 0);
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }


}
