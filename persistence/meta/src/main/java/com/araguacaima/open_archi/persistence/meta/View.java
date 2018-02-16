package com.araguacaima.open_archi.persistence.meta;


import com.araguacaima.open_archi.persistence.commons.exceptions.EntityError;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "View", schema = "META")
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
}
