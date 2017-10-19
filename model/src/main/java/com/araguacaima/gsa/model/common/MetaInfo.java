package com.araguacaima.gsa.model.common;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class MetaInfo implements Serializable, Comparable<MetaInfo> {

    private String id;

    private Version version;

    private Account createdBy;

    private Account modifiedBy;

    private Date created;

    private Date modified;

    public MetaInfo() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public Account getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Account createdBy) {
        this.createdBy = createdBy;
    }

    public Account getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Account modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        final Object cloned = super.clone();
        ((MetaInfo) cloned).id = UUID.randomUUID().toString();
        return cloned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        MetaInfo metaInfo = (MetaInfo) o;

        return new EqualsBuilder().append(id, metaInfo.id)
                .append(version, metaInfo.version)
                .append(createdBy, metaInfo.createdBy)
                .append(modifiedBy, metaInfo.modifiedBy)
                .append(created, metaInfo.created)
                .append(modified, metaInfo.modified)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id)
                .append(version)
                .append(createdBy)
                .append(modifiedBy)
                .append(created)
                .append(modified)
                .toHashCode();
    }

    @Override
    public int compareTo(MetaInfo o) {
        return this.version.compareTo(o.version);
    }
}
