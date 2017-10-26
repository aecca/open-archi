package com.araguacaima.gsa.persistence;

import com.araguacaima.gsa.persistence.msa.Msa;
import org.junit.Test;

import javax.persistence.TypedQuery;
import java.util.Calendar;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class JPACRUDTest extends AbstractJPATest {

    @Test
    public void testDelete_success() {
        Msa msa = em.find(Msa.class, 1);

        em.getTransaction().begin();
        em.remove(msa);
        em.getTransaction().commit();

        List<Msa> msas = em.createNamedQuery("Msa.getAll", Msa.class).getResultList();

        assertEquals(0, msas.size());
    }

    @Test
    public void testGetAll_success() {
        List<Msa> msas = em.createNamedQuery("Msa.getAll", Msa.class).getResultList();
        assertEquals(1, msas.size());
    }

    @Test
    public void testGetObjectById_success() {
        Msa msa = em.find(Msa.class, 1);
        assertNotNull(msa);
    }

    @Test
    public void testPersist_success() {
        em.getTransaction().begin();
        final Msa msa = new Msa();
        String id = msa.getId();
        msa.setIssueDate(Calendar.getInstance().getTime());
        msa.setExpirationDate(Calendar.getInstance().getTime());
        em.persist(msa);
        em.getTransaction().commit();

        TypedQuery<Msa> consultaAlumnos = em.createNamedQuery("Msa.getById", Msa.class);
        consultaAlumnos.setParameter("id", id);
        List<Msa> msas = consultaAlumnos.getResultList();

        assertNotNull(msas);
        assertEquals(1, msas.size());
        assertEquals(id, msas.get(0).getId());
    }

}