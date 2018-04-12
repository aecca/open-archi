package com.araguacaima.open_archi.persistence.persons;

import org.junit.Test;

import javax.persistence.TypedQuery;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class JPACRUDTest extends AbstractJPATest {

    @Test
    public void testDelete_success() {
        Responsible persons = emGsa.find(Responsible.class, 1);

        emGsa.getTransaction().begin();
        emGsa.remove(persons);
        emGsa.getTransaction().commit();

        List<Responsible> personss = emGsa.createNamedQuery("Persons.getAll", Responsible.class).getResultList();

        assertEquals(0, personss.size());
    }

    @Test
    public void testGetAll_success() {
        List<Responsible> personss = emGsa.createNamedQuery("Persons.getAll", Responsible.class).getResultList();
        assertEquals(1, personss.size());
    }

    @Test
    public void testGetObjectById_success() {
        Responsible persons = emGsa.find(Responsible.class, 1);
        assertNotNull(persons);
    }

    @Test
    public void testPersist_success() {
        emGsa.getTransaction().begin();
        final Responsible persons = new Responsible();
        String id = persons.getId();
        emGsa.persist(persons);
        emGsa.getTransaction().commit();

        TypedQuery<Responsible> consultaAlumnos = emGsa.createNamedQuery("Persons.getById", Responsible.class);
        consultaAlumnos.setParameter("id", id);
        List<Responsible> personss = consultaAlumnos.getResultList();

        assertNotNull(personss);
        assertEquals(1, personss.size());
        assertEquals(id, personss.get(0).getId());
    }

}