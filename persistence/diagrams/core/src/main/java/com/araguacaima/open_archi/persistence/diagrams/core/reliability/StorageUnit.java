package com.araguacaima.open_archi.persistence.diagrams.core.reliability;

public enum StorageUnit {
    GB("Giga Bytes"),
    TB("Tera Bytes"),
    MB("Mega Bytes"),
    PB("Peta Bytes");

    private final String volume;

    StorageUnit(String volume) {
        this.volume = volume;
    }

    public String getVolume() {
        return volume;
    }
}
