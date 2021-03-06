package com.araguacaima.open_archi.persistence.diagrams.core.reliability;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "Activity", schema = "DIAGRAMS")
@DynamicUpdate
public class Activity extends BaseEntity implements MeasurableRange {

    public static Measurable DEFAULT_BIG = new Measurable(new Range(Requests.REQUESTS_PER_MONTH.name(),
            4000001,
            20000000));
    public static Measurable DEFAULT_HUGE = new Measurable(new Range(Requests.REQUESTS_PER_MONTH.name(),
            20000001,
            80000000));
    public static Measurable DEFAULT_MEDIUM = new Measurable(new Range(Requests.REQUESTS_PER_SECOND.name(),
            600001,
            4000000));
    public static Measurable DEFAULT_MINIMAL = new Measurable(new Range(Requests.REQUESTS_PER_SECOND.name(),
            0,
            120000));
    public static Measurable DEFAULT_SMALL = new Measurable(new Range(Requests.REQUESTS_PER_SECOND.name(),
            120001,
            600000));
    private static Set<Measurable> RANGES = new HashSet<Measurable>() {{
        add(DEFAULT_BIG);
        add(DEFAULT_HUGE);
        add(DEFAULT_MEDIUM);
        add(DEFAULT_MINIMAL);
        add(DEFAULT_SMALL);
    }};
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Measurable value;

    public Activity() {
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


    public void override(Activity source, boolean keepMeta, String suffix) {
        super.override(source, keepMeta, suffix);
        this.setValue(source.getValue());
    }

    public void copyNonEmpty(Activity source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getValue() != null) {
            this.setValue(source.getValue());
        }
    }
}
