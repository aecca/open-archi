package com.araguacaima.open_archi.persistence.diagrams.core;

import com.araguacaima.open_archi.persistence.commons.exceptions.EntityError;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "ElementRole", schema = "DIAGRAMS", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQueries({@NamedQuery(name = ElementRole.GET_ALL_ROLES, query = "select a from ElementRole a "), @NamedQuery(name = ElementRole.GET_ROLE_BY_NAME, query = "select a from ElementRole a where a.name  = :name ")})
public class ElementRole extends BaseEntity {

    public static final String GET_ALL_ROLES = "get.all.roles";
    public static final String GET_ROLE_BY_NAME = "get.role.by.name";

    @Column
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void override(BaseEntity source, boolean keepMeta, String suffix) {
        super.override(source, keepMeta, suffix);
    }

    @Override
    public void copyNonEmpty(BaseEntity source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
    }

    @Override
    public void validateRequest() throws EntityError {
        super.validateRequest();
    }

    @Override
    public void validateCreation() throws EntityError {
        super.validateCreation();
    }

    @Override
    public void validateModification() throws EntityError {
        super.validateModification();
    }

    @Override
    public void validateReplacement() throws EntityError {
        super.validateReplacement();
    }
}
