package com.araguacaima.gsa.persistence.msa;

import com.araguacaima.gsa.persistence.meta.BaseEntity;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "gsa")
@Table(schema = "MSA",
        name = "Initiative")
public class Initiative extends BaseEntity {

    @OneToOne(cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST})
    private Markdown description;
    @OneToOne(cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST})
    private Objectives objectives;
    @OneToOne(cascade = CascadeType.PERSIST)
    @Cascade({org.hibernate.annotations.CascadeType.PERSIST})
    private Msa msa;

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

    public Msa getMsa() {
        return msa;
    }

    public void setMsa(Msa msa) {
        this.msa = msa;
    }
}
