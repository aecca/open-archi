package com.araguacaima.open_archi.persistence.diagrams.core.specification;

import com.araguacaima.open_archi.persistence.diagrams.core.Item;
import com.araguacaima.open_archi.persistence.diagrams.core.Shape;
import com.araguacaima.specification.AbstractSpecification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class ApplyColorScheme extends AbstractSpecification {

    public ApplyColorScheme() {
        this(false);
    }

    public ApplyColorScheme(boolean evaluateAllTerms) {
        super(evaluateAllTerms);
    }

    public boolean isSatisfiedBy(Object object, Map map) {
        if (Item.class.isAssignableFrom(object.getClass())) {
            Item item = (Item) object;
            if (item.getShape() != null) {
                Shape shape = item.getShape();
                switch (item.getKind()) {
                    case ARCHITECTURE_MODEL:
                        shape.setFill("#01203A");
                        break;
                    case FLOWCHART_MODEL:
                        shape.setFill("#01203A");
                        break;
                    case SEQUENCE_MODEL:
                        shape.setFill("#01203A");
                        break;
                    case GANTT_MODEL:
                        shape.setFill("#01203A");
                        break;
                    case ENTITY_RELATIONSHIP_MODEL:
                        shape.setFill("#01203A");
                        break;
                    case UML_CLASS_MODEL:
                        shape.setFill("#01203A");
                        break;
                    case BPM_MODEL:
                        shape.setFill("#01203A");
                        break;
                    case ARCHITECTURE:
                        shape.setFill("#01203A");
                        break;
                    case FLOWCHART:
                        shape.setFill("#01203A");
                        break;
                    case SEQUENCE:
                        shape.setFill("#01203A");
                        break;
                    case GANTT:
                        shape.setFill("#01203A");
                        break;
                    case ENTITY_RELATIONSHIP:
                        shape.setFill("#01203A");
                        break;
                    case UML_CLASS:
                        shape.setFill("#01203A");
                        break;
                    case FEATURE:
                        shape.setFill("#01203A");
                        break;
                    case COMPONENT:
                        shape.setFill("#1368BD");
                        break;
                    case CONSUMER:
                        shape.setFill("#F0AD4B");
                        break;
                    case CONTAINER:
                        shape.setFill("#08427B");
                        break;
                    case DEPLOYMENT:
                        shape.setFill("#01203A");
                        break;
                    case BPM:
                        shape.setFill("#01203A");
                        break;
                    case SYSTEM:
                        shape.setFill("#01203A");
                        break;
                }
            }
        }
        return true;
    }

    public Collection<Object> getTerms() {
        return new ArrayList<>();
    }
}
