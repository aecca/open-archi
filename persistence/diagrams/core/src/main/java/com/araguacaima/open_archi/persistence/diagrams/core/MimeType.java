package com.araguacaima.open_archi.persistence.diagrams.core;

public enum MimeType {

    BMP("image/bmp"),
    VND("image/vnd.dwg"),
    GIF("image/gif"),
    JPEG("image/jpeg"),
    PNG("image/png"),
    SVG("image/svg+xml");

    private final String value;

    MimeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
