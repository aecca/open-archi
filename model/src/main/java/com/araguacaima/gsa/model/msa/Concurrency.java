package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;

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
