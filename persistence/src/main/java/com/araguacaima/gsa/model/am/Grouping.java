package com.araguacaima.gsa.model.am;

import com.araguacaima.gsa.model.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@PersistenceContext(unitName = "gsa")
@Table(name = "Grouping", schema = "AM")
public class Grouping  extends BaseEntity {

    @Column
    private UUID id;

    @Column
    private String name;
}
