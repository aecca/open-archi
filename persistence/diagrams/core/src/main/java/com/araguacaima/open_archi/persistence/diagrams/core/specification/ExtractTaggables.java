package com.araguacaima.open_archi.persistence.diagrams.core.specification;

import com.araguacaima.commons.utils.ReflectionUtils;
import com.araguacaima.open_archi.persistence.diagrams.core.Taggable;
import com.araguacaima.specification.AbstractSpecification;

import java.lang.reflect.Field;
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
                if (!taggables.contains(object_)) {
                    Class<?> type = ReflectionUtils.extractGenerics(field);
                    if (Taggable.class.isAssignableFrom(type)) {
                        taggables.add((Taggable) object_);
                    }
                    traverse(object_, type, taggables);
                }
            }
        }, ExtractTaggables::getComplexFields);

    }

    private static boolean getComplexFields(Field field) {
        return reflectionUtils.getFullyQualifiedJavaTypeOrNull(field.getType()) == null;
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
