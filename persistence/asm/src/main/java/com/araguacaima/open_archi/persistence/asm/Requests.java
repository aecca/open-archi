package com.araguacaima.open_archi.persistence.asm;

public enum Requests {
    REQUESTS_PER_MONTH("Request per month"),
    REQUESTS_PER_DAY("Request per day"),
    REQUESTS_PER_HOUR("Request per hour"),
    REQUESTS_PER_WEEK("Request per week"),
    REQUESTS_PER_SECOND("Request per second"),
    REQUESTS_PER_MILLISECOND("Request per millisecond");

    private final String value;

    Requests(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
