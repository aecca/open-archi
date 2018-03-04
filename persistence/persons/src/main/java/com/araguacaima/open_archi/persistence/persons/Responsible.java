package com.araguacaima.open_archi.persistence.persons;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@PersistenceUnit(unitName = "open-archi")
@Entity
@Table(schema = "PERSONS", name = "Responsible")
@DynamicUpdate
public class Responsible extends BaseEntity {

    @Column
    private String organizationUnit;
    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Person person;

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person Responsible) {
        this.person = Responsible;
    }

    public void override(Responsible source) {
        super.override(source);
        this.setPerson(source.getPerson());
        this.setOrganizationUnit(source.getOrganizationUnit());
    }

    public void copyNonEmpty(Responsible source) {
        super.copyNonEmpty(source);
        if (source.getPerson() != null) {
            this.setPerson(source.getPerson());
        }
        if (source.getOrganizationUnit() != null && !source.getOrganizationUnit().isEmpty()) {
            this.setOrganizationUnit(source.getOrganizationUnit());
        }
    }

}
