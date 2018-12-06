package com.araguacaima.open_archi.persistence.diagrams.bpm;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.DiagramableElement;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;
import com.araguacaima.open_archi.persistence.diagrams.core.ModelElement;
import com.araguacaima.open_archi.persistence.meta.BaseEntity;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@PersistenceUnit(unitName = "open-archi")
@DiscriminatorValue(value = "BpmModel")
public class Model extends ModelElement implements DiagramableElement<Model> {

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinTable(schema = "DIAGRAMS",
            name = "Bpm_Model_Relationships",
            joinColumns = {@JoinColumn(name = "Bpm_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Relationship_Id",
                    referencedColumnName = "Id")})
    private Set<Relationship> relationships = new LinkedHashSet<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Model_Pools",
            joinColumns = {@JoinColumn(name = "Bpm_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Pool_Id",
                    referencedColumnName = "Id")})
    private Collection<Pool> pools;

    public Model() {
        setKind(ElementKind.BPM_MODEL);
    }

    public Collection<Pool> getPools() {
        return pools;
    }

    public void setPools(Collection<Pool> pools) {
        this.pools = pools;
    }

    @Override
    public Collection<BaseEntity> override(Model source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.override(source, keepMeta, suffix, clonedFrom));
        for (Pool consumer : source.getPools()) {
            Pool newPool = new Pool();
            overriden.addAll(newPool.override(consumer, keepMeta, suffix, clonedFrom));
            if (!this.pools.add(newPool)) {
                this.pools.remove(newPool);
                this.pools.add(newPool);
            }
            overriden.add(newPool);
        }
        return overriden;
    }

    @Override
    public Collection<BaseEntity> copyNonEmpty(Model source, boolean keepMeta) {
        Collection<BaseEntity> overriden = new ArrayList<>();
        overriden.addAll(super.copyNonEmpty(source, keepMeta));
        if (source.getPools() != null && !source.getPools().isEmpty()) {
            for (Pool consumer : source.getPools()) {
                Pool newPool = new Pool();
                overriden.addAll(newPool.copyNonEmpty(consumer, keepMeta));
                if (!this.pools.add(newPool)) {
                    this.pools.remove(newPool);
                    this.pools.add(newPool);
                }
                overriden.add(newPool);
            }
        }
        return overriden;
    }
}
