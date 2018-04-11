package com.araguacaima.open_archi.persistence.diagrams.core.reliability;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "BatchProcessing", schema = "DIAGRAMS")
@DynamicUpdate
public class BatchProcessing extends BaseEntity {

    public void override(BatchProcessing source, boolean keepMeta) {
        super.override(source, keepMeta);
    }

    public void copyNonEmpty(BatchProcessing source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);

    }
}
