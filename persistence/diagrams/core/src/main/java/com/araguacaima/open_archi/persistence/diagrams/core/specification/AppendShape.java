package com.araguacaima.open_archi.persistence.diagrams.core.specification;

import com.araguacaima.open_archi.persistence.commons.Utils;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;
import com.araguacaima.open_archi.persistence.diagrams.core.Shape;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.specification.AbstractSpecification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class AppendShape extends AbstractSpecification {

    public AppendShape() {
        this(false);
    }

    public AppendShape(boolean evaluateAllTerms) {
        super(evaluateAllTerms);
    }

    public boolean isSatisfiedBy(Object object, Map map) {
        if (Item.class.isAssignableFrom(object.getClass())) {
            Item item = (Item) object;
            Shape shape;
            if (item.getShape() == null) {
                shape = new Shape();
                shape.setType(ElementKind.DEFAULT);
                shape.setFill(Utils.randomHexColor());
                shape.setStroke("#333333");
                shape.setInput(true);
                shape.setOutput(true);
                item.setShape(shape);
            }
        }
        return true;
    }

    public Collection<Object> getTerms() {
        return new ArrayList<>();
    }
}
