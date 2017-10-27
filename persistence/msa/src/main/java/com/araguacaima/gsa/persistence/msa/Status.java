package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.persistence.meta.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@PersistenceContext(unitName = "msa")
@Table(schema = "MSA",
        name = "Status")
public class Status extends BaseEntity {

    @Column
    private boolean current;
    @Column
    private Date issueDate;
    @Column
    private int rank;
    @OneToOne
    private Markdown reason;
    @Column
    private StatusStep step;
    @ManyToOne
    private Msa msa;

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Markdown getReason() {
        return reason;
    }

    public void setReason(Markdown reason) {
        this.reason = reason;
    }

    public StatusStep getStep() {
        return step;
    }

    public void setStep(StatusStep statusItem) {
        this.step = statusItem;
    }

    public Msa getMsa() {
        return msa;
    }

    public void setMsa(Msa msa) {
        this.msa = msa;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }
}
