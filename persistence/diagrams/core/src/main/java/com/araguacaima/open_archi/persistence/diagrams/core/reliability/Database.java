package com.araguacaima.open_archi.persistence.diagrams.core.reliability;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "Database", schema = "DIAGRAMS")
@DynamicUpdate
public class Database extends BaseEntity implements MeasurableRange {
    private static final Set<Measurable> RANGE = new HashSet<Measurable>() {{
        add(DEFAULT_BIG);
        add(DEFAULT_HUGE);
        add(DEFAULT_MEDIUM);
        add(DEFAULT_MINIMAL);
        add(DEFAULT_SMALL);
    }};
    public static Measurable DEFAULT_BIG = new Measurable(new Range(StorageUnit.Giga_Bytes.name(), 251, 500));
    public static Measurable DEFAULT_HUGE = new Measurable(new Range(StorageUnit.Giga_Bytes.name(), 501, null));
    public static Measurable DEFAULT_MEDIUM = new Measurable(new Range(StorageUnit.Giga_Bytes.name(), 101, 250));
    public static Measurable DEFAULT_MINIMAL = new Measurable(new Range(StorageUnit.Giga_Bytes.name(), 0, 50));
    public static Measurable DEFAULT_SMALL = new Measurable(new Range(StorageUnit.Giga_Bytes.name(), 51, 100));
    @Column
    @Enumerated(EnumType.STRING)
    private DataBaseType type;
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
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

    @Override
    public Set<Measurable> getRanges() {
        return RANGE;
    }

    public void override(Database source, boolean keepMeta) {
        super.override(source, keepMeta);
        this.setValue(source.getValue());
        this.setType(source.getType());
    }

    public void copyNonEmpty(Database source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getValue() != null) {
            this.setValue(source.getValue());
        }
        if (source.getType() != null) {
            this.setType(source.getType());
        }
    }
}
