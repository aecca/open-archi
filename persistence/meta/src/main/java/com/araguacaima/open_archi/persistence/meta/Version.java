package com.araguacaima.open_archi.persistence.meta;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@PersistenceUnit(unitName = "open-archi")
@Entity
@Table(schema = "META", name = "Version")
@DynamicUpdate
@NamedQueries(value = {@NamedQuery(name = Version.COUNT_ALL_VERSIONS,
        query = "select count(a) from Version a"), @NamedQuery(name = Version.GET_DEFAULT_VERSION,
        query = "select a from Version a where a.major = 1 and a.minor = 0 and a.build = 0"), @NamedQuery(
        name = Version.GET_ALL_VERSIONS,
        query = "select a from Version a order by a.major, a.minor, a.build"), @NamedQuery(
        name = Version.FIND_VERSION,
        query = "select a from Version a where a.major = :major and a.minor = :minor and a.build = :build order by a.major, a.minor, a.build"), @NamedQuery(
        name = Version.GET_LAST_VERSION,
        query = "SELECT v1 "
                + "FROM Version v1 LEFT OUTER JOIN Version v2 ON ( "
                + "  (v1.major = v2.major AND v1.minor < v2.minor) OR (v1.major = v2.major AND v1.minor = v2.minor AND v1.build < v2.build) "
                + "  OR (v1.major < v2.major)) "
                + "WHERE v2.id IS NULL")})
public class Version extends BaseEntity implements Serializable, Comparable<Version>, Cloneable {

    public static final String GET_ALL_VERSIONS = "Version.getAllVersions";
    public static final String COUNT_ALL_VERSIONS = "Version.countAllVersions";
    public static final String GET_LAST_VERSION = "Version.getLastVersion";
    public static final String GET_DEFAULT_VERSION = "Version.getDefaultVersion";
    public static final String FIND_VERSION = "Version.findByMajorMinorAndBuild";
    public static final String FIND_BY_ID = "Version.findById";
    public static final String PARAM_ID = "id";
    public static final String PARAM_MAJOR = "major";
    public static final String PARAM_MINOR = "minor";
    public static final String PARAM_BUILD = "build";

    private static final long serialVersionUID = -5350803918802322500L;

    @Column(nullable = false)
    @NotNull
    private Integer major = 0;
    @Column(nullable = false)
    @NotNull
    private Integer minor = 0;
    @Column
    private Integer build = 1;

    @Column
    @Enumerated(EnumType.STRING)
    protected VersionStatus status;

    public Version() {
        this.status = VersionStatus.ACTIVE;
    }

    public Version(Integer major, Integer minor, Integer build) {
        this();
        this.major = major;
        this.minor = minor;
        this.build = build;
    }

    public Version(String version) throws NumberFormatException {
        this();
        if (StringUtils.isNotBlank(version)) {
            version = version.trim();
            String[] versionSplitted;

            versionSplitted = version.split("\\.");
            try {
                this.major = Integer.valueOf(versionSplitted[0]);
            } catch (IndexOutOfBoundsException ignored) {
            } catch (NumberFormatException nfe) {
                this.major = Integer.valueOf(versionSplitted[0].substring(0, 1));
            }
            try {
                this.minor = Integer.valueOf(versionSplitted[1]);
            } catch (IndexOutOfBoundsException ignored) {
            } catch (NumberFormatException nfe) {
                this.minor = Integer.valueOf(versionSplitted[1].substring(0, 1));
            }
            if (this.major == null) {
                versionSplitted = version.split(",");
                try {
                    this.major = Integer.valueOf(versionSplitted[0]);
                } catch (IndexOutOfBoundsException ignored) {
                } catch (NumberFormatException nfe) {
                    this.major = Integer.valueOf(versionSplitted[0].substring(0, 1));
                }
                try {
                    this.minor = Integer.valueOf(versionSplitted[1]);
                } catch (IndexOutOfBoundsException ignored) {
                } catch (NumberFormatException nfe) {
                    this.minor = Integer.valueOf(versionSplitted[1].substring(0, 1));
                }
            }
            try {
                this.build = Integer.valueOf(versionSplitted[2]);
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
    }

    public Integer getMajor() {
        return major;
    }

    public void setMajor(Integer major) {
        this.major = major;
    }

    public Integer getMinor() {
        return minor;
    }

    public void setMinor(Integer minor) {
        this.minor = minor;
    }

    public Integer getBuild() {
        return build;
    }

    public void setBuild(Integer build) {
        this.build = build;
    }

    public VersionStatus getStatus() {
        return status;
    }

    public void setStatus(VersionStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + build;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        final Version cloned = new Version();
        cloned.id = UUID.randomUUID().toString();
        cloned.setMajor(this.getMajor());
        cloned.setMinor(this.getMinor());
        cloned.setBuild(this.getBuild());
        return cloned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Version version = (Version) o;

        return new EqualsBuilder().append(id, version.id)
                .append(major, version.major)
                .append(minor, version.minor)
                .append(build, version.build)
                .isEquals();
    }

    public Version nextBuild() {
        if (build != null) {
            build = build++;
        } else {
            build = 1;
        }
        return this;
    }

    public Version nextMinor() {
        if (minor != null) {
            minor = minor++;
        } else {
            minor = 0;
        }
        return this;
    }

    public Version nextMajor() {
        if (major != null) {
            major = major++;
        } else {
            major = 0;
        }
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(major).append(minor).append(build).toHashCode();
    }

    @Override
    public int compareTo(Version other) {
        try {
            if (this.major.equals(other.major)) {
                if (this.minor.equals(other.minor)) {
                    return this.build - other.build;
                } else {
                    return this.minor - other.minor;
                }
            } else {
                return this.major - other.major;
            }
        } catch (Throwable ignored) {
            return -1;
        }
    }
}
