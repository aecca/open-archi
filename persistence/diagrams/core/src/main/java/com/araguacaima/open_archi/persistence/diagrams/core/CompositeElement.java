package com.araguacaima.open_archi.persistence.diagrams.core;

import com.araguacaima.open_archi.persistence.meta.MetaInfo;
import com.araguacaima.open_archi.persistence.meta.Version;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "CompositeElement", schema = "DIAGRAMS")
@DynamicUpdate
public class CompositeElement<T extends ElementKind> implements Comparable<CompositeElement<T>> {

    @Id
    @NotNull
    @Column(name = "Id")
    private String id;

    @Column
    @Type(type = "com.araguacaima.open_archi.persistence.diagrams.core.ElementKind")
    private T type;

    @Column
    private String link;

    @OneToOne
    private Version version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        if (StringUtils.isNotBlank(this.id)) {
            setLink("/models/" + this.id);
        } else {
            setLink(null);
        }
    }

    public T getType() {
        return type;
    }

    public void setType(T type) {
        this.type = type;
    }

    public CompositeElement() {
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public static CompositeElement<ElementKind> fromItem(Item item) {
        CompositeElement<ElementKind> compositeElement = new CompositeElement<>();
        MetaInfo meta = item.getMeta();
        if (meta != null) {
            compositeElement.setVersion(meta.getActiveVersion());
        }
        compositeElement.setType(item.getKind());
        compositeElement.setId(item.getId());
        return compositeElement;
    }

    public void override(CompositeElement compositeElement, boolean keepMeta, String suffix) {
        this.id = compositeElement.getId();
        this.type = (T) compositeElement.getType();
        this.link = compositeElement.getLink();
        Version version = compositeElement.getVersion();
        if (version != null) {
            Version version_ = new Version();
            version_.override(version, keepMeta, suffix);
            this.version = version_;
        }
    }

    public void copyNonEmpty(CompositeElement compositeElement, boolean keepMeta) {
        if (StringUtils.isNotBlank(compositeElement.getId())) {
            this.id = compositeElement.getId();
        }
        this.type = (T) compositeElement.getType();
        if (StringUtils.isNotBlank(compositeElement.getLink())) {
            this.link = compositeElement.getLink();
        }
        Version version = compositeElement.getVersion();
        if (version != null) {
            Version version_ = new Version();
            version_.copyNonEmpty(version, keepMeta);
            this.version = version_;
        }
    }

    @Override
    public int compareTo(CompositeElement<T> o) {
        if (o == null) {
            return 0;
        } else {
            if (o.getType().equals(this.type)) {
                if (o.getId().equals(this.id)) {
                    return o.getVersion().compareTo(this.version);
                } else {
                    return -1;
                }
            } else {
                return -1;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompositeElement<?> that = (CompositeElement<?>) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        return getType() != null ? getType().equals(that.getType()) : that.getType() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        return result;
    }
}
