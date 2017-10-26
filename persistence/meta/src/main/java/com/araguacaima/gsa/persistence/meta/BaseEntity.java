package com.araguacaima.gsa.persistence.meta;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.UUID;

@MappedSuperclass
public class BaseEntity implements Serializable, BasicEntity, Cloneable {

    private static final long serialVersionUID = 5449758397914117108L;

    @Id
    @Column(name = "Id")
    protected String id;

    public BaseEntity() {
        this.id = generateId();
    }

    public String generateId() {
        return UUID.randomUUID().toString();
    }


    @Override
    public String getId() {
        return this.id;
    }

}