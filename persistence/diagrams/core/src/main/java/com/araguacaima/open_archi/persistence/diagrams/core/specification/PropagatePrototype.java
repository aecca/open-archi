package com.araguacaima.open_archi.persistence.diagrams.core.specification;

import com.araguacaima.open_archi.persistence.diagrams.core.Item;
import com.araguacaima.specification.AbstractSpecification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class PropagatePrototype extends AbstractSpecification {

    public PropagatePrototype() {
        this(false);
    }

    public PropagatePrototype(boolean evaluateAllTerms) {
        super(evaluateAllTerms);
    }

    public boolean isSatisfiedBy(Object object, Map map) {
        if (Item.class.isAssignableFrom(object.getClass())) {
            Item initiator = (Item) map.get("Initiator");
            Item item = (Item) object;
            boolean prototype = item.isPrototype();
            if (initiator == null) {
                Boolean incomingPrototype = (Boolean) map.get("IsPrototype");
                if (incomingPrototype != null) {
                    prototype = incomingPrototype;
                }
            } else {
                prototype = initiator.isPrototype();
            }
            item.setPrototype(prototype);
        }
        return true;
    }

    public Collection<Object> getTerms() {
        return new ArrayList<>();
    }
}
