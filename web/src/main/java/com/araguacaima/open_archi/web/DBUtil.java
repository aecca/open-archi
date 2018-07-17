
package com.araguacaima.open_archi.web;

import com.araguacaima.commons.utils.ReflectionUtils;
import com.araguacaima.open_archi.persistence.commons.Utils;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import com.araguacaima.open_archi.persistence.meta.BasicEntity;
import com.araguacaima.open_archi.persistence.utils.JPAEntityManagerUtils;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static java.nio.charset.Charset.forName;


@SuppressWarnings("WeakerAccess")
public class DBUtil {

    private static final Logger log = LoggerFactory.getLogger(DBUtil.class);
    private static Set<Class<? extends BaseEntity>> classes = new HashSet<>();
    private static Set<Object> persistedObjects = new HashSet<>();
    private static ReflectionUtils reflectionUtils = new ReflectionUtils(null);
    private static EnhancedRandomBuilder randomBuilder;
    private static LocalTime timeLower = LocalTime.of(0, 0);
    private static LocalTime timeUpper = LocalTime.of(0, 0);
    private static LocalDate dateLower = LocalDate.of(2000, 1, 1);
    private static LocalDate dateUpper = LocalDate.of(2040, 12, 31);

    public DBUtil() {
    }

    static {
        classes.add(com.araguacaima.open_archi.persistence.diagrams.architectural.Model.class);
        classes.add(com.araguacaima.open_archi.persistence.diagrams.bpm.Model.class);
        classes.add(com.araguacaima.open_archi.persistence.diagrams.er.Model.class);
        classes.add(com.araguacaima.open_archi.persistence.diagrams.flowchart.Model.class);
        classes.add(com.araguacaima.open_archi.persistence.diagrams.gantt.Model.class);
        classes.add(com.araguacaima.open_archi.persistence.diagrams.sequence.Model.class);
        classes.add(com.araguacaima.open_archi.persistence.diagrams.classes.Model.class);
        randomBuilder = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
                .seed(123L)
                .objectPoolSize(100)
                .charset(forName("UTF-8"))
                .timeRange(timeLower, timeUpper)
                .dateRange(dateLower, dateUpper)
                .stringLengthRange(5, 20)
                .collectionSizeRange(1, 5)
                .scanClasspathForConcreteTypes(true)
                .overrideDefaultInitialization(true);
        JPAEntityManagerUtils.begin();
    }

    public static void dbPopulation() {
        JPAEntityManagerUtils.begin();
        try {
            EnhancedRandom enhancedRandom = randomBuilder.build();
            for (Class<? extends BaseEntity> clazz : classes) {
                int next = new Random().nextInt((4 - 1) + 1) + 1;
                for (int i = 0; i < next; i++) {
                    BaseEntity entity = enhancedRandom.nextObject(clazz);
                    populate(entity, clazz);
                }
            }
        } catch (Throwable ignored) {
            JPAEntityManagerUtils.rollback();
        } finally {
            JPAEntityManagerUtils.commit();
        }
    }


    public static void populate(BaseEntity entity) throws Throwable {
        populate(entity, true);
    }

    public static void populate(BaseEntity entity, boolean flatten) throws Throwable {
        boolean autocommit = JPAEntityManagerUtils.getAutocommit();
        JPAEntityManagerUtils.setAutocommit(false);
        JPAEntityManagerUtils.begin();
        if (flatten) {
            flattenForPopulation(entity);
        }
        try {
            Class clazz = entity.getClass();
            populate(entity, clazz);
            try {
                JPAEntityManagerUtils.commit();
            } catch (Throwable ignored) {

            }
        } catch (Throwable t) {
            JPAEntityManagerUtils.rollback();
            throw t;
        } finally {
            JPAEntityManagerUtils.setAutocommit(autocommit);
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
            JPAEntityManagerUtils.persist(entity);
            persistedObjects.add(entity);
        } else {
            log.debug("Entity with name '" + reflectionUtils.invokeGetter(entity, "name") + "' is already processed");
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
                JPAEntityManagerUtils.persist(entity);
                persistedObjects.add(entity);
            } else {
                log.debug("Entity with name '" + reflectionUtils.invokeGetter(entity, "name") + "' is already processed");
            }
        } catch (Throwable t) {
            if (!EntityExistsException.class.isAssignableFrom(t.getClass())) {
                t.printStackTrace();
            }
        }
        return entity;
    }

    private static Object innerPopulationCreateIfNotExists(Object entity) {
        ReflectionUtils.doWithFields(entity.getClass(), field -> {
            field.setAccessible(true);
            Object object_ = field.get(entity);
            processCreateIfNotExists(field.getType(), object_);
        }, Utils::filterMethod);
        try {
            Object id = reflectionUtils.invokeGetter(entity, "id");
            if (JPAEntityManagerUtils.find(entity.getClass(), id) == null) {
                if (!persistedObjects.contains(entity)) {
                    JPAEntityManagerUtils.persist(entity);
                    persistedObjects.add(entity);
                } else {
                    log.debug("Entity with name '" + reflectionUtils.invokeGetter(entity, "name") + "' is already processed");
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return entity;
    }

    private static void processFieldWhenCreationIfNotExists(Object entity, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        Object object_ = field.get(entity);
        if (object_ != null) {
            processCreateIfNotExists(field.getType(), object_);
        }
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

    @SuppressWarnings("unchecked")
    private static void processCreateIfNotExists(Class<?> type, Object object_) {
        if (ReflectionUtils.isCollectionImplementation(type)) {
            for (Object innerCollection : (Collection) object_) {
                Object result = innerPopulationCreateIfNotExists(innerCollection);
                if (result != null) {
                    if (!persistedObjects.contains(result)) {
                        Object entity = JPAEntityManagerUtils.merge(result);
                        persistedObjects.add(entity);
                    } else {
                        log.debug("Entity with name '" + reflectionUtils.invokeGetter(result, "name") + "' is already processed");
                    }
                }
            }
        } else if (ReflectionUtils.isMapImplementation(type)) {
            Map<Object, Object> map = (Map<Object, Object>) object_;
            Set<Map.Entry<Object, Object>> set = map.entrySet();
            for (Map.Entry innerMapValues : set) {
                Object result = innerPopulationCreateIfNotExists(innerMapValues.getValue());
                if (result != null) {
                    if (!persistedObjects.contains(result)) {
                        Object entity_ = JPAEntityManagerUtils.merge(result);
                        persistedObjects.add(entity_);
                    } else {
                        log.debug("Entity with name '" + reflectionUtils.invokeGetter(result, "name") + "' is already processed");
                    }
                }
            }
        }
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

    private static void processFieldFlatten(Object entity, Field field, Object object_) throws IllegalAccessException {
        if (object_ != null) {
            Object result = flattenForPopulation(field.getType(), object_);
            if (result != null) {
                field.set(entity, result);
                if (!ReflectionUtils.isCollectionImplementation(result.getClass()) && !ReflectionUtils.isMapImplementation(result.getClass())) {
                    Class<?> type = result.getClass();
                    if (reflectionUtils.getFullyQualifiedJavaTypeOrNull(type) == null && !type.isEnum() && !Enum.class.isAssignableFrom(type)) {
                        if (!persistedObjects.contains(result)) {
                            Object entity_ = JPAEntityManagerUtils.merge(result);
                            persistedObjects.add(entity_);
                        } else {
                            log.debug("Entity with name '" + reflectionUtils.invokeGetter(result, "name") + "' is already processed");
                        }
                    }
                } else {
                    ((Collection) result).forEach(value -> {
                        Class<?> type = value.getClass();
                        if (reflectionUtils.getFullyQualifiedJavaTypeOrNull(type) == null && !type.isEnum() && !Enum.class.isAssignableFrom(type)) {
                            if (!persistedObjects.contains(value)) {
                                Object entity_ = JPAEntityManagerUtils.merge(value);
                                persistedObjects.add(entity_);
                            } else {
                                log.debug("Entity with name '" + reflectionUtils.invokeGetter(value, "name") + "' is already processed");
                            }
                        }
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
                    log.debug("Entity with name '" + reflectionUtils.invokeGetter(object_, "name") + "' is already processed");
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
                Object entity_ = JPAEntityManagerUtils.findByQuery(Item.class, Item.GET_ITEM_ID_BY_NAME, params);
                if (entity_ == null) {
                    entity_ = JPAEntityManagerUtils.find(entity);
                    if (entity_ != null) {
                        return entity_;
                    }
                } else {
                    ((Item) entity_).override((Item) entity, false, null);
                    return entity_;
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return entity;
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
                    Object entity = JPAEntityManagerUtils.merge(value);
                    persistedObjects.add(entity);
                } else {
                    log.debug("Entity with name '" + reflectionUtils.invokeGetter(value, "name") + "' is already processed");
                }
                valuesToAdd.add(value);
            }
            ((Collection) object_).removeAll(valuesToRemove);

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
                    Object entity = JPAEntityManagerUtils.merge(value);
                    persistedObjects.add(entity);
                } else {
                    log.debug("Entity with name '" + reflectionUtils.invokeGetter(value, "name") + "' is already processed");
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

    public static void replace(BaseEntity entity) throws Throwable {
        boolean autocommit = JPAEntityManagerUtils.getAutocommit();
        JPAEntityManagerUtils.setAutocommit(false);
        JPAEntityManagerUtils.begin();
        Class<?> clazz = entity.getClass();
        Object persistedEntity = JPAEntityManagerUtils.find(clazz, entity.getId());
        try {
            if (persistedEntity == null) {
                throw new EntityNotFoundException("Can not replace due object with id '" + entity.getId() + "' does not exists");
            }
            JPAEntityManagerUtils.detach(persistedEntity);
            JPAEntityManagerUtils.detach(entity);
            persistedEntity = entity;
            JPAEntityManagerUtils.update(persistedEntity);
        } catch (Throwable t) {
            JPAEntityManagerUtils.rollback();
            throw t;
        } finally {
            JPAEntityManagerUtils.commit();
            JPAEntityManagerUtils.setAutocommit(autocommit);
        }
    }

    private static void createIfNotExists(BaseEntity entity) {
        ReflectionUtils.doWithFields(entity.getClass(), field -> processFieldWhenCreationIfNotExists(entity, field), Utils::filterMethod);
    }

    public static void update(BaseEntity entity) throws Throwable {
        boolean autocommit = JPAEntityManagerUtils.getAutocommit();
        JPAEntityManagerUtils.setAutocommit(false);
        JPAEntityManagerUtils.begin();
        Class<?> clazz = entity.getClass();
        Object persistedEntity = JPAEntityManagerUtils.find(clazz, entity.getId());
        try {
            if (persistedEntity == null) {
                throw new EntityNotFoundException("Can not replace due object with id '" + entity.getId() + "' does not exists");
            }
            createIfNotExists(entity);
            reflectionUtils.invokeMethod(persistedEntity, "copyNonEmpty", new Object[]{entity});
            JPAEntityManagerUtils.update(persistedEntity);
        } catch (Throwable t) {
            JPAEntityManagerUtils.rollback();
            throw t;
        } finally {
            JPAEntityManagerUtils.commit();
            JPAEntityManagerUtils.setAutocommit(autocommit);
        }
    }

    public static void persist(Object entity) {
        boolean autocommit = JPAEntityManagerUtils.getAutocommit();
        JPAEntityManagerUtils.setAutocommit(false);
        JPAEntityManagerUtils.begin();
        try {
            JPAEntityManagerUtils.persist(entity);
        } catch (Throwable t) {
            JPAEntityManagerUtils.rollback();
            throw t;
        } finally {
            JPAEntityManagerUtils.commit();
            JPAEntityManagerUtils.setAutocommit(autocommit);
        }
    }

    public static void delete(Class<?> clazz, String key) throws Throwable {
        Object entity = JPAEntityManagerUtils.find(clazz, (Object) key);
        JPAEntityManagerUtils.delete(entity);
    }

}
