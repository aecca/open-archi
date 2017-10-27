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
        Msa msa = emMsa.find(Msa.class, 1);

        emMsa.getTransaction().begin();
        emMsa.remove(msa);
        emMsa.getTransaction().commit();

        List<Msa> msas = emMsa.createNamedQuery("Msa.getAll", Msa.class).getResultList();

        assertEquals(0, msas.size());
    }

    @Test
    public void testGetAll_success() {
        List<Msa> msas = emMsa.createNamedQuery("Msa.getAll", Msa.class).getResultList();
        assertEquals(1, msas.size());
    }

    @Test
    public void testGetObjectById_success() {
        Msa msa = emMsa.find(Msa.class, 1);
        assertNotNull(msa);
    }

    @Test
    public void testPersist_success() {
        emMsa.getTransaction().begin();
        final Msa msa = new Msa();
        String id = msa.getId();
        msa.setIssueDate(Calendar.getInstance().getTime());
        msa.setExpirationDate(Calendar.getInstance().getTime());
        emMsa.persist(msa);
        emMsa.getTransaction().commit();

        TypedQuery<Msa> consultaAlumnos = emMsa.createNamedQuery("Msa.getById", Msa.class);
        consultaAlumnos.setParameter("id", id);
        List<Msa> msas = consultaAlumnos.getResultList();

        assertNotNull(msas);
        assertEquals(1, msas.size());
        assertEquals(id, msas.get(0).getId());
    }

}