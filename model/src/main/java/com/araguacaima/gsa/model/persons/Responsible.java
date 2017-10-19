package com.araguacaima.gsa.model.persons;

import com.araguacaima.gsa.model.common.BaseEntity;

public class Responsible extends BaseEntity {

    private String organizationUnit;

    private Person responsible;

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public Person getResponsible() {
        return responsible;
    }

    public void setResponsible(Person responsible) {
        this.responsible = responsible;
    }

}
