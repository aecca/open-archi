package com.araguacaima.gsa.model;

import com.araguacaima.gsa.model.msa.Msa;
import org.junit.Test;

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
        //TODO AMM: Complete
        em.persist(msa);
        em.getTransaction().commit();

        List<Msa> msas = em.createNamedQuery("Msa.getAll", Msa.class).getResultList();

        assertNotNull(msas);
        assertEquals(2, msas.size());
    }

}