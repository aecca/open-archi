package com.araguacaima.gsa.model.meta;

import java.io.Serializable;
import java.util.UUID;


public class BaseEntity implements Serializable, Cloneable, IBaseEntity {

    private static final long serialVersionUID = 5449758397914117108L;

    protected String id;
    protected MetaInfo metaInfo;
    private MetaData metaData;

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
    public MetaData getMetaData() {
        return this.metaData;
    }

    @Override
    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
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

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getTags() {
        return null;
    }

    @Override
    public void addTags(String... tags) {

    }

    @Override
    public void removeTag(String tag) {

    }

    @Override
    public boolean hasTag(String tag) {
        return false;
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