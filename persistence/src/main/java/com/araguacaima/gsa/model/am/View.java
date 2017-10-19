package com.araguacaima.gsa.model.am;


import com.araguacaima.gsa.model.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

@Entity
@PersistenceContext(unitName = "gsa")
@Table(name = "View", schema = "AM")
public class View extends BaseEntity {
}
