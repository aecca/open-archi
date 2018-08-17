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
import org.apache.commons.lang3.StringUtils;
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

    @Transient
    @JsonIgnore
    private static ReflectionUtils reflectionUtils = new ReflectionUtils(null);

    private static final long serialVersionUID = 5449758397914117108L;

    @Transient
    @JsonIgnore
    private static SpecificationMapBuilder specificationMapBuilder = new SpecificationMapBuilder(MapUtils.getInstance());

    @Transient
    protected String key;

    @Id
    @NotNull
    @Column(name = "Id")
    protected String id;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private MetaInfo meta;

    public BaseEntity() {
        this.id = generateId();
    }

    @JsonIgnore
    private String generateId() {
        return UUID.randomUUID().toString();
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
    @JsonIgnore
    public void validateRequest() throws EntityError {
        //Do nothing. All request are valid on this entity
    }

    @Override
    @JsonIgnore
    public void validateCreation() throws EntityError {
        Map<String, Object> map = new HashMap<>();
        map.put("OperationType", OperationType.CREATION);
        map.put("Initiator", this);
        traverse(this, "validateCreation", map);
    }

    @Override
    @JsonIgnore
    public void validateModification() throws EntityError {
        Map<String, Object> map = new HashMap<>();
        map.put("OperationType", OperationType.MODIFICATION);
        map.put("Initiator", this);
        traverse(this, "validateModification", map);
    }

    @Override
    @JsonIgnore
    public void validateReplacement() throws EntityError {
        Map<String, Object> map = new HashMap<>();
        map.put("OperationType", OperationType.REPLACEMENT);
        map.put("Initiator", this);
        traverse(this, "validateReplacement", map);
    }

    @JsonIgnore
    private void traverse(Object entity, String method, Map map) {
        Class<?> clazz = entity.getClass();
        ReflectionUtils.doWithFields(clazz, field -> {
            field.setAccessible(true);
            Object object_ = field.get(entity);
            Class<?> clazz_ = field.getType();
            if (ReflectionUtils.isCollectionImplementation(clazz_) && object_ != null) {
                Collection collection = (Collection) object_;
                if (!collection.isEmpty()) {
                    for (Object innerCollection : collection) {
                        traverse(innerCollection, method, map);
                    }
                }
            } else if (ReflectionUtils.isMapImplementation(clazz_) && object_ != null) {
                Map<Object, Object> map_ = (Map<Object, Object>) object_;
                Set<Map.Entry<Object, Object>> set = map_.entrySet();
                if (!map_.isEmpty()) {
                    for (Map.Entry innerMapValues : set) {
                        traverse(innerMapValues.getValue(), method, map);
                    }
                }
            } else {
                if (object_ != null) {
                    processSpecification(method, object_, clazz_, map);
                }
            }
        }, Utils::filterMethod);
        processSpecification(method, entity, clazz, map);
    }

    @JsonIgnore
    private void processSpecification(String method, Object object_, Class<?> clazz_, Map map) {
        try {
            if (reflectionUtils.getFullyQualifiedJavaTypeOrNull(clazz_) == null) {
                List<SpecificationMap> specificationMaps = specificationMapBuilder.getInstances(clazz_, true);
                boolean specificationResult = true;
                for (SpecificationMap specificationMap : specificationMaps) {
                    Specification specification = specificationMap.getSpecificationFromMethod(method);
                    if (specification != null) {
                        specificationResult = specificationResult && specification.isSatisfiedBy(object_, map);
                    }
                }
                if (!specificationResult) {
                    throw new EntityError(map.get(Constants.SPECIFICATION_ERROR).toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntityError(e.getMessage(), e);
        }
    }

    @JsonIgnore
    public void override(BaseEntity source, boolean keepMeta, String suffix) {
        if (source.getMeta() != null) {
            if (keepMeta) {
                this.meta = source.getMeta();
            } else {
                this.meta = buildDefaultMeta();
            }
        } else {
            this.meta = null;
        }
        if (StringUtils.isNotBlank(source.getId())) {
            this.key = source.getId();
        }
    }

    @JsonIgnore
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

    @JsonIgnore
    private MetaInfo buildDefaultMeta() {
        MetaInfo meta = new MetaInfo();
        Date time = Calendar.getInstance().getTime();
        meta.setCreated(time);
        History history = new History(time);
        history.setVersion(new Version());
        meta.addHistory(history);
        return meta;
    }

}