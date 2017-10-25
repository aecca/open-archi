package com.araguacaima.gsa.model.meta;

import java.io.Serializable;

public interface IBaseEntity extends Serializable, Cloneable {

    String getId();

    void setId(String id);

    String getTags();

    void addTags(String... tags);

    void removeTag(String tag);

    boolean hasTag(String tag);

     MetaInfo getMetaInfo();

     void setMetaInfo(MetaInfo metaInfo);

}
