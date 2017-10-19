package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;

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

    private RequestTarget target;

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
