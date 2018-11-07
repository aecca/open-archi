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
    private static final String GET_LAST_VERSION = "SELECT " +
            "  v7.major, " +
            "  v7.minor, " +
            "  max(v7.build) build " +
            "FROM (SELECT * " +
            "      FROM (SELECT * " +
            "            FROM meta.version v1 " +
            "            WHERE v1.major IN (SELECT max(v2.major) " +
            "                               FROM meta.version v2)) v3 " +
            "      WHERE v3.minor IN (SELECT max(v6.minor) " +
            "                         FROM (SELECT * " +
            "                               FROM meta.version v4 " +
            "                               WHERE v4.major IN (SELECT max(v5.major) " +
            "                                                  FROM meta.version v5)) v6)) v7 " +
            "GROUP BY v7.major, v7.minor ";

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
                        Version version = JPAEntityManagerUtils.findByNativeQuery(Version.class, GET_LAST_VERSION);
                        if (version == null) {
                            version = new Version();
                        } else {
                            version = version.nextBuild();
                        }
                        meta = new MetaInfo();
                        meta.getActiveHistory().setVersion(version);
                        return true;
                    }
                }
            }
            if (meta != null) {
                Date thisTime = Calendar.getInstance().getTime();
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
