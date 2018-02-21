package com.araguacaima.open_archi.persistence.asm;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "ASM",
        name = "Status")
public class Status extends BaseEntity {

    @Column
    private boolean current;
    @Column
    private Date issueDate;
    @Column
    private int rank;
    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Markdown reason;
    @Column
    @Enumerated(EnumType.STRING)
    private StatusStep step;
    @ManyToOne
    private Asm asm;

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

    public Asm getAsm() {
        return asm;
    }

    public void setAsm(Asm asm) {
        this.asm = asm;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }
}
