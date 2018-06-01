package com.araguacaima.open_archi.persistence.diagrams.core;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "Image", schema = "DIAGRAMS")
@DynamicUpdate
public class Image extends BaseEntity {

    @Column
    @Enumerated(EnumType.STRING)
    private MimeType type;
    @Column
    private byte[] raw = new byte[0];
    @Column
    private String url;

    public Image(MimeType type) {
        this.type = type;
    }

    public Image() {
    }

    public MimeType getType() {
        return type;
    }

    public void setType(MimeType type) {
        this.type = type;
    }

    public byte[] getRaw() {
        return raw;
    }

    public void setRaw(byte[] raw) {
        this.raw = raw;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void override(Image source, boolean keepMeta, String suffix) {
        super.override(source, keepMeta, suffix);
        this.setType(source.getType());
        this.setUrl(source.getUrl());
        this.setRaw(source.getRaw());
    }

    public void copyNonEmpty(Image source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getType() != null) {
            this.setType(source.getType());
        }
        if (source.getUrl() != null) {
            this.setUrl(source.getUrl());
        }
        if (source.getRaw() != null) {
            this.setRaw(source.getRaw());
        }
    }
}
