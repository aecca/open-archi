package com.araguacaima.gsa.model.am;

public final class Enterprise {

    private String name;

    public Enterprise() {
    }

    public Enterprise(String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("Name must be specified.");
        }

        this.name = name;
    }

    public String getName() {
        return name;
    }

}
