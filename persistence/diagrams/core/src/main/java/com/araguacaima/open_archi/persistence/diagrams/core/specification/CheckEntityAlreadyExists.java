package com.araguacaima.open_archi.persistence.diagrams.core.specification;

import com.araguacaima.open_archi.persistence.commons.Constants;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import com.araguacaima.open_archi.persistence.utils.JPAEntityManagerUtils;
import com.araguacaima.specification.AbstractSpecification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CheckEntityAlreadyExists extends AbstractSpecification {

    private static final String GENERAL_ERROR = "ModelAlreadyExistsError";

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
                return true;
            } else {
                map.put(Constants.SPECIFICATION_ERROR, "Key '" + key + "' does not exists");
            }

        }
        return false;
    }

    @Override
    public Collection<Object> getTerms() {
        return new ArrayList<>();
    }

}
