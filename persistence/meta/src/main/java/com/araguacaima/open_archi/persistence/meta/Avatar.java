package com.araguacaima.open_archi.persistence.meta;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "Avatar", schema = "META")
@DynamicUpdate
public class Avatar {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "Id", updatable = false, nullable = false)
    private UUID id;

    @Column
    @Convert(converter = MimeTypeConverter.class)
    @JsonDeserialize(using = MimeTypeDeserializer.class)
    private MimeType type;

    @Column
    @Lob
    private String raw;

    @Column
    private String url;

    public Avatar(MimeType type) {
        this.type = type;
    }

    public Avatar() {
    }

    public MimeType getType() {
        return type;
    }

    public void setType(MimeType type) {
        this.type = type;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
