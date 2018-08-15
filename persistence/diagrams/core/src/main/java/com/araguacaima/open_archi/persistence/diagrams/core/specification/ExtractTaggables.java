package com.araguacaima.open_archi.persistence.diagrams.core.specification;

import com.araguacaima.commons.utils.ReflectionUtils;
import com.araguacaima.open_archi.persistence.diagrams.core.Taggable;
import com.araguacaima.specification.AbstractSpecification;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class ExtractTaggables extends AbstractSpecification {

    private static ReflectionUtils reflectionUtils = new ReflectionUtils(null);

    public ExtractTaggables() {
        this(false);
    }

    public ExtractTaggables(boolean evaluateAllTerms) {
        super(evaluateAllTerms);
    }

    public boolean isSatisfiedBy(Object object, Map map) {
        Set<Taggable> taggables = (Set<Taggable>) map.get("Taggables");
        if (taggables == null) {
            Object initiator = map.get("Initiator");
            taggables = extractTaggables(initiator);
            map.put("Taggables", taggables);
        }
        return true;
    }

    private void traverse(Object entity, Class clazz, Set<Taggable> taggables) {
        ReflectionUtils.doWithFields(clazz, field -> {
            field.setAccessible(true);
            Object object_ = field.get(entity);
            if (object_ != null) {

                if (ReflectionUtils.isCollectionImplementation(object_.getClass())) {
                    ((Collection) object_).forEach(iter -> {
                        traverse(iter, iter.getClass(), taggables);
                        Taggable iter1 = (Taggable) iter;
                        taggables.add(iter1);
                    });
                } else {
                    if (!taggables.contains(object_)) {
                        Class<?> generic = ReflectionUtils.extractGenerics(field);
                        if (Taggable.class.isAssignableFrom(generic)) {
                            Class<?> fieldType = field.getType();
                            if (ReflectionUtils.isCollectionImplementation(fieldType)) {
                                ((Collection) object_).forEach(iter -> {
                                    traverse(iter, generic, taggables);
                                    Taggable iter1 = (Taggable) iter;
                                    taggables.add(iter1);
                                });
                            } else {
                                Taggable taggable = (Taggable) field.get(entity);
                                taggables.add(taggable);
                            }
                        } else {
                            String genericType = reflectionUtils.getFullyQualifiedJavaTypeOrNull(generic);
                            if (genericType == null) {
                                traverse(object_, generic, taggables);
                            }
                        }
                    }
                }
            }
        }, ExtractTaggables::isComplex);

    }

    private static boolean isComplex(Field field) {
        if ((field.getModifiers() & Modifier.STATIC) != 0) {
            return false;
        }
        Class aClass = null;
        try {
            aClass = ReflectionUtils.extractGenerics(field);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return aClass != null && (reflectionUtils.getFullyQualifiedJavaTypeOrNull(aClass) == null && !aClass.isEnum() && !Enum.class.isAssignableFrom(aClass));
    }

    private Set<Taggable> extractTaggables(Object object) {
        Set<Taggable> taggables = new HashSet<>();
        traverse(object, object.getClass(), taggables);
        return taggables;
    }

    public Collection<Object> getTerms() {
        return new ArrayList<>();
    }
}
