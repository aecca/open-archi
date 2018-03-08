package com.araguacaima.open_archi.persistence.persons;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

@PersistenceUnit(unitName = "open-archi")
@Entity
@Table(schema = "PERSONS", name = "Person")
@DynamicUpdate
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


    public void override(Person source) {
        super.override(source);
        this.names = source.getNames();
        this.lastNames = source.getLastNames();
    }

    public void copyNonEmpty(Person source) {
        super.copyNonEmpty(source);
        if (source.getNames() != null) {
            this.names = source.getNames();
        }
        if (source.getLastNames() != null) {
            this.lastNames = source.getLastNames();
        }
    }

}
