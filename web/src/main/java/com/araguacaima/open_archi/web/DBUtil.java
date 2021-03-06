package com.araguacaima.open_archi.web;

import com.araguacaima.commons.utils.ReflectionUtils;
import com.araguacaima.open_archi.persistence.commons.Utils;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import com.araguacaima.open_archi.persistence.meta.BasicEntity;
import com.araguacaima.orpheusdb.utils.OrpheusDbJPAEntityManagerUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.lang.reflect.Field;
import java.util.*;


@SuppressWarnings({"WeakerAccess", "MismatchedQueryAndUpdateOfCollection"})
public class DBUtil {

    private static final Logger log = LoggerFactory.getLogger(DBUtil.class);
    private static Set<Class<? extends BaseEntity>> classes = new HashSet<>();
    private static Set<Object> persistedObjects = new HashSet<>();
    private static ReflectionUtils reflectionUtils = new ReflectionUtils(null);

    static {
        classes.add(com.araguacaima.open_archi.persistence.diagrams.architectural.Model.class);
        classes.add(com.araguacaima.open_archi.persistence.diagrams.bpm.Model.class);
        classes.add(com.araguacaima.open_archi.persistence.diagrams.er.Model.class);
        classes.add(com.araguacaima.open_archi.persistence.diagrams.flowchart.Model.class);
        classes.add(com.araguacaima.open_archi.persistence.diagrams.gantt.Model.class);
        classes.add(com.araguacaima.open_archi.persistence.diagrams.sequence.Model.class);
        classes.add(com.araguacaima.open_archi.persistence.diagrams.classes.Model.class);
        OrpheusDbJPAEntityManagerUtils.begin();
    }

    public DBUtil() {
    }

    private static void processInnerIterable(Object result) {
        if (result != null) {
            if (!persistedObjects.contains(result)) {
                Object entity = OrpheusDbJPAEntityManagerUtils.merge(result);
                persistedObjects.add(entity);
            } else {
                logProcessing(result);
            }
        }
    }

    private static void processInnerComplex(Object value, Class<?> type) {
        if (reflectionUtils.getFullyQualifiedJavaTypeOrNull(type) == null && !type.isEnum() && !Enum.class.isAssignableFrom(type)) {
            if (!persistedObjects.contains(value)) {
                Object entity_ = OrpheusDbJPAEntityManagerUtils.merge(value);
                persistedObjects.add(entity_);
            } else {
                logProcessing(value);
            }
        }
    }

    private static void logProcessing(Object object) {
        if (log.isDebugEnabled()) {
            Object id = reflectionUtils.invokeGetter(object, "id");
            String type = object.getClass().getSimpleName();
            Field field = reflectionUtils.getFieldByFieldName(object, "name");
            if (field != null) {
                Object name = null;
                try {
                    field.setAccessible(true);
                    name = field.get(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                log.debug(" Entity of type '" + type + "' with name '" + name + "' is already processed");
            } else {
                log.debug(" Entity of type '" + type + "' with id '" + id + "' is already processed");
            }
        }
    }

    public static void replace(BaseEntity entity) throws Throwable {
        Replace.replace(entity);
    }

    public static void populate(BaseEntity entity) throws Throwable {
        Populate.populate(entity);
    }

    public static void populate(BaseEntity entity, boolean flattern) throws Throwable {
        Populate.populate(entity, flattern);
    }

    public static void update(BaseEntity entity) throws Throwable {
        boolean autocommit = OrpheusDbJPAEntityManagerUtils.getAutocommit();
        OrpheusDbJPAEntityManagerUtils.setAutocommit(false);
        OrpheusDbJPAEntityManagerUtils.begin();
        Class<?> clazz = entity.getClass();
        Object persistedEntity = OrpheusDbJPAEntityManagerUtils.find(clazz, entity.getId());
        try {
            if (persistedEntity == null) {
                throw new EntityNotFoundException("Can not replace due object with id '" + entity.getId() + "' does not exists");
            }
            reflectionUtils.invokeMethod(persistedEntity, "copyNonEmpty", new Object[]{entity, true});
            OrpheusDbJPAEntityManagerUtils.update(persistedEntity);
        } catch (Throwable t) {
            OrpheusDbJPAEntityManagerUtils.rollback();
            throw t;
        } finally {
            OrpheusDbJPAEntityManagerUtils.commit();
            OrpheusDbJPAEntityManagerUtils.setAutocommit(autocommit);
        }
    }

    public static void persist(com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement entity) {
        boolean autocommit = OrpheusDbJPAEntityManagerUtils.getAutocommit();
        OrpheusDbJPAEntityManagerUtils.setAutocommit(false);
        OrpheusDbJPAEntityManagerUtils.begin();
        try {
            OrpheusDbJPAEntityManagerUtils.persist(entity);
        } catch (Throwable t) {
            OrpheusDbJPAEntityManagerUtils.rollback();
            throw t;
        } finally {
            OrpheusDbJPAEntityManagerUtils.commit();
            OrpheusDbJPAEntityManagerUtils.setAutocommit(autocommit);
        }
    }

    public static void delete(Class<?> clazz, String key) throws Throwable {
        Object entity = OrpheusDbJPAEntityManagerUtils.find(clazz, (String) key);
        OrpheusDbJPAEntityManagerUtils.delete(entity);
    }

    private static class Populate {
        public static void populate(BaseEntity entity, boolean persists) throws Throwable {
            populate(entity, true, persists);
        }

        public static void populate(BaseEntity entity) throws Throwable {
            populate(entity, true, true);
        }

        public static void populate(BaseEntity entity, boolean flatten, boolean persists) throws Throwable {
            boolean autocommit = OrpheusDbJPAEntityManagerUtils.getAutocommit();
            OrpheusDbJPAEntityManagerUtils.setAutocommit(false);
            OrpheusDbJPAEntityManagerUtils.begin();
            if (flatten) {
                flattenForPopulation(entity);
            }
            try {
                Class clazz = entity.getClass();
                populate(entity, clazz);
                if (persists) {
                    OrpheusDbJPAEntityManagerUtils.commit();
                }
            } catch (Throwable t) {
                OrpheusDbJPAEntityManagerUtils.rollback();
                throw t;
            } finally {
                OrpheusDbJPAEntityManagerUtils.setAutocommit(autocommit);
                persistedObjects.clear();
            }
        }

        private static void populate(BaseEntity entity, Class clazz) {
            ReflectionUtils.doWithFields(clazz, field -> {
                field.setAccessible(true);
                Object object_ = field.get(entity);
                if (object_ != null) {
                    Object result = populate(field.getType(), object_);
                    field.set(entity, result);
                }
            }, Utils::filterMethod);
            if (!persistedObjects.contains(entity)) {
                OrpheusDbJPAEntityManagerUtils.persist(entity);
                persistedObjects.add(entity);
            } else {
                logProcessing(entity);
            }
        }

        private static Object innerPopulation(Object entity) {
            ReflectionUtils.doWithFields(entity.getClass(), field -> {
                field.setAccessible(true);
                Object object_ = field.get(entity);
                if (object_ != null) {
                    Object result = populate(field.getType(), object_);
                    field.set(entity, result);
                }
            }, Utils::filterMethod);
            try {
                if (!persistedObjects.contains(entity)) {
                    OrpheusDbJPAEntityManagerUtils.persist(entity);
                    persistedObjects.add(entity);
                    OrpheusDbJPAEntityManagerUtils.flush();
                } else {
                    logProcessing(entity);
                }
            } catch (Throwable t) {
                if (!EntityExistsException.class.isAssignableFrom(t.getClass())) {
                    t.printStackTrace();
                }
                throw t;
            }
            return entity;
        }

        private static Object innerPopulationCreateIfNotExists(Object entity) {
            ReflectionUtils.doWithFields(entity.getClass(), field -> {
                field.setAccessible(true);
                Object object_ = field.get(entity);
                if (object_ != null) {
                    processCreateIfNotExists(field.getType(), object_);
                }
            }, Utils::filterMethod);
            try {
                Object id = reflectionUtils.invokeGetter(entity, "id");
                if (OrpheusDbJPAEntityManagerUtils.find(entity.getClass(), id) == null) {
                    if (!persistedObjects.contains(entity)) {
                        OrpheusDbJPAEntityManagerUtils.persist(entity);
                        persistedObjects.add(entity);
                    } else {
                        logProcessing(entity);
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
                throw t;
            }
            return entity;
        }

        @SuppressWarnings("unchecked")
        private static Object populate(Class<?> type, Object object_) {
            if (ReflectionUtils.isCollectionImplementation(type)) {
                Collection<Object> valuesToRemove = new ArrayList<>();
                for (Object innerCollection : (Collection) object_) {
                    Object value = innerPopulation(innerCollection);
                    if (!value.equals(innerCollection)) {
                        valuesToRemove.add(innerCollection);
                    }
                }
                ((Collection) object_).removeAll(valuesToRemove);
                return object_;
            } else if (ReflectionUtils.isMapImplementation(type)) {
                Map<Object, Object> map = (Map<Object, Object>) object_;
                Set<Map.Entry<Object, Object>> set = map.entrySet();
                for (Map.Entry innerMapValues : set) {
                    Object value = innerPopulation(innerMapValues.getValue());
                    map.put(innerMapValues.getKey(), value);
                }
                return map;
            } else {
                if (reflectionUtils.getFullyQualifiedJavaTypeOrNull(type) == null && !type.isEnum() && !Enum.class.isAssignableFrom(type)) {
                    if (BasicEntity.class.isAssignableFrom(type) || type.getAnnotation(Entity.class) != null) {
                        try {
                            return innerPopulation(object_);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
            }

            return object_;
        }

        public static void flattenForPopulation(Object entity) throws Throwable {
            Class clazz = entity.getClass();
            flattenForPopulation(entity, clazz);
        }

        private static void flattenForPopulation(Object entity, Class clazz) {
            ReflectionUtils.doWithFields(clazz, field -> {
                field.setAccessible(true);
                Object object_ = field.get(entity);
                if (object_ != null) {
                    processFieldFlatten(entity, field, object_);
                }
            }, Utils::filterMethod);
        }

        @SuppressWarnings("unchecked")
        private static Object flattenForPopulation(Class<?> type, Object object_) {
            if (ReflectionUtils.isCollectionImplementation(type)) {
                if (((Collection) object_).isEmpty()) {
                    return null;
                }
                Collection<Object> valuesToRemove = new ArrayList<>();
                Collection<Object> valuesToAdd = new ArrayList<>();
                for (Object innerCollection : (Collection) object_) {
                    Object value = innerFlatten(innerCollection);
                    valuesToRemove.add(innerCollection);
                    if (!persistedObjects.contains(value)) {
                        Object entity = OrpheusDbJPAEntityManagerUtils.merge(value);
                        persistedObjects.add(entity);
                    } else {
                        logProcessing(value);
                    }
                    valuesToAdd.add(value);
                }
                ((Collection) object_).removeAll(valuesToRemove);
                OrpheusDbJPAEntityManagerUtils.flush();
                ((Collection) object_).addAll(valuesToAdd);
                return object_;
            } else if (ReflectionUtils.isMapImplementation(type)) {
                Map<Object, Object> map = (Map<Object, Object>) object_;
                if (map.isEmpty()) {
                    return null;
                }
                Set<Map.Entry<Object, Object>> set = map.entrySet();
                for (Map.Entry innerMapValues : set) {
                    Object value = innerFlatten(innerMapValues.getValue());
                    if (!persistedObjects.contains(value)) {
                        Object entity = OrpheusDbJPAEntityManagerUtils.merge(value);
                        persistedObjects.add(entity);
                    } else {
                        logProcessing(value);
                    }
                    map.put(innerMapValues.getKey(), value);
                }
                return map;
            } else {
                if (reflectionUtils.getFullyQualifiedJavaTypeOrNull(type) == null && !type.isEnum() && !Enum.class.isAssignableFrom(type)) {
                    if (BasicEntity.class.isAssignableFrom(type) || type.getAnnotation(Entity.class) != null) {
                        try {
                            return innerFlatten(object_);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
            }

            return object_;
        }

        @SuppressWarnings("unchecked")
        private static void processCreateIfNotExists(Class<?> type, Object object_) {
            if (ReflectionUtils.isCollectionImplementation(type)) {
                for (Object innerCollection : (Collection) object_) {
                    Object result = innerPopulationCreateIfNotExists(innerCollection);
                    processInnerIterable(result);
                }
            } else if (ReflectionUtils.isMapImplementation(type)) {
                Map<Object, Object> map = (Map<Object, Object>) object_;
                Set<Map.Entry<Object, Object>> set = map.entrySet();
                for (Map.Entry innerMapValues : set) {
                    Object result = innerPopulationCreateIfNotExists(innerMapValues.getValue());
                    processInnerIterable(result);
                }
            }
        }

        @SuppressWarnings("unchecked")
        private static void processFieldFlatten(Object entity, Field field, Object object_) throws IllegalAccessException {
            if (object_ != null) {
                Object result = flattenForPopulation(field.getType(), object_);
                if (result != null) {
                    field.set(entity, result);
                    if (!ReflectionUtils.isCollectionImplementation(result.getClass()) && !ReflectionUtils.isMapImplementation(result.getClass())) {
                        Class<?> type = result.getClass();
                        processInnerComplex(result, type);
                    } else {
                        ((Collection) result).forEach(value -> {
                            Class<?> type = value.getClass();
                            processInnerComplex(value, type);
                        });
                    }
                }
            }
        }

        private static Object innerFlatten(Object entity) {
            ReflectionUtils.doWithFields(entity.getClass(), field -> {
                field.setAccessible(true);
                Object object_ = field.get(entity);
                if (object_ != null) {
                    if (!persistedObjects.contains(object_)) {
                        processFieldFlatten(entity, field, object_);
                    } else {
                        logProcessing(object_);
                    }
                }
            }, Utils::filterMethod);
            try {
                if (Item.class.isAssignableFrom(entity.getClass())) {
                    Map<String, Object> params = new HashMap<>();
                    String name = (String) reflectionUtils.invokeGetter(entity, "name");
                    params.put("name", name);
                    ElementKind kind = (ElementKind) reflectionUtils.invokeGetter(entity, "kind");
                    params.put("kind", kind);
                    Object entity_ = OrpheusDbJPAEntityManagerUtils.findByQuery(Item.class, Item.GET_ITEM_ID_BY_NAME, params);
                    if (entity_ == null) {
                        entity_ = OrpheusDbJPAEntityManagerUtils.find(entity);
                        if (entity_ != null) {
                            return entity_;
                        }
                    } else {
                        ((Item) entity_).override((Item) entity, false, null, null);
                        return entity_;
                    }
                } else {
                    Object entity_ = OrpheusDbJPAEntityManagerUtils.find(entity);
                    if (entity_ != null) {
                        Object[] args = new Object[]{entity, false, StringUtils.EMPTY};
                        reflectionUtils.invokeMethod(entity_, "override", args);
                        return entity_;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return entity;
        }

    }

    private static class Replace {

        public static void replace(BaseEntity entity) throws Throwable {
            replace(entity, true);
        }

        public static void replace(BaseEntity entity, boolean persists) throws Throwable {
            boolean autocommit = OrpheusDbJPAEntityManagerUtils.getAutocommit();
            OrpheusDbJPAEntityManagerUtils.setAutocommit(false);
            OrpheusDbJPAEntityManagerUtils.begin();

            try {
                Class clazz = entity.getClass();
                replace(entity, clazz);
                if (persists) {
                    OrpheusDbJPAEntityManagerUtils.commit();
                }
            } catch (Throwable t) {
                OrpheusDbJPAEntityManagerUtils.rollback();
                throw t;
            } finally {
                OrpheusDbJPAEntityManagerUtils.setAutocommit(autocommit);
                persistedObjects.clear();
            }
        }

        private static void replace(BaseEntity entity, Class clazz) {
            ReflectionUtils.doWithFields(clazz, field -> {
                field.setAccessible(true);
                Object object_ = field.get(entity);
                if (object_ != null) {
                    Object result = replace(field.getType(), object_);
                    field.set(entity, result);
                }
            }, Utils::filterMethod);
            if (!persistedObjects.contains(entity)) {
                OrpheusDbJPAEntityManagerUtils.merge(entity);
                persistedObjects.add(entity);
            } else {
                logProcessing(entity);
            }
        }

        private static Object innerReplacement(Object entity) {
            ReflectionUtils.doWithFields(entity.getClass(), field -> {
                field.setAccessible(true);
                Object object_ = field.get(entity);
                if (object_ != null) {
                    Object result = replace(field.getType(), object_);
                    field.set(entity, result);
                }
            }, Utils::filterMethod);
            try {
                if (!persistedObjects.contains(entity)) {
                    OrpheusDbJPAEntityManagerUtils.merge(entity);
                    persistedObjects.add(entity);
                } else {
                    logProcessing(entity);
                }
            } catch (Throwable t) {
                if (!EntityExistsException.class.isAssignableFrom(t.getClass())) {
                    t.printStackTrace();
                }
                throw t;
            }
            return entity;
        }

        private static Object innerReplacementCreateIfNotExists(Object entity) {
            ReflectionUtils.doWithFields(entity.getClass(), field -> {
                field.setAccessible(true);
                Object object_ = field.get(entity);
                if (object_ != null) {
                    processCreateIfNotExists(field.getType(), object_);
                }
            }, Utils::filterMethod);
            try {
                Object id = reflectionUtils.invokeGetter(entity, "id");
                if (OrpheusDbJPAEntityManagerUtils.find(entity.getClass(), id) == null) {
                    if (!persistedObjects.contains(entity)) {
                        OrpheusDbJPAEntityManagerUtils.merge(entity);
                        persistedObjects.add(entity);
                    } else {
                        logProcessing(entity);
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
                throw t;
            }
            return entity;
        }

        @SuppressWarnings("unchecked")
        private static Object replace(Class<?> type, Object object_) {
            if (ReflectionUtils.isCollectionImplementation(type)) {
                Collection<Object> valuesToRemove = new ArrayList<>();
                for (Object innerCollection : (Collection) object_) {
                    Object value = innerReplacement(innerCollection);
                    if (!value.equals(innerCollection)) {
                        valuesToRemove.add(innerCollection);
                    }
                }
                ((Collection) object_).removeAll(valuesToRemove);
                return object_;
            } else if (ReflectionUtils.isMapImplementation(type)) {
                Map<Object, Object> map = (Map<Object, Object>) object_;
                Set<Map.Entry<Object, Object>> set = map.entrySet();
                for (Map.Entry innerMapValues : set) {
                    Object value = innerReplacement(innerMapValues.getValue());
                    map.put(innerMapValues.getKey(), value);
                }
                return map;
            } else {
                if (reflectionUtils.getFullyQualifiedJavaTypeOrNull(type) == null && !type.isEnum() && !Enum.class.isAssignableFrom(type)) {
                    if (BasicEntity.class.isAssignableFrom(type) || type.getAnnotation(Entity.class) != null) {
                        try {
                            return innerReplacement(object_);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
            }

            return object_;
        }

        @SuppressWarnings("unchecked")
        private static Object flattenForReplacement(Class<?> type, Object object_) {
            if (ReflectionUtils.isCollectionImplementation(type)) {
                if (((Collection) object_).isEmpty()) {
                    return null;
                }
                Collection<Object> valuesToRemove = new ArrayList<>();
                Collection<Object> valuesToAdd = new ArrayList<>();
                for (Object innerCollection : (Collection) object_) {
                    Object value = innerFlatten(innerCollection);
                    valuesToRemove.add(innerCollection);
                    if (!persistedObjects.contains(value)) {
                        Object entity = OrpheusDbJPAEntityManagerUtils.merge(value);
                        persistedObjects.add(entity);
                    } else {
                        logProcessing(value);
                    }
                    valuesToAdd.add(value);
                }
                ((Collection) object_).removeAll(valuesToRemove);
                OrpheusDbJPAEntityManagerUtils.flush();
                ((Collection) object_).addAll(valuesToAdd);
                return object_;
            } else if (ReflectionUtils.isMapImplementation(type)) {
                Map<Object, Object> map = (Map<Object, Object>) object_;
                if (map.isEmpty()) {
                    return null;
                }
                Set<Map.Entry<Object, Object>> set = map.entrySet();
                for (Map.Entry innerMapValues : set) {
                    Object value = innerFlatten(innerMapValues.getValue());
                    if (!persistedObjects.contains(value)) {
                        Object entity = OrpheusDbJPAEntityManagerUtils.merge(value);
                        persistedObjects.add(entity);
                    } else {
                        logProcessing(value);
                    }
                    map.put(innerMapValues.getKey(), value);
                }
                return map;
            } else {
                if (reflectionUtils.getFullyQualifiedJavaTypeOrNull(type) == null && !type.isEnum() && !Enum.class.isAssignableFrom(type)) {
                    if (BasicEntity.class.isAssignableFrom(type) || type.getAnnotation(Entity.class) != null) {
                        try {
                            return innerFlatten(object_);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
            }

            return object_;
        }

        @SuppressWarnings("unchecked")
        private static void processCreateIfNotExists(Class<?> type, Object object_) {
            if (ReflectionUtils.isCollectionImplementation(type)) {
                for (Object innerCollection : (Collection) object_) {
                    Object result = innerReplacementCreateIfNotExists(innerCollection);
                    processInnerIterable(result);
                }
            } else if (ReflectionUtils.isMapImplementation(type)) {
                Map<Object, Object> map = (Map<Object, Object>) object_;
                Set<Map.Entry<Object, Object>> set = map.entrySet();
                for (Map.Entry innerMapValues : set) {
                    Object result = innerReplacementCreateIfNotExists(innerMapValues.getValue());
                    processInnerIterable(result);
                }
            }
        }

        @SuppressWarnings("unchecked")
        private static void processFieldFlatten(Object entity, Field field, Object object_) throws IllegalAccessException {
            if (object_ != null) {
                Object result = flattenForReplacement(field.getType(), object_);
                if (result != null) {
                    field.set(entity, result);
                    if (!ReflectionUtils.isCollectionImplementation(result.getClass()) && !ReflectionUtils.isMapImplementation(result.getClass())) {
                        Class<?> type = result.getClass();
                        processInnerComplex(result, type);
                    } else {
                        ((Collection) result).forEach(value -> {
                            Class<?> type = value.getClass();
                            processInnerComplex(value, type);
                        });
                    }
                }
            }
        }

        private static Object innerFlatten(Object entity) {
            ReflectionUtils.doWithFields(entity.getClass(), field -> {
                field.setAccessible(true);
                Object object_ = field.get(entity);
                if (object_ != null) {
                    if (!persistedObjects.contains(object_)) {
                        processFieldFlatten(entity, field, object_);
                    } else {
                        logProcessing(object_);
                    }
                }
            }, Utils::filterMethod);
            try {
                if (Item.class.isAssignableFrom(entity.getClass())) {
                    Map<String, Object> params = new HashMap<>();
                    String name = (String) reflectionUtils.invokeGetter(entity, "name");
                    params.put("name", name);
                    ElementKind kind = (ElementKind) reflectionUtils.invokeGetter(entity, "kind");
                    params.put("kind", kind);
                    Object entity_ = OrpheusDbJPAEntityManagerUtils.findByQuery(Item.class, Item.GET_ITEM_ID_BY_NAME, params);
                    if (entity_ == null) {
                        entity_ = OrpheusDbJPAEntityManagerUtils.find(entity);
                        if (entity_ != null) {
                            return entity_;
                        }
                    } else {
                        ((Item) entity_).override((Item) entity, false, null, null);
                        return entity_;
                    }
                } else {
                    Object entity_ = OrpheusDbJPAEntityManagerUtils.find(entity);
                    if (entity_ != null) {
                        Object[] args = new Object[]{entity, false, StringUtils.EMPTY};
                        reflectionUtils.invokeMethod(entity_, "override", args);
                        return entity_;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return entity;
        }

    }
}
