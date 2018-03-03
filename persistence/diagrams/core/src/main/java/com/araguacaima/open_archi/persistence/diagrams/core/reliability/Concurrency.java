package com.araguacaima.open_archi.persistence.diagrams.core.reliability;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "Concurrency", schema = "DIAGRAMS")
@DynamicUpdate
public class Concurrency extends BaseEntity implements MeasurableRange {
    public static Measurable HIGH = new Measurable(new Range(ConcurrencyUnit
            .NUMBER_OF_CONCURRENT_CONSUMERS.name(),
            0,
            10));
    public static Measurable LOW = new Measurable(new Range(ConcurrencyUnit.NUMBER_OF_CONCURRENT_CONSUMERS.name(),
            11,
            30));
    public static Measurable MEDIUM = new Measurable(new Range(ConcurrencyUnit
            .NUMBER_OF_CONCURRENT_CONSUMERS.name(),
            31,
            null));
    private static final Set<Measurable> RANGES = new HashSet<Measurable>() {{
        add(HIGH);
        add(LOW);
        add(MEDIUM);
    }};
    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Measurable value;

    public Concurrency() {
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
}
