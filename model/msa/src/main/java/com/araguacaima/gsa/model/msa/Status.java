package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.meta.BaseEntity;

import java.util.Date;

public class Status extends BaseEntity {

    private boolean current;

    private Date issueDate;

    private int rank;

    private Markdown reason;

    private StatusStep step;

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

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }
}
