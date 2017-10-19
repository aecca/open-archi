package com.araguacaima.gsa.model.am;


import com.araguacaima.gsa.model.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

@Entity
@PersistenceContext(unitName = "gsa")
@Table(name = "Enterprise", schema = "AM")
public class Enterprise extends BaseEntity {

    @Column
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
