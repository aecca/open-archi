package com.araguacaima.open_archi.persistence.meta;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

/**
 * Created by Alejandro on 19/12/2014.
 */
@PersistenceUnit(unitName = "open-archi")
@Entity
@Table(schema = "META", name = "MetaInfo")
@DynamicUpdate
@NamedQueries(value = {@NamedQuery(name = MetaInfo.COUNT_ALL_META_INFO,
        query = "select count(a) from MetaInfo a"), @NamedQuery(
        name = MetaInfo.GET_ALL_META_INFO,
        query = "select a from MetaInfo a order by a.created"),
        @NamedQuery(name = MetaInfo.GET_META_INFO_BY_VERSION,
                query = "select m " +
                        "from MetaInfo m " +
                        "   inner join m.history h " +
                        "   inner join h.version v " +
                        "where v = :version")})
public class MetaInfo implements Serializable, Comparable<MetaInfo>, SimpleOverridable<MetaInfo> {

    public static final String GET_ALL_META_INFO = "MetaInfo.getAllMetaInfo";
    public static final String COUNT_ALL_META_INFO = "MetaInfo.countAllMetaInfo";
    public static final String GET_META_INFO_BY_VERSION = "get.metainfo.by.version";

    @Id
    private String id;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "META",
            name = "MetaInfo_History_Ids",
            joinColumns = {@JoinColumn(name = "MetaInfo_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "History_Id",
                    referencedColumnName = "Id")})
    private Set<History> history = new TreeSet<>();

    @NotNull
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Account createdBy;

    @Column(nullable = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    public MetaInfo() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Account getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Account createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Set<History> getHistory() {
        return history;
    }

    public void setHistory(Set<History> history) {
        this.history = history;
    }

    public void addHistory(History history) {
        this.history.add(history);
    }

    public void addNewHistory(Date time) {
        History history = new History(time);
        history.setVersion(new Version());
        this.history.add(history);
    }

    public void addNewHistory(Date time, Account modifiedBy) {
        History history = new History(time);
        history.setVersion(new Version());
        history.setModifiedBy(modifiedBy);
        this.history.add(history);
    }

    @JsonIgnore
    public Version getActiveVersion() {
        return getActiveHistory().getVersion();
    }

    @JsonIgnore
    public History getActiveHistory() {
        History history = IterableUtils.find(this.history, object -> VersionStatus.ACTIVE.equals(object.getStatus()));
        if (history == null) {
            history = new History();
            history.setVersion(new Version());
        }
        return history;
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
                .append(history, metaInfo.history)
                .append(createdBy, metaInfo.createdBy)
                .append(created, metaInfo.created)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id)
                .append(history)
                .append(createdBy)
                .append(created)
                .toHashCode();
    }

    @Override
    public int compareTo(MetaInfo o) {
        if (o == null) {
            return 0;
        }
        return this.created.compareTo(o.getCreated());
    }

    @Override
    public void override(MetaInfo source, boolean keepMeta, String suffix) {
        this.id = source.getId();
        if (source.getHistory() != null) {
            source.getHistory().forEach(history -> {
                History history_ = new History();
                history_.override(history, keepMeta, suffix);
                this.history.add(history_);
            });
        }
        if (source.getCreatedBy() == null) {
            this.createdBy = null;
        } else {
            this.createdBy.override(source.getCreatedBy(), keepMeta, suffix);
        }
        this.created = source.getCreated();
    }

    @Override
    public void copyNonEmpty(MetaInfo source, boolean keepMeta) {
        if (StringUtils.isNotBlank(source.getId())) {
            this.id = source.getId();
        }
        if (source.getHistory() != null) {
            source.getHistory().forEach(history -> {
                History history_ = new History();
                history_.copyNonEmpty(history, keepMeta);
                this.history.add(history_);
            });
        }
        if (source.getCreatedBy() != null) {
            this.createdBy.copyNonEmpty(source.getCreatedBy(), keepMeta);
        }
        if (source.getCreated() != null) {
            this.created = source.getCreated();
        }
    }
}
