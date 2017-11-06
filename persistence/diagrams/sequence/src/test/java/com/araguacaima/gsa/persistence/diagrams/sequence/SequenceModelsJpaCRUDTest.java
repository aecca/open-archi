package com.araguacaima.gsa.persistence.diagrams.sequence;

import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class SequenceModelsJpaCRUDTest extends AbstractJPATest {

    @Test
    public void testDelete_success() {
        Model sequenceModels = emGsa.find(Model.class, 1);

        emGsa.getTransaction().begin();
        emGsa.remove(sequenceModels);
        emGsa.getTransaction().commit();

        List<Model> sequenceModelsList = emGsa.createNamedQuery(Model.GET_ALL_SEQUENCE_MODELS, Model.class).getResultList();

        assertEquals(0, sequenceModelsList.size());
    }

    @Test
    public void testGetAll_success() {
        List<Model> sequenceModels = emGsa.createNamedQuery(Model.GET_ALL_SEQUENCE_MODELS, Model.class).getResultList();
        assertEquals(1, sequenceModels.size());
    }

    @Test
    public void testGetObjectById_success() {
        Model sequenceModels = emGsa.find(Model.class, 1);
        assertNotNull(sequenceModels);
    }

    @Test
    public void testPersist_success() {
        emGsa.getTransaction().begin();
        final Model sequenceModel = new Model();
        String id = sequenceModel.getId();
        emGsa.persist(sequenceModel);
        emGsa.getTransaction().commit();
        Model persistedSequenceModel = emGsa.find(Model.class, id);
        assertNotNull(persistedSequenceModel);
        assertEquals(id, persistedSequenceModel.getId());
    }

    @Test
    public void testPersist_success2() {
        emGsa.getTransaction().begin();
        final SmallProject smallProject = new SmallProject();
        String id = smallProject.getId();
        emGsa.persist(smallProject);
        emGsa.getTransaction().commit();
        SmallProject persistedSequenceModel = emGsa.find(SmallProject.class, id);
        assertNotNull(persistedSequenceModel);
        assertEquals(id, persistedSequenceModel.getId());
    }


    @Test
    public void testPersist_success3() {
        emGsa.getTransaction().begin();
        final LargeProject largeProject = new LargeProject();
        String id = largeProject.getId();
        emGsa.persist(largeProject);
        emGsa.getTransaction().commit();
        LargeProject persistedSequenceModel = emGsa.find(LargeProject.class, id);
        assertNotNull(persistedSequenceModel);
        assertEquals(id, persistedSequenceModel.getId());
    }
}