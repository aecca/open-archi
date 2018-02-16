package com.araguacaima.open_archi.persistence.asm;

public enum Country {
    MX("México", "Mexico"),
    ES("España", "Spain"),
    PE("Perú", "Peru"),
    AR("Argentina", "Argentina"),
    UR("Uruguay", "Uruguay"),
    PA("Paraguay", "Paraguay"),
    VE("Venezuela", "Venezuela"),
    CO("Colombia", "Spain"),
    USA("Estados Unidos", "USA"),
    CL("Chile", "Chile");

    private final String value_EN;
    private final String value_ES;

    Country(String valueES, String valueEN) {
        this.value_ES = valueES;
        this.value_EN = valueEN;
    }

    public String getValue_EN() {
        return value_EN;
    }

    public String getValue_ES() {
        return value_ES;
    }
}
