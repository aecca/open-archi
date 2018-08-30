package com.araguacaima.open_archi.persistence.diagrams.core.specification;

import com.araguacaima.open_archi.persistence.meta.Account;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import com.araguacaima.open_archi.persistence.meta.History;
import com.araguacaima.open_archi.persistence.meta.MetaInfo;
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
        if (BaseEntity.class.isAssignableFrom(object.getClass())) {
            BaseEntity entity = (BaseEntity) object;
            MetaInfo meta;
            Date thisTime = Calendar.getInstance().getTime();
            if (entity.getMeta() == null) {
                if (map.get("meta") == null) {
                    meta = new MetaInfo();
                    meta.addNewHistory(thisTime);
                    meta.setCreated(thisTime);
                    map.put("meta", meta);
                } else {
                    meta = (MetaInfo) map.get("meta");
                }
                entity.setMeta(meta);
            } else {
                meta = entity.getMeta();
                meta.addNewHistory(thisTime);
                map.put("meta", meta);
            }
            Account account = (Account) map.get("account");
            if (account != null) {
                Account createdBy = meta.getCreatedBy();
                if (createdBy == null) {
                    meta.setCreatedBy(account);
                } else {
                    History activeHistory = meta.getActiveHistory();
                    Account modifiedBy = activeHistory.getModifiedBy();
                    if (modifiedBy == null) {
                        activeHistory.setModifiedBy(account);
                    } else {
                        meta.addNewHistory(Calendar.getInstance().getTime(), account);
                    }
                }
            }
        }
        return true;
    }

    public Collection<Object> getTerms() {
        return new ArrayList<>();
    }
}
