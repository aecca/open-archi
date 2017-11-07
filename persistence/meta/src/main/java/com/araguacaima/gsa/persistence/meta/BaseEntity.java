package com.araguacaima.gsa.persistence.meta;

import com.araguacaima.gsa.persistence.commons.Constants;
import com.araguacaima.gsa.persistence.commons.exceptions.EntityError;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ResourceBundle;
import java.util.UUID;

@MappedSuperclass
@PersistenceUnit(unitName = "gsa")
public abstract class BaseEntity implements Serializable, BasicEntity, Cloneable {
    @Transient
    protected ResourceBundle resourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_NAME);

    private static final long serialVersionUID = 5449758397914117108L;

    @Id
    @NotNull
    @Column(name = "Id")
    protected String id;

    public BaseEntity() {
        this.id = generateId();
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }


    @Override
    public String getId() {
        return this.id;
    }

    protected String getRequestErrorMessageKey() {
        return this.getClass().getName().toLowerCase() + "-" + "request" + "." + "error";
    }

    protected String getModificationErrorMessageKey() {
        return this.getClass().getName().toLowerCase() + "-" + "modification" + "." + "error";
    }

    protected String getCreationErrorMessageKey() {
        return this.getClass().getName().toLowerCase() + "-" + "creation" + "." + "error";
    }

    @Override
    public void validateRequest() throws EntityError {
        //Do nothing. All request are valid on this entity
    }

    @Override
    public void validateCreation() throws EntityError {
        if (id == null) {
            this.id = generateId();
        }
    }

    @Override
    public void validateModification() throws EntityError {
        if (id != null) {
            throw new EntityError(resourceBundle.getString(this.getClass().getName() + "-" + "entity.identifier.cannot.be.modified"));
        }
    }

}