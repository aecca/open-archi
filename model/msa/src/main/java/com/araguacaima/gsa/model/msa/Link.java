package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.meta.BaseEntity;

public class Link extends BaseEntity {

    private String name;

    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
