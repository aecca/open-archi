package com.araguacaima.gsa.persistence.diagrams.sequence;

import com.araguacaima.gsa.persistence.diagrams.core.Element;
import com.araguacaima.gsa.persistence.diagrams.core.ElementKind;

import javax.persistence.*;
import java.util.Collection;

@Entity
@PersistenceUnit(unitName = "gsa" )
@Table(name = "Sequence_Model", schema = "DIAGRAMS")
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
public class Model extends Element {

    public static final String FIND_BY_NAME = "SequenceModel.findByName";
    public static final String GET_MODELS_COUNT = "SequenceModel.getModelsCount";
    public static final String GET_ALL_SEQUENCE_MODELS = "SequenceModel.getAllModels";
    public static final String PARAM_NAME = "name";
    
    @OneToMany
    @JoinTable(schema = "DIAGRAMS",
            name = "Sequence_Model_Sequences",
            joinColumns = {@JoinColumn(name = "Sequence_Model_Id",
                    referencedColumnName = "Id")},
            inverseJoinColumns = {@JoinColumn(name = "Sequence_Id",
                    referencedColumnName = "Id")})
    private Collection<Sequence> sequence;
    @Column
    private ElementKind kind = ElementKind.SEQUENCE_MODEL;

    public Collection<Sequence> getSequence() {
        return sequence;
    }

    public void setSequence(Collection<Sequence> sequence) {
        this.sequence = sequence;
    }

    public ElementKind getKind() {
        return kind;
    }

    public void setKind(ElementKind kind) {
        this.kind = kind;
    }
}
