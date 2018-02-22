package com.araguacaima.open_archi.persistence.diagrams.core;

import com.araguacaima.commons.utils.MapUtils;
import com.araguacaima.open_archi.persistence.commons.Constants;
import com.araguacaima.open_archi.persistence.commons.exceptions.EntityError;
import com.araguacaima.open_archi.persistence.meta.Valuable;
import com.araguacaima.specification.Specification;
import com.araguacaima.specification.util.SpecificationMap;
import com.araguacaima.specification.util.SpecificationMapBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "CompositeElement", schema = "DIAGRAMS")
public class CompositeElement<T extends ElementKind> implements Valuable {


    @Transient
    @JsonIgnore
    protected static final ResourceBundle resourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_NAME);
    private static SpecificationMapBuilder specificationMapBuilder = new SpecificationMapBuilder(MapUtils.getInstance());
    @Id
    @NotNull
    @Column(name = "Id")
    protected String id;

    @Column
    @Type(type = "com.araguacaima.open_archi.persistence.diagrams.core.ElementKind")
    protected T type;

    @Column
    protected String link;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T getType() {
        return type;
    }

    public void setType(T type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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
