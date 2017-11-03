package com.araguacaima.gsa.persistence.persons;

import com.araguacaima.gsa.persistence.meta.BaseEntity;

import javax.persistence.*;

@PersistenceUnit(unitName = "gsa" )
@Entity
@Table(schema = "PERSONS",
        name = "Responsible")
public class Responsible extends BaseEntity {

    @Column
    private String organizationUnit;
    @OneToOne
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

    public void setResponsible(Person Responsible) {
        this.responsible = Responsible;
    }

}
