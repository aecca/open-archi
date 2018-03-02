
package com.araguacaima.open_archi.web;

import com.araguacaima.commons.utils.ReflectionUtils;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;
import com.araguacaima.open_archi.persistence.meta.BasicEntity;
import com.araguacaima.open_archi.persistence.utils.JPAEntityManagerUtils;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static java.nio.charset.Charset.forName;


public class Util {

    private static Logger log = LoggerFactory.getLogger(Server.class);
    private static Set<Class> classes = new HashSet<>();
    private static ReflectionUtils reflectionUtils = new ReflectionUtils(null);
    private static EnhancedRandomBuilder randomBuilder;
    private static LocalTime timeLower = LocalTime.of(0, 0);
    private static LocalTime timeUpper = LocalTime.of(0, 0);
    private static LocalDate dateLower = LocalDate.of(2000, 1, 1);
    private static LocalDate dateUpper = LocalDate.of(2040, 12, 31);

    public Util() {
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
    }

    public static void dbPopulation() {
        JPAEntityManagerUtils.begin();
        try {
            EnhancedRandom enhancedRandom = randomBuilder.build();
            for (Class<?> clazz : classes) {
                int next = new Random().nextInt((4 - 1) + 1) + 1;
                for (int i = 0; i < next; i++) {
                    Object entity = enhancedRandom.nextObject(clazz);
                    ReflectionUtils.doWithFields(clazz, field -> {
                        field.setAccessible(true);
                        Object object_ = field.get(entity);
                        Object result = process(field.getType(), object_);
                        field.set(object_, result);
                    }, Util::filterMethod);
                    JPAEntityManagerUtils.persist(entity);
                }
            }
        } catch (Throwable ignored) {
            JPAEntityManagerUtils.rollback();
        } finally {
            JPAEntityManagerUtils.commit();
        }
    }

    public static void populate(Object entity) throws Throwable {
        JPAEntityManagerUtils.begin();
        try {
            Class clazz = entity.getClass();
            ReflectionUtils.doWithFields(clazz, field -> {
                field.setAccessible(true);
                Object object_ = field.get(entity);
                Object result = process(field.getType(), object_);
                field.set(entity, result);
            }, Util::filterMethod);
            JPAEntityManagerUtils.persist(entity);
        } catch (Throwable t) {
            JPAEntityManagerUtils.rollback();
            throw t;
        } finally {
            JPAEntityManagerUtils.commit();
        }
    }

    private static Object innerPopulation(Object entity) {
        ReflectionUtils.doWithFields(entity.getClass(), field -> {
            field.setAccessible(true);
            Object object_ = field.get(entity);
            Object result = process(field.getType(), object_);
            field.set(entity, result);
        }, Util::filterMethod);
        try {
            if (Item.class.isAssignableFrom(entity.getClass())) {
                Map<String, Object> params = new HashMap<>();
                String name = (String) reflectionUtils.invokeGetter(entity, "name");
                params.put("name", name);
                ElementKind kind = (ElementKind) reflectionUtils.invokeGetter(entity, "kind");
                params.put("kind", kind);
                Item item = JPAEntityManagerUtils.find(Item.class, Item.GET_ITEM_ID_BY_NAME, params);
                if (item == null) {
                    JPAEntityManagerUtils.persist(entity);
                } else {
                    item.copy((Item) entity);
                    JPAEntityManagerUtils.update(item);
                    return item;
                }
            } else {
                JPAEntityManagerUtils.persist(entity);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return entity;
    }

    private static boolean filterMethod(Field field) {
        Class aClass = null;
        try {
            aClass = ReflectionUtils.extractGenerics(field);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return aClass == null || reflectionUtils.getFullyQualifiedJavaTypeOrNull(aClass) == null;
    }

    private static Object process(Class<?> type, Object object_) {
        if (object_ != null) {
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
                    map.put(innerMapValues.getKey(), innerPopulation(innerMapValues.getValue()));
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
        }
        return object_;
    }
}
