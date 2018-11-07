package com.araguacaima.open_archi.persistence.diagrams.core.specification;

import com.araguacaima.open_archi.persistence.commons.Constants;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import com.araguacaima.open_archi.persistence.meta.MetaInfo;
import com.araguacaima.open_archi.persistence.meta.Version;
import com.araguacaima.open_archi.persistence.utils.JPAEntityManagerUtils;
import com.araguacaima.specification.AbstractSpecification;

import java.util.*;

@SuppressWarnings("unchecked")
public class IncreaseMetaInfoHistory extends AbstractSpecification {

    private static final Map<String, Object> versionParam = new HashMap<String, Object>() {{
        put("version", new Version());
    }};

    public IncreaseMetaInfoHistory() {
        this(false);
    }

    public IncreaseMetaInfoHistory(boolean evaluateAllTerms) {
        super(evaluateAllTerms);
    }

    public boolean isSatisfiedBy(Object object, Map map) {
        if (BaseEntity.class.isAssignableFrom(object.getClass())) {
            BaseEntity entity = (BaseEntity) object;
            MetaInfo meta = (MetaInfo) map.get("meta");
            Date thisTime = Calendar.getInstance().getTime();
            if (meta == null) {
                BaseEntity existentEntity = (BaseEntity) map.get(Constants.EXISTENT_ENTITY);
                if (existentEntity != null) {
                    meta = existentEntity.getMeta();
                } else {
                    BaseEntity parent = (BaseEntity) map.get("Parent");
                    if (parent != null) {
                        meta = parent.getMeta();
                    }
                    if (meta == null) {
                        Version version = JPAEntityManagerUtils.findByNativeQuery(Version.class, Version.GET_LAST_VERSION);
                        if (version == null) {
                            version = new Version();
                        }
                        meta = new MetaInfo();
                        meta.getActiveHistory().setVersion(version);
                    }
                }
            }
            if (meta != null) {
                meta.addNewHistory(thisTime);
                entity.setMeta(meta);
            }
        }
        return true;
    }

    public Collection<Object> getTerms() {
        return new ArrayList<>();
    }
}
