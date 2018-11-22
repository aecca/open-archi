package com.araguacaima.open_archi.persistence.diagrams.core.specification;

import com.araguacaima.open_archi.persistence.commons.Constants;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import com.araguacaima.orpheusdb.utils.JPAEntityManagerUtils;
import com.araguacaima.specification.AbstractSpecification;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CheckModelDoesNotExists extends AbstractSpecification {

    public CheckModelDoesNotExists() {
        this(false);
    }

    public CheckModelDoesNotExists(boolean evaluateAllTerms) {
        super(evaluateAllTerms);
    }

    @Override
    public boolean isSatisfiedBy(Object object, Map map) {
        Class<?> clazz = object.getClass();
        if (BaseEntity.class.isAssignableFrom(clazz)) {
            BaseEntity entity = (BaseEntity) object;
            if (Item.class.isAssignableFrom(clazz)) {
                Item item = (Item) entity;
                Map<String, Object> params = new HashMap<>();
                ElementKind kind = item.getKind();
                String name = item.getName();
                params.put("kind", kind);
                params.put("name", name);
                if (CollectionUtils.isNotEmpty(JPAEntityManagerUtils.executeQuery(Item.class, Item.GET_ITEMS_BY_NAME_AND_KIND, params))) {
                    map.put(Constants.SPECIFICATION_ERROR_ALREADY_EXISTS, "Name '" + name + "' and Kind '" + kind + "' pair already exists");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Collection<Object> getTerms() {
        return new ArrayList<>();
    }

}
