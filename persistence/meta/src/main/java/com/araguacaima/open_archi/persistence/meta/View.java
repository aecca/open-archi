package com.araguacaima.open_archi.persistence.meta;


import com.araguacaima.open_archi.persistence.commons.exceptions.EntityError;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "View", schema = "META")
@DynamicUpdate
public class View extends BaseEntity {

    @Column
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public void validateRequest() throws EntityError {
        super.validateRequest();
        //Do nothing. All request are valid on this entity
    }

    @Override
    public void validateCreation() {
        super.validateCreation();
        if (name == null) {
            throw new EntityError(resourceBundle.getString(getCreationErrorMessageKey()));
        }
    }

    @Override
    public void validateModification() throws EntityError {
        super.validateModification();
        if (name != null) {
            throw new EntityError(resourceBundle.getString(getModificationErrorMessageKey()));
        }
    }

    public void override(View source, boolean keepMeta) {
        super.override(source, keepMeta);
        this.setName(source.getName());
    }

    public void copyNonEmpty(View source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getName() != null) {
            this.setName(source.getName());
        }
    }
}
