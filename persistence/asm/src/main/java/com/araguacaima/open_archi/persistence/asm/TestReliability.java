package com.araguacaima.open_archi.persistence.asm;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "ASM", name = "TestReliability")
@DynamicUpdate
public class TestReliability extends AbstractReliability {

}
