package com.araguacaima.open_archi.persistence.diagrams.core;


import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "ConnectTrigger", schema = "DIAGRAMS")
@DynamicUpdate
public class ConnectTrigger {

    @Id
    @NotNull
    @Column(name = "Id")
    private String id;

    @Column
    private String triggerById;

    @Column
    private String triggerByName;

    @Column
    private String triggerByExpression;

    public ConnectTrigger() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTriggerById() {
        return triggerById;
    }

    public void setTriggerById(String triggerById) {
        this.triggerById = triggerById;
    }

    public String getTriggerByName() {
        return triggerByName;
    }

    public void setTriggerByName(String triggerByName) {
        this.triggerByName = triggerByName;
    }

    public String getTriggerByExpression() {
        return triggerByExpression;
    }

    public void setTriggerByExpression(String triggerByExpression) {
        this.triggerByExpression = triggerByExpression;
    }
}
