package com.araguacaima.open_archi.persistence.asm;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(schema = "ASM",
        name = "Initiative")
public class Initiative extends BaseEntity {

    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Markdown description;
    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Objectives objectives;
    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Asm asm;

    public Markdown getDescription() {
        return description;
    }

    public void setDescription(Markdown description) {
        this.description = description;
    }

    public Objectives getObjectives() {
        return objectives;
    }

    public void setObjectives(Objectives objectives) {
        this.objectives = objectives;
    }

    public Asm getAsm() {
        return asm;
    }

    public void setAsm(Asm asm) {
        this.asm = asm;
    }
}
