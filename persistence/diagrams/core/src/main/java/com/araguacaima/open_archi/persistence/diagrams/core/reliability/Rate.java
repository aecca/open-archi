package com.araguacaima.open_archi.persistence.diagrams.core.reliability;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "Rate", schema = "DIAGRAMS")
@DynamicUpdate
public class Rate extends BaseEntity implements MeasurableRange {

    public static Measurable DEFAULT_BIG = new Measurable(new Range(Requests.REQUESTS_PER_SECOND.name(), 251, 500));
    public static Measurable DEFAULT_HUGE = new Measurable(new Range(Requests.REQUESTS_PER_SECOND.name(),
            501,
            null));
    public static Measurable DEFAULT_MEDIUM = new Measurable(new Range(Requests.REQUESTS_PER_SECOND.name(),
            101,
            250));
    public static Measurable DEFAULT_MINIMAL = new Measurable(new Range(Requests.REQUESTS_PER_SECOND.name(), 0, 50));
    public static Measurable DEFAULT_SMALL = new Measurable(new Range(Requests.REQUESTS_PER_SECOND.name(), 51, 100));
    private static final Set<Measurable> RANGES = new HashSet<Measurable>() {{
        add(DEFAULT_BIG);
        add(DEFAULT_HUGE);
        add(DEFAULT_MEDIUM);
        add(DEFAULT_MINIMAL);
        add(DEFAULT_SMALL);
    }};
    @Column
    @Enumerated(EnumType.STRING)
    private RequestTarget target;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Measurable value;

    public Rate() {
    }

    public Rate(RequestTarget target) {
        this.target = target;
    }

    public RequestTarget getTarget() {
        return target;
    }

    public void setTarget(RequestTarget target) {
        this.target = target;
    }

    public Measurable getValue() {
        return value;
    }

    public void setValue(Measurable value) {
        this.value = value;
    }

    @Override
    public Set<Measurable> getRanges() {
        return RANGES;
    }


    public void override(Rate source, boolean keepMeta, String suffix) {
        super.override(source, keepMeta, suffix);
        this.setValue(source.getValue());
        this.setTarget(source.getTarget());
    }

    public void copyNonEmpty(Rate source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getValue() != null) {
            this.setValue(source.getValue());
        }
        if (source.getTarget() != null) {
            this.setTarget(source.getTarget());
        }
    }
}
