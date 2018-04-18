package com.araguacaima.open_archi.persistence.meta;

import com.araguacaima.commons.utils.MapUtils;
import com.araguacaima.commons.utils.ReflectionUtils;
import com.araguacaima.open_archi.persistence.commons.Constants;
import com.araguacaima.open_archi.persistence.commons.OperationType;
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
        Map<String, OperationType> map = new HashMap<>();
        map.put("OperationType", OperationType.CREATION);
        traverse(this, "validateCreation", map);
    }

    @Override
    public void validateModification() throws EntityError {
        Map<String, OperationType> map = new HashMap<>();
        map.put("OperationType", OperationType.MODIFICATION);
        traverse(this, "validateModification", map);
    }

    @Override
    public void validateReplacement() throws EntityError {
        Map<String, OperationType> map = new HashMap<>();
        map.put("OperationType", OperationType.REPLACEMENT);
        traverse(this, "validateReplacement", map);
    }

    private void traverse(Object entity, String method, Map map) {
        Class<?> clazz = entity.getClass();
        ReflectionUtils.doWithFields(clazz, field -> {
            field.setAccessible(true);
            Object object_ = field.get(entity);
            Class<?> clazz_ = field.getType();
            if (ReflectionUtils.isCollectionImplementation(clazz_) && object_ != null) {
                Collection collection = (Collection) object_;
                if (collection.isEmpty()) {
                    processSpecification(method, object_, ReflectionUtils.extractGenerics(field), map);
                } else {
                    for (Object innerCollection : collection) {
                        traverse(innerCollection, method, map);
                    }
                }
            } else if (ReflectionUtils.isMapImplementation(clazz_) && object_ != null) {
                Map<Object, Object> map_ = (Map<Object, Object>) object_;
                Set<Map.Entry<Object, Object>> set = map_.entrySet();
                if (map_.isEmpty()) {
                    processSpecification(method, object_, ReflectionUtils.extractGenerics(field), map);
                } else {
                    for (Map.Entry innerMapValues : set) {
                        traverse(innerMapValues.getValue(), method, map);
                    }
                }
            } else {
                if (reflectionUtils.getFullyQualifiedJavaTypeOrNull(clazz_) == null && !clazz_.isEnum() && !Enum.class.isAssignableFrom(clazz_)) {
                    processSpecification(method, object_, clazz_, map);
                }
            }
        }, Utils::filterMethod);
        processSpecification(method, entity, clazz, map);
    }

    private void processSpecification(String method, Object object_, Class<?> clazz_, Map map) {
        try {
            SpecificationMap specificationMap = specificationMapBuilder.getInstance(clazz_, true);
            Specification specification = specificationMap.getSpecificationFromMethod(method);
            if (specification != null) {
                if (!specification.isSatisfiedBy(object_, map)) {
                    throw new EntityError(map.get(Constants.SPECIFICATION_ERROR).toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntityError(e.getMessage(), e);
        }
    }

    public void override(BaseEntity source, boolean keepMeta, String suffix) {
        if (source.getMeta() != null) {
            if (keepMeta) {
                this.meta = source.getMeta();
            } else {
                this.meta = buildDefaultMeta();
            }
        }
    }

    public void copyNonEmpty(BaseEntity source, boolean keepMeta) {
        if (source.getMeta() != null) {
            if (keepMeta) {
                this.meta = source.getMeta();
            } else {
                if (source.getMeta() != null) {
                    this.meta = buildDefaultMeta();
                }
            }
        }
    }

    private MetaInfo buildDefaultMeta() {
        MetaInfo meta = new MetaInfo();
        Date time = Calendar.getInstance().getTime();
        meta.setCreated(time);
        History history = new History(time);
        history.setVersion(new Version());
        meta.addHistory(history);
        return meta;
    }

    public void setId(String id) {
        this.id = id;
    }
}