package com.araguacaima.open_archi.persistence.diagrams.sequence;

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
                query = "select count(a) from Model a"),
        @NamedQuery(
                name = Model.GET_ALL_SEQUENCE_MODELS,
                query = "select a from Model a")})
public class Model extends Element implements DiagramableElement {

    public static final String FIND_BY_NAME = "SequenceModel.findByName";
    public static final String GET_MODELS_COUNT = "SequenceModel.getModelsCount";
    public static final String GET_ALL_SEQUENCE_MODELS = "SequenceModel.getAllModels";
    public static final String PARAM_NAME = "name";
    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Sequence_Model_Relationships",
            joinColumns = {@JoinColumn(name = "Sequence_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Relationship_Id",
                    referencedColumnName = "Id")})
    protected Set<Relationship> relationships = new LinkedHashSet<>();
    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Sequence_Model_Sequences",
            joinColumns = {@JoinColumn(name = "Sequence_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Sequence_Id",
                    referencedColumnName = "Id")})
    private Collection<Sequence> sequences;
    @Column
    @Enumerated(EnumType.STRING)
    private ElementKind kind = ElementKind.SEQUENCE_MODEL;

    public Collection<Sequence> getSequences() {
        return sequences;
    }

    public void setSequences(Collection<Sequence> sequence) {
        this.sequences = sequence;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }

    public Set<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(Set<Relationship> relationships) {
        this.relationships = relationships;
    }
}
