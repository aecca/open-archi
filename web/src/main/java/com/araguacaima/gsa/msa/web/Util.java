
package com.araguacaima.gsa.msa.web;

import com.araguacaima.commons.utils.ReflectionUtils;
import com.araguacaima.gsa.persistence.meta.BasicEntity;
import com.araguacaima.gsa.persistence.utils.JPAEntityManagerUtils;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
//        classes.add(com.araguacaima.gsa.persistence.diagrams.architectural.Model.class);
//        classes.add(com.araguacaima.gsa.persistence.diagrams.bpm.Model.class);
//        classes.add(com.araguacaima.gsa.persistence.diagrams.er.Model.class);
//        classes.add(com.araguacaima.gsa.persistence.diagrams.flowchart.Model.class);
        classes.add(com.araguacaima.gsa.persistence.diagrams.gantt.Model.class);
//        classes.add(com.araguacaima.gsa.persistence.diagrams.sequence.Model.class);
//        classes.add(com.araguacaima.gsa.persistence.diagrams.classes.Model.class);
        randomBuilder = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
                .seed(123L)
                .objectPoolSize(100)
                .charset(forName("UTF-8"))
                .timeRange(timeLower, timeUpper)
                .dateRange(dateLower, dateUpper)
                .stringLengthRange(5, 20)
                .collectionSizeRange(1, 1)
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
                        process(field.getType(), object_, new LinkedHashMap<>());
                    }, Util::filterMethod);
                    JPAEntityManagerUtils.persist(entity);
                }
            }
        } catch (Throwable ignored) {
            JPAEntityManagerUtils.rollback();
        } finally {
            JPAEntityManagerUtils.commit();
            JPAEntityManagerUtils.closeAll();
        }
    }

    private static void innerPopulation(Object entity, Map<Class, Object> entitiesForReattempt) {
        ReflectionUtils.doWithFields(entity.getClass(), field -> {
            field.setAccessible(true);
            Object object_ = field.get(entity);
            process(field.getType(), object_, entitiesForReattempt);
        }, Util::filterMethod);
        try {
            JPAEntityManagerUtils.persist(entity);
        } catch (Throwable t) {
            entitiesForReattempt.put(entity.getClass(), entity);
        }
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

    private static void process(Class<?> type, Object object_, Map<Class, Object> entitiesForReattempt) {
        if (ReflectionUtils.isCollectionImplementation(type)) {
            for (Object innerCollection : (Collection) object_) {
                innerPopulation(innerCollection, entitiesForReattempt);
            }
        } else if (ReflectionUtils.isMapImplementation(type)) {
            for (Object innerMapValues : ((Map) object_).values()) {
                innerPopulation(innerMapValues, entitiesForReattempt);
            }
        } else {
            if (reflectionUtils.getFullyQualifiedJavaTypeOrNull(type) == null && !type.isEnum() && !Enum.class.isAssignableFrom(type)) {
                if (BasicEntity.class.isAssignableFrom(type)) {
                    try {
                        innerPopulation(object_, entitiesForReattempt);
                    } catch (Throwable t) {
                        entitiesForReattempt.put(object_.getClass(), object_);
                    }
                }
            }
        }
    }
}
