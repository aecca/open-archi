package com.araguacaima.open_archi.persistence.diagrams.core.specification;

import com.araguacaima.open_archi.persistence.commons.Constants;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.Item;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import com.araguacaima.open_archi.persistence.utils.JPAEntityManagerUtils;
import com.araguacaima.specification.AbstractSpecification;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

public class CheckInitiatorAlreadyExists extends AbstractSpecification {

    public CheckInitiatorAlreadyExists() {
        this(false);
    }

    public CheckInitiatorAlreadyExists(boolean evaluateAllTerms) {
        super(evaluateAllTerms);
    }

    @Override
    public boolean isSatisfiedBy(Object object, Map map) {
        Class<?> clazz = object.getClass();
        Object initiator = map.get(Constants.INITIATOR);
        if (initiator.equals(object)) {
            if (BaseEntity.class.isAssignableFrom(clazz)) {
                BaseEntity entity = (BaseEntity) object;
                if (Item.class.isAssignableFrom(clazz)) {
                    Item item = (Item) entity;
                    Map<String, Object> params = new HashMap<>();
                    ElementKind kind = item.getKind();
                    String name = item.getName();
                    params.put("kind", kind);
                    params.put("name", name);
                    List<Item> list = JPAEntityManagerUtils.executeQuery(Item.class, Item.GET_ITEMS_BY_NAME_AND_KIND, params);
                    if (CollectionUtils.isNotEmpty(list)) {
                        Item next = list.iterator().next();
                        JPAEntityManagerUtils.detach(next);
                        map.put(Constants.EXISTENT_ENTITY, next);
                        map.put("meta", next.getMeta());
                        return true;
                    } else {
                        map.put(Constants.SPECIFICATION_ERROR, "Name '" + name + "' and Kind '" + kind + "' pair does not exists");
                    }
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public Collection<Object> getTerms() {
        return new ArrayList<>();
    }

}
