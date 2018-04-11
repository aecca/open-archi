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
     * The Z coordinate of this <code>Point</code>.
     * If no Z coordinate is set it will default to 0.
     */
    @Column
    private double z;

    /**
     * Constructs and initializes a point at the origin
     * (0,&nbsp;0) of the coordinate space.
     */
    public Point() {
        this(0, 0, 0);
    }


    /**
     * Constructs and initializes a point at the specified
     * {@code (x,y)} location in the coordinate space.
     *
     * @param x the X coordinate of the newly constructed <code>Point</code>
     * @param y the Y coordinate of the newly constructed <code>Point</code>
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs and initializes a point at the specified
     * {@code (x,y,z)} location in the coordinate space.
     *
     * @param x the X coordinate of the newly constructed <code>Point</code>
     * @param y the Y coordinate of the newly constructed <code>Point</code>
     * @param z the Z coordinate of the newly constructed <code>Point</code>
     */
    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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


    /**
     * {@inheritDoc}
     *
     * @since 1.2
     */
    public double getZ() {
        return z;
    }


    public void override(Point source, boolean keepMeta, String suffix) {
        super.override(source, keepMeta, suffix);
        this.x = source.getX();
        this.y = source.getY();
        this.z = source.getZ();
    }

    public void copyNonEmpty(Point source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getX() != 0) {
            this.x = source.getX();
        }
        if (source.getY() != 0) {
            this.y = source.getY();
        }
        if (source.getZ() != 0) {
            this.z = source.getZ();
        }
    }
}
