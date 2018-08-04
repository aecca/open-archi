package com.araguacaima.open_archi.persistence.diagrams.sequence;

import com.araguacaima.open_archi.persistence.diagrams.core.CompositeElement;
import com.araguacaima.open_archi.persistence.diagrams.core.DiagramableElement;
import com.araguacaima.open_archi.persistence.diagrams.core.Element;
import com.araguacaima.open_archi.persistence.diagrams.core.ElementKind;

import javax.persistence.*;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@PersistenceUnit(unitName = "open-archi")
@DiscriminatorValue(value = "SequenceModel")
@NamedQueries(value = {
       /* @NamedQuery(
        name = Model.FIND_BY_NAME,
        query = "select m from Model m JOIN FETCH Item i where m.id = i.id and i.name = :" + Model.PARAM_NAME),*/
        @NamedQuery(name = Model.GET_MODELS_COUNT,
                query = "select count(a) from com.araguacaima.open_archi.persistence.diagrams.sequence.Model a"),
        @NamedQuery(
                name = Model.GET_ALL_SEQUENCE_MODELS,
                query = "select a from com.araguacaima.open_archi.persistence.diagrams.sequence.Model a")})
public class Model extends Element implements DiagramableElement<Model> {

    public static final String FIND_BY_NAME = "SequenceModel.findByName";
    public static final String GET_MODELS_COUNT = "SequenceModel.getModelsCount";
    public static final String GET_ALL_SEQUENCE_MODELS = "SequenceModel.getAllModels";
    public static final String PARAM_NAME = "name";

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinTable(schema = "DIAGRAMS",
            name = "Sequence_Model_Relationships",
            joinColumns = {@JoinColumn(name = "Sequence_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Relationship_Id",
                    referencedColumnName = "Id")})
    private Set<Relationship> relationships = new LinkedHashSet<>();

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(schema = "DIAGRAMS",
            name = "Sequence_Model_Sequences",
            joinColumns = {@JoinColumn(name = "Sequence_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Sequence_Id",
                    referencedColumnName = "Id")})
    private Collection<Sequence> sequences;

    public Model() {
        setKind(ElementKind.SEQUENCE_MODEL);
    }

    public Collection<Sequence> getSequences() {
        return sequences;
    }

    public void setSequences(Collection<Sequence> sequence) {
        this.sequences = sequence;
    }

    public Set<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(Set<Relationship> relationships) {
        this.relationships = relationships;
    }

    @Override
    public void override(Model source, boolean keepMeta, String suffix, CompositeElement clonedFrom) {
        super.override(source, keepMeta, suffix, clonedFrom);
        this.setSequences(source.getSequences());
        this.setRelationships(source.getRelationships());
    }

    @Override
    public void copyNonEmpty(Model source, boolean keepMeta) {
        super.copyNonEmpty(source, keepMeta);
        if (source.getSequences() != null && !source.getSequences().isEmpty()) {
            this.setSequences(source.getSequences());
        }
        if (source.getRelationships() != null && !source.getRelationships().isEmpty()) {
            this.setRelationships(source.getRelationships());
        }
    }
}
