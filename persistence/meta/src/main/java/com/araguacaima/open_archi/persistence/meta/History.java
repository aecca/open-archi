package com.araguacaima.open_archi.persistence.meta;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@PersistenceUnit(unitName = "open-archi")
@Entity
@Table(schema = "META", name = "History")
@DynamicUpdate
@NamedQueries(value = {
        @NamedQuery(
                name = History.GET_DEFAULT_HISTORY_VERSION,
                query = "select a from History a where a.version.id.major = 1 and a.version.id.minor = 0 and a.version.id.build = 0 order by a.version"),
        @NamedQuery(
                name = History.GET_DEFAULT_ACTIVE_VERSION,
                query = "select a from History a where a.status = 'ACTIVE'")})
public class History implements Serializable, Comparable<History>, SimpleOverridable<History> {

    public static final String GET_DEFAULT_HISTORY_VERSION = "get.default.history.version";
    public static final String GET_DEFAULT_ACTIVE_VERSION = "get.default.active.version";

    @Id
    private String id;

    @NotNull
    @ManyToOne
    private Version version;

    @Column
    @Enumerated(EnumType.STRING)
    private VersionStatus status;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Account modifiedBy;

    public History() {
        this.id = UUID.randomUUID().toString();
        this.status = VersionStatus.INITIAL;
    }


    public History(Date modified) {
        this();
        this.modified = modified;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getModified() {
        return modified;
    }

    public Account getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Account modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public VersionStatus getStatus() {
        return status;
    }

    public void setStatus(VersionStatus status) {
        this.status = status;
    }

    @Override
    public int compareTo(History o) {
        int compare = this.version.compareTo(o.getVersion());
        if (compare == 0) {
            if (this.modified == null || o.getModified() == null) {
                return 0;
            }
            return this.modified.compareTo(o.getModified());
        } else {
            return compare;
        }
    }

    @Override
    public void override(History source, boolean keepMeta, String suffix) {
        this.id = source.getId();
        if (source.getVersion() != null) {
            Version version_ = new Version();
            version_.override(source.getVersion(), keepMeta, suffix);
            this.version = version_;
        }
        this.status = source.getStatus();
        this.modified = source.modified;
        if (source.getModifiedBy() != null) {
            Account account_ = new Account();
            account_.override(source.getModifiedBy(), keepMeta, suffix);
            this.modifiedBy = account_;
        } else {
            this.modifiedBy = null;
        }
    }

    @Override
    public void copyNonEmpty(History source, boolean keepMeta) {
        if (StringUtils.isNotBlank(source.getId())) {
            this.id = source.getId();
        }
        if (source.getVersion() != null) {
            Version version_ = new Version();
            version_.copyNonEmpty(source.getVersion(), keepMeta);
            this.version = version_;
        }
        this.status = source.getStatus();
        if (source.getModified() != null) {
            this.modified = source.getModified();
        }
        if (source.getModifiedBy() != null) {
            Account account_ = new Account();
            account_.copyNonEmpty(source.getModifiedBy(), keepMeta);
            this.modifiedBy = account_;
        }
    }
}
