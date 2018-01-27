package com.araguacaima.gsa.persistence.diagrams.classes;

public enum Visibility {
    PUBLIC("public"),
    PRIVATE("private"),
    PROTECTED("protected"),
    PACKAGE("package");

    private String value;

    Visibility(String value) {
        this.value = value;
    }
}
