package com.araguacaima.gsa.persistence.diagrams.sequence;

import org.junit.Test;

import javax.persistence.TypedQuery;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class JPACRUDTest extends AbstractJPATest {

    @Test
    public void testDelete_success() {
        Model diagrams = emGsa.find(Model.class, 1);

        emGsa.getTransaction().begin();
        emGsa.remove(diagrams);
        emGsa.getTransaction().commit();

        List<Model> diagramss = emGsa.createNamedQuery("Diagrams.getAll", Model.class).getResultList();

        assertEquals(0, diagramss.size());
    }

    @Test
    public void testGetAll_success() {
        List<Model> diagramss = emGsa.createNamedQuery("Diagrams.getAll", Model.class).getResultList();
        assertEquals(1, diagramss.size());
    }

    @Test
    public void testGetObjectById_success() {
        Model diagrams = emGsa.find(Model.class, 1);
        assertNotNull(diagrams);
    }

    @Test
    public void testPersist_success() {
        emGsa.getTransaction().begin();
        final Model sequence = new Model();
        String id = sequence.getId();
        emGsa.persist(sequence);
        emGsa.getTransaction().commit();

        TypedQuery<Model> consultaAlumnos = emGsa.createNamedQuery("Diagrams.getById", Model.class);
        consultaAlumnos.setParameter("id", id);
        List<Model> diagramss = consultaAlumnos.getResultList();

        assertNotNull(diagramss);
        assertEquals(1, diagramss.size());
        assertEquals(id, diagramss.get(0).getId());
    }

}