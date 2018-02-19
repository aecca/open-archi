package com.araguacaima.open_archi.persistence.asm;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "Database",
        schema = "ASM")
public class Database extends BaseEntity {
    public static Measurable DEFAULT_BIG = new Measurable(new Range<StorageUnit>(StorageUnit.GB, 251, 500));
    public static Measurable DEFAULT_HUGE = new Measurable(new Range<StorageUnit>(StorageUnit.GB, 501, null));
    public static Measurable DEFAULT_MEDIUM = new Measurable(new Range<StorageUnit>(StorageUnit.GB, 101, 250));
    public static Measurable DEFAULT_MINIMAL = new Measurable(new Range<StorageUnit>(StorageUnit.GB, 0, 50));
    public static Measurable DEFAULT_SMALL = new Measurable(new Range<StorageUnit>(StorageUnit.GB, 51, 100));
    @Column
    @Enumerated(EnumType.STRING)
    private DataBaseType type;
    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Measurable value;

    public Database(DataBaseType type) {
        this.type = type;
    }

    public Database() {
    }

    public DataBaseType getType() {
        return type;
    }

    public void setType(DataBaseType type) {
        this.type = type;
    }

    public Measurable getValue() {
        return value;
    }

    public void setValue(Measurable value) {
        this.value = value;
    }
}
