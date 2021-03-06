package com.araguacaima.open_archi.persistence.asm;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "Concurrency", schema = "ASM")
@DynamicUpdate
public class Concurrency extends BaseEntity {
    public static Measurable HIGH = new Measurable(new Range<ConcurrencyUnit>(ConcurrencyUnit
            .NUMBER_OF_CONCURRENT_USERS,
            0,
            10));
    public static Measurable LOW = new Measurable(new Range<ConcurrencyUnit>(ConcurrencyUnit.NUMBER_OF_CONCURRENT_USERS,
            11,
            30));
    public static Measurable MEDIUM = new Measurable(new Range<ConcurrencyUnit>(ConcurrencyUnit
            .NUMBER_OF_CONCURRENT_USERS,
            31,
            null));
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
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
}
