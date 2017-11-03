package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.persistence.meta.BaseEntity;
import com.araguacaima.gsa.persistence.persons.Responsible;

import javax.persistence.*;
import java.util.Date;

@Entity
@PersistenceUnit(unitName = "gsa" )
@Table(schema = "MSA",
        name = "VersionControl")
public class VersionControl extends BaseEntity {

    @Column
    private String description;
    @Column
    private Date issueDate;
    @OneToOne
    @JoinTable(schema = "PERSONS",
            name = "Responsible",
            joinColumns = {
                    @JoinColumn(table = "Responsible", name = "Person_Id", referencedColumnName = "Id")})
    private Responsible responsible;
    @ManyToOne
    private Msa msa;
    @Column
    private String version;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Responsible getResponsible() {
        return responsible;
    }

    public void setResponsible(Responsible Responsible) {
        this.responsible = Responsible;
    }

    public Msa getMsa() {
        return msa;
    }

    public void setMsa(Msa msa) {
        this.msa = msa;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
