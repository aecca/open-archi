package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "gsa")
@Table(name = "Activity", schema = "MSA")
public class Activity extends BaseEntity {

    public static Measurable DEFAULT_BIG = new Measurable(new Range<Requests>(Requests.REQUESTS_PER_MONTH,
            4000001,
            20000000));
    public static Measurable DEFAULT_HUGE = new Measurable(new Range<Requests>(Requests.REQUESTS_PER_MONTH,
            20000001,
            80000000));
    public static Measurable DEFAULT_MEDIUM = new Measurable(new Range<Requests>(Requests.REQUESTS_PER_SECOND,
            600001,
            4000000));
    public static Measurable DEFAULT_MINIMAL = new Measurable(new Range<Requests>(Requests.REQUESTS_PER_SECOND,
            0,
            120000));
    public static Measurable DEFAULT_SMALL = new Measurable(new Range<Requests>(Requests.REQUESTS_PER_SECOND,
            120001,
            600000));
    @OneToOne(cascade = CascadeType.REMOVE)
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
}
