package com.araguacaima.open_archi.persistence.diagrams.core;

import com.araguacaima.commons.utils.MapUtils;
import com.araguacaima.open_archi.persistence.commons.Constants;
import com.araguacaima.open_archi.persistence.commons.exceptions.EntityError;
import com.araguacaima.open_archi.persistence.meta.Valuable;
import com.araguacaima.open_archi.persistence.meta.Version;
import com.araguacaima.specification.Specification;
import com.araguacaima.specification.util.SpecificationMap;
import com.araguacaima.specification.util.SpecificationMapBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "CompositeElement", schema = "DIAGRAMS")
@DynamicUpdate
public class CompositeElement<T extends ElementKind> implements Valuable {

    private static SpecificationMapBuilder specificationMapBuilder = new SpecificationMapBuilder(MapUtils.getInstance());

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

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
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
        try {
            SpecificationMap specificationMap = specificationMapBuilder.getInstance(this.getClass(), true);
            Specification specification = specificationMap.getSpecificationFromMethod("validateModification");
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
    public void validateReplacement() throws EntityError {
        try {
            SpecificationMap specificationMap = specificationMapBuilder.getInstance(this.getClass(), true);
            Specification specification = specificationMap.getSpecificationFromMethod("validateReplacement");
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
}
