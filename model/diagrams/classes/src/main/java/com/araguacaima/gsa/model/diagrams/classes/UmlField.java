package com.araguacaima.gsa.model.diagrams.classes;

public class UmlField extends UmlElement {

    private Visibility visibility = Visibility.PACKAGE;

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
}
