package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;
import com.araguacaima.gsa.model.persons.Responsible;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "Msa",
        name = "VersionControl")
public class VersionControl extends BaseEntity {

    @Column
    private String description;
    @Column
    private Date issueDate;
    @OneToOne
    private Responsible Responsible;
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
        return Responsible;
    }

    public void setResponsible(Responsible Responsible) {
        this.Responsible = Responsible;
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
