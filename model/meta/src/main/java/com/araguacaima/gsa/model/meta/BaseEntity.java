package com.araguacaima.gsa.model.meta;

import java.io.Serializable;
import java.util.UUID;


public class BaseEntity implements Serializable, BasicEntity, Cloneable {

    private static final long serialVersionUID = 5449758397914117108L;

    protected String id;
    protected MetaInfo metaInfo;

    public BaseEntity() {
        this.id = generateId();
    }

    public String generateId() {
        return UUID.randomUUID().toString();
    }

    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof BaseEntity)) {
            return false;
        }
        BaseEntity other = (BaseEntity) obj;
        return getId().equals(other.getId());
    }

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        } else {
            return 0;
        }
    }
}