package com.araguacaima.gsa.model.persons;

import com.araguacaima.gsa.model.meta.BaseEntity;

public class Person extends BaseEntity {

    private String lastNames;

    private String names;

    public String getLastNames() {
        return lastNames;
    }

    public void setLastNames(String lastNames) {
        this.lastNames = lastNames;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

}
