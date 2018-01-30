package com.araguacaima.gsa.persistence.diagrams.core.specification;

import com.araguacaima.gsa.persistence.meta.BaseEntity;
import com.araguacaima.gsa.persistence.meta.MetaInfo;
import com.araguacaima.gsa.persistence.meta.Version;
import com.araguacaima.specification.AbstractSpecification;

import java.util.*;

public class AppendMetaInfo extends AbstractSpecification {

    public AppendMetaInfo() {
        this(false);
    }

    public AppendMetaInfo(boolean evaluateAllTerms) {
        super(evaluateAllTerms);
    }

    public boolean isSatisfiedBy(Object object, Map map) {
        if (object.getClass().isAssignableFrom(BaseEntity.class)) {
            BaseEntity entity = (BaseEntity) object;
            MetaInfo meta;
            Date thisTime = Calendar.getInstance().getTime();
            if (entity.getMeta() == null) {
                meta = new MetaInfo();
                Version version = new Version();
                meta.setVersion(version);
                meta.setCreated(thisTime);
                meta.setModified(thisTime);
            } else {
                meta = entity.getMeta();
                meta.setModified(thisTime);
                meta.getVersion().nextBuild();
            }
        }
        return true;
    }

    public Collection/*<Object>*/ getTerms() {
        return new ArrayList();
    }
}
