package com.araguacaima.open_archi.persistence.diagrams.classes;

import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "open-archi")
@Table(name = "UmlMethod", schema = "DIAGRAMS")
@DynamicUpdate
public class UmlMethod extends BaseEntity {
    @Column
    private String name;
    @Column
    private String type;
    @ManyToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "UmlMethod_Parameters",
            joinColumns = {@JoinColumn(name = "UmlMethod_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Parameter_Id",
                    referencedColumnName = "Id")})
    private Collection<UmlParameter> parameters;
    @Column
    @Enumerated(EnumType.STRING)
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


    public void override(UmlMethod source) {
        super.override(source);
        this.name = source.getName();
        this.type = source.getType();
    }

    public void copyNonEmpty(UmlMethod source) {
        super.copyNonEmpty(source);
        if (source.getName() != null) {
            this.name = source.getName();
        }
        if (source.getType() != null) {
            this.type = source.getType();
        }
    }

}
