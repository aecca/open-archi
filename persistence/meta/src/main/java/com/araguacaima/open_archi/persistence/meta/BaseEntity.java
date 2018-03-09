package com.araguacaima.open_archi.persistence.meta;

import com.araguacaima.commons.utils.MapUtils;
import com.araguacaima.commons.utils.ReflectionUtils;
import com.araguacaima.open_archi.persistence.commons.Constants;
import com.araguacaima.open_archi.persistence.commons.Utils;
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
import java.util.*;

@MappedSuperclass
@PersistenceUnit(unitName = "open-archi")
@JsonIgnoreProperties(value = {"resourceBundle"})
@Component
public abstract class BaseEntity implements Serializable, BasicEntity, Cloneable {

    @Transient
    @JsonIgnore
    protected static final ResourceBundle resourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_NAME);

    private static ReflectionUtils reflectionUtils = new ReflectionUtils(null);

    private static final long serialVersionUID = 5449758397914117108L;
    private static SpecificationMapBuilder specificationMapBuilder = new SpecificationMapBuilder(MapUtils.getInstance());

    @Id
    @NotNull
    @Column(name = "Id")
    protected String id;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
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
        traverse(this, "validateCreation");
    }

    @Override
    public void validateModification() throws EntityError {
        traverse(this, "validateModification");
    }

    @Override
    public void validateReplacement() throws EntityError {
        traverse(this, "validateReplacement");
    }

    private void traverse(Object entity, String method) {
        Class<?> clazz = entity.getClass();
        ReflectionUtils.doWithFields(clazz, field -> {
            field.setAccessible(true);
            Object object_ = field.get(entity);
            Class<?> clazz_ = field.getType();
            if (ReflectionUtils.isCollectionImplementation(clazz_) && object_ != null) {
                for (Object innerCollection : (Collection) object_) {
                    traverse(innerCollection, method);
                }
            } else if (ReflectionUtils.isMapImplementation(clazz_) && object_ != null) {
                Map<Object, Object> map = (Map<Object, Object>) object_;
                Set<Map.Entry<Object, Object>> set = map.entrySet();
                for (Map.Entry innerMapValues : set) {
                    traverse(innerMapValues.getValue(), method);
                }
            } else {
                if (reflectionUtils.getFullyQualifiedJavaTypeOrNull(clazz_) == null && !clazz_.isEnum() && !Enum.class.isAssignableFrom(clazz_)) {
                    processSpecification(method, object_, clazz_);
                }
            }
        }, Utils::filterMethod);
        processSpecification(method, entity, clazz);
    }

    private void processSpecification(String method, Object object_, Class<?> clazz_) {
        try {
            SpecificationMap specificationMap = specificationMapBuilder.getInstance(clazz_, true);
            Specification specification = specificationMap.getSpecificationFromMethod(method);
            if (specification != null) {
                Map map = new HashMap<>();
                if (!specification.isSatisfiedBy(object_, map)) {
                    throw new EntityError(map.get("ERROR").toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntityError(e.getMessage(), e);
        }
    }

    public void override(BaseEntity source) {
        this.meta = source.getMeta();
    }


    public void copyNonEmpty(BaseEntity source) {
        if (source.getMeta() != null) {
            this.meta = source.getMeta();
        }
    }

    public void setId(String id) {
        this.id = id;
    }
}