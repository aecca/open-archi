package com.araguacaima.gsa.model.msa;

import com.araguacaima.gsa.model.meta.BaseEntity;

public class Markdown extends BaseEntity {

    private MarkdownFlavour flavour;

    private String value;

    public MarkdownFlavour getFlavour() {
        return flavour;
    }

    public void setFlavour(MarkdownFlavour flavour) {
        this.flavour = flavour;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
