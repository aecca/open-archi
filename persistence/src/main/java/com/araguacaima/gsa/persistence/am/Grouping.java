package com.araguacaima.gsa.persistence.am;

import com.araguacaima.gsa.persistence.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

@Entity
@PersistenceContext(unitName = "gsa")
@Table(name = "Grouping", schema = "AM")
public class Grouping extends BaseEntity {

    @Column
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
