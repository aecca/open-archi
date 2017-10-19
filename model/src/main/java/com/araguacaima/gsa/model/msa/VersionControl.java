package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.common.BaseEntity;
import com.araguacaima.gsa.model.persons.Responsible;

import java.util.Date;

public class VersionControl extends BaseEntity {

    private String description;

    private Date issueDate;

    private Responsible responsible;

    private Msa msa;

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

    public void setResponsible(Responsible responsible) {
        this.responsible = responsible;
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
