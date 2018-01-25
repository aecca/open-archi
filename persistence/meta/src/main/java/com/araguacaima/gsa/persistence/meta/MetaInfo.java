package com.araguacaima.gsa.persistence.meta;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Alejandro on 19/12/2014.
 */
@PersistenceUnit(unitName = "gsa")
@Entity
@Table(schema = "META", name = "MetaInfo")
@NamedQueries(value = {@NamedQuery(name = MetaInfo.COUNT_ALL_META_INFO,
        query = "select count(a) from MetaInfo a"), @NamedQuery(
        name = MetaInfo.GET_ALL_META_INFO,
        query = "select a from MetaInfo a order by a.version, a.created"), @NamedQuery(
        name = MetaInfo.GET_DEFAULT_META_INFO,
        query = "select a from MetaInfo a where a.version.major = 1 and a.version.minor = 0 and a.version.build = 0 order by a.version, a.created")})
public class MetaInfo implements Serializable, Comparable<MetaInfo> {

    public static final String GET_ALL_META_INFO = "MetaInfo.getAllMetaInfo";
    public static final String COUNT_ALL_META_INFO = "MetaInfo.countAllMetaInfo";
    public static final String GET_DEFAULT_META_INFO = "MetaInfo.getDefaultMetaInfo";

    @Id
    private String id;

    @NotNull
    @ManyToOne
    private Version version;

    @NotNull
    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Account createdBy;

    @OneToOne(cascade = CascadeType.REMOVE)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Account modifiedBy;

    @Column(nullable = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
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
