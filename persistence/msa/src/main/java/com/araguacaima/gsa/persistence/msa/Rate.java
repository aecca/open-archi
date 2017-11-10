package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "gsa")
@Table(name = "Rate",
        schema = "MSA")
public class Rate extends BaseEntity {

    public static Measurable DEFAULT_BIG = new Measurable(new Range<Requests>(Requests.REQUESTS_PER_SECOND, 251, 500));
    public static Measurable DEFAULT_HUGE = new Measurable(new Range<Requests>(Requests.REQUESTS_PER_SECOND,
            501,
            null));
    public static Measurable DEFAULT_MEDIUM = new Measurable(new Range<Requests>(Requests.REQUESTS_PER_SECOND,
            101,
            250));
    public static Measurable DEFAULT_MINIMAL = new Measurable(new Range<Requests>(Requests.REQUESTS_PER_SECOND, 0, 50));
    public static Measurable DEFAULT_SMALL = new Measurable(new Range<Requests>(Requests.REQUESTS_PER_SECOND, 51, 100));
    @Column
    private RequestTarget target;
    @OneToOne(cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST})
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
}
