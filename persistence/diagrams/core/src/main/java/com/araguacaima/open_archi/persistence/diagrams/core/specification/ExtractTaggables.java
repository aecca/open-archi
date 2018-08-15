package com.araguacaima.open_archi.persistence.diagrams.core.specification;

import com.araguacaima.commons.utils.ReflectionUtils;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;
import com.araguacaima.open_archi.persistence.diagrams.core.Taggable;
import com.araguacaima.specification.AbstractSpecification;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class ExtractTaggables extends AbstractSpecification {

    private ReflectionUtils reflectionUtils = new ReflectionUtils(null);
    private static String DIAGRAMS_PACKAGES = "com.araguacaima.open_archi.persistence.diagrams";
    private static Reflections diagramsReflections;

    static {
        diagramsReflections = new Reflections(DIAGRAMS_PACKAGES, Taggable.class.getClassLoader());
    }

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

    private Set<Taggable> extractTaggables(Object object) {
        Set<Taggable> taggables = new HashSet<>();
        ReflectionUtils.doWithFields(object.getClass(), field -> {
            if (Taggable.class.isAssignableFrom(field.getType())) {
                Taggable taggable = (Taggable) field.get(object);
                taggables.add(taggable);
            }
        });
        return taggables.isEmpty() ? null : taggables;
    }

    public Collection<Object> getTerms() {
        return new ArrayList<>();
    }
}
