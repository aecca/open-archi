package com.araguacaima.open_archi.persistence.diagrams.core.specification;

import com.araguacaima.open_archi.persistence.diagrams.core.Item;
import com.araguacaima.open_archi.persistence.diagrams.core.Relationship;
import com.araguacaima.open_archi.persistence.diagrams.core.Taggable;
import com.araguacaima.open_archi.persistence.meta.MetaInfo;
import com.araguacaima.specification.AbstractSpecification;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class FixRelationships extends AbstractSpecification {

    public FixRelationships() {
        this(false);
    }

    public FixRelationships(boolean evaluateAllTerms) {
        super(evaluateAllTerms);
    }

    public boolean isSatisfiedBy(Object object, Map map) {
        if (Item.class.isAssignableFrom(object.getClass())) {
            Item item = (Item) object;
            if (item.getRelationships() != null && !item.getRelationships().isEmpty()) {
                item.getRelationships().forEach(relationship -> {
                    String sourceId = relationship.getSourceId();
                    String destinationId = relationship.getDestinationId();
                    if (relationship.getSource() == null) {
                        if (StringUtils.isNotBlank(sourceId)) {
                            Set<Taggable> taggables = (Set<Taggable>) map.get("Taggables");
                            if (taggables != null) {
                                taggables.forEach(taggable -> {
                                    if (taggable.getId().equals(sourceId)) {
                                        relationship.setSource(taggable);
                                    }
                                });
                            }
                        }
                    }
                    if (relationship.getDestination() == null) {
                        if (StringUtils.isNotBlank(destinationId)) {
                            Set<Taggable> taggables = (Set<Taggable>) map.get("Taggables");
                            if (taggables != null) {
                                taggables.forEach(taggable -> {
                                    if (taggable.getId().equals(destinationId)) {
                                        relationship.setDestination(taggable);
                                    }
                                });
                            }
                        }
                    }
                });
            }
        }
        return true;
    }

    private Set<Taggable> extractTaggables(Object object) {
        return null;
    }

    public Collection<Object> getTerms() {
        return new ArrayList<>();
    }
}
