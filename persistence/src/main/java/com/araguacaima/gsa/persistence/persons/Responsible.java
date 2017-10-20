package com.araguacaima.gsa.persistence.persons;

import com.araguacaima.gsa.persistence.common.BaseEntity;

import javax.persistence.*;

@PersistenceContext(unitName = "persons")
@Entity
@Table(schema = "PERSONS",
        name = "Responsible")
public class Responsible extends BaseEntity {

    @Column
    private String organizationUnit;
    @OneToOne
    private Person Responsible;

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public Person getResponsible() {
        return Responsible;
    }

    public void setResponsible(Person Responsible) {
        this.Responsible = Responsible;
    }

}
