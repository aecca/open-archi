package com.araguacaima.open_archi.persistence.meta;

import com.araguacaima.commons.utils.MapUtils;
import com.araguacaima.open_archi.persistence.commons.Constants;
import com.araguacaima.open_archi.persistence.commons.exceptions.EntityError;
import com.araguacaima.specification.Specification;
import com.araguacaima.specification.util.SpecificationMap;
import com.araguacaima.specification.util.SpecificationMapBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

@MappedSuperclass
@PersistenceUnit(unitName = "open-archi")
@JsonIgnoreProperties(value = {"resourceBundle"})
@Component
public abstract class BaseEntity implements Serializable, BasicEntity, Cloneable {

    @Transient
    @JsonIgnore
    protected static final ResourceBundle resourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_NAME);

    private static final long serialVersionUID = 5449758397914117108L;
    private static SpecificationMapBuilder specificationMapBuilder = new SpecificationMapBuilder(MapUtils.getInstance());

    @Id
    @NotNull
    @Column(name = "Id")
    protected String id;

    @OneToOne
    private MetaInfo meta;

    public BaseEntity() {
        this.id = generateId();
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String getId() {
        return this.id;
    }

    public MetaInfo getMeta() {
        return meta;
    }

    public void setMeta(MetaInfo meta) {
        this.meta = meta;
    }

    @JsonIgnore
    protected String getRequestErrorMessageKey() {
        return this.getClass().getName().toLowerCase() + "-" + "request" + "." + "error";
    }

    @JsonIgnore
    protected String getModificationErrorMessageKey() {
        return this.getClass().getName().toLowerCase() + "-" + "modification" + "." + "error";
    }

    @JsonIgnore
    protected String getCreationErrorMessageKey() {
        return this.getClass().getName().toLowerCase() + "-" + "creation" + "." + "error";
    }

    @Override
    public void validateRequest() throws EntityError {
        //Do nothing. All request are valid on this entity
    }

    @Override
    public void validateCreation() throws EntityError {
        try {
            SpecificationMap specificationMap = specificationMapBuilder.getInstance(this.getClass(), true);
            Specification specification = specificationMap.getSpecificationFromMethod("validateCreation");
            if (specification != null) {
                Map map = new HashMap<>();
                if (!specification.isSatisfiedBy(this, map)) {
                    throw new EntityError(map.get("ERROR").toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntityError(e.getMessage(), e);
        }

    }

    @Override
    public void validateModification() throws EntityError {
        if (id != null) {
            throw new EntityError(resourceBundle.getString(this.getClass().getName() + "-" + "entity.identifier.cannot.be.modified"));
        }
    }

}