package com.araguacaima.open_archi.persistence.diagrams.core.specification;

import com.araguacaima.open_archi.persistence.commons.Constants;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import com.araguacaima.orpheusdb.utils.JPAEntityManagerUtils;
import com.araguacaima.specification.AbstractSpecification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CheckEntityAlreadyExists extends AbstractSpecification {

    public CheckEntityAlreadyExists() {
        this(false);
    }

    public CheckEntityAlreadyExists(boolean evaluateAllTerms) {
        super(evaluateAllTerms);
    }

    @Override
    public boolean isSatisfiedBy(Object object, Map map) {
        Class<?> clazz = object.getClass();
        if (BaseEntity.class.isAssignableFrom(clazz)) {
            BaseEntity entity = (BaseEntity) object;
            Object key = entity.getId();
            if (JPAEntityManagerUtils.find(clazz, key) != null) {
                map.put(Constants.SPECIFICATION_MESSAGE, "Entity with key of '" + key + "' already exists");
                return true;
            } else {
                map.put(Constants.SPECIFICATION_MESSAGE, "Entity with key of '" + key + "' does not exists");
            }
        }
        return false;
    }

    @Override
    public Collection<Object> getTerms() {
        return new ArrayList<>();
    }

}
