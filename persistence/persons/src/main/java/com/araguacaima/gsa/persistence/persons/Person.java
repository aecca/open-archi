package com.araguacaima.gsa.persistence.persons;

import com.araguacaima.gsa.persistence.meta.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

@PersistenceUnit(unitName = "persons")
@Entity
@Table(schema = "PERSONS",
        name = "Person")
public class Person extends BaseEntity {

    @Column
    private String lastNames;
    @Column
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
