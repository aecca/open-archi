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
            taggables = reflectionUtils.extractByType(initiator, Taggable.class);
            map.put("Taggables", taggables);
        }
        return true;
    }

    public Collection<Object> getTerms() {
        return new ArrayList<>();
    }
}
