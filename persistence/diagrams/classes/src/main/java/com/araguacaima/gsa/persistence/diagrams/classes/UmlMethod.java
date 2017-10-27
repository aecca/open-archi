package com.araguacaima.gsa.persistence.diagrams.classes;

import com.araguacaima.gsa.persistence.meta.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceContext(unitName = "diagrams")
@Table(name = "UmlMethod", schema = "DIAGRAMS")
public class UmlMethod extends BaseEntity {
    @Column
    private String name;
    @Column
    private String type;
    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "UmlMethod_Parameters",
            joinColumns = {@JoinColumn(name = "Parameter_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Parameter_Id",
                    referencedColumnName = "Id")})
    private Collection<UmlParameter> parameters;
    @Column
    private Visibility visibility = Visibility.PACKAGE;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Collection<UmlParameter> getParameters() {
        return parameters;
    }

    public void setParameters(Collection<UmlParameter> parameters) {
        this.parameters = parameters;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
}
