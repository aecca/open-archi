package com.araguacaima.open_archi.persistence.meta;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@PersistenceUnit(unitName = "open-archi")
@Entity
@Table(schema = "META", name = "History")
@DynamicUpdate
@NamedQueries(value = {
        @NamedQuery(
                name = History.GET_DEFAULT_HISTORY_VERSION,
                query = "select a from History a where a.version.major = 1 and a.version.minor = 0 and a.version.build = 0 order by a.version"),
        @NamedQuery(
                name = History.GET_DEFAULT_ACTIVE_VERSION,
                query = "select a from History a where a.version.status = 'ACTIVE'")})
public class History implements Serializable, Comparable<History> {

    public static final String GET_DEFAULT_HISTORY_VERSION = "get.default.history.version";
    public static final String GET_DEFAULT_ACTIVE_VERSION = "get.default.active.version";

    @Id
    private String id;

    @NotNull
    @ManyToOne
    private Version version;

    public Version getVersion() {
        return version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    @Override
    public int compareTo(History o) {

        //TODO completar
        return this.version.compareTo(o.getVersion());
    }
}
