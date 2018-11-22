package com.araguacaima.open_archi.persistence.diagrams.core;

import com.araguacaima.open_archi.persistence.commons.Utils;
import com.araguacaima.orpheusdb.utils.JPAEntityManagerUtils;

import java.util.HashMap;
import java.util.Map;

public class ShapeFactory {
    public static Shape getInstance(ElementKind kind) {
        Shape shape = new Shape();
        shape.setType(ElementKind.DEFAULT);
        shape.setFill(Utils.randomHexColor());
        shape.setStroke("#333333");
        shape.setInput(true);
        shape.setOutput(true);

        if (kind == null) {
            return shape;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("type", kind);
        ElementShape elementShape = JPAEntityManagerUtils.findByQuery(ElementShape.class, ElementShape.GET_ELEMENT_SHAPE_BY_TYPE, params);

        if (elementShape == null) {
            return shape;
        }
        return ElementShapeWrapper.toShape(elementShape);
    }
}
