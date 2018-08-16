package com.araguacaima.open_archi.persistence.diagrams.core.specification;

import com.araguacaima.open_archi.persistence.diagrams.core.Item;
import com.araguacaima.open_archi.persistence.diagrams.core.Taggable;
import com.araguacaima.specification.AbstractSpecification;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

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
                Set<Taggable> taggables = (Set<Taggable>) map.get("Taggables");
                item.getRelationships().forEach(relationship -> {
                    String sourceId = relationship.getSourceId();
                    String destinationId = relationship.getDestinationId();
                    if (relationship.getSource() == null) {
                        if (StringUtils.isNotBlank(sourceId)) {
                            if (taggables != null) {
                                taggables.forEach(taggable -> {
                                    if (sourceId.equals(taggable.getId()) || sourceId.equals(taggable.getKey())) {
                                        relationship.setSource(taggable);
                                    }
                                });
                            }
                        }
                    }
                    if (relationship.getDestination() == null) {
                        if (StringUtils.isNotBlank(destinationId)) {
                            if (taggables != null) {
                                taggables.forEach(taggable -> {
                                    if (destinationId.equals(taggable.getId()) || destinationId.equals(taggable.getKey())) {
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

    public Collection<Object> getTerms() {
        return new ArrayList<>();
    }
}
