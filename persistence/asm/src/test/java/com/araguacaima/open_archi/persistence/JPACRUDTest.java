package com.araguacaima.open_archi.persistence;

import com.araguacaima.open_archi.persistence.asm.Asm;
import org.junit.Test;

import javax.persistence.TypedQuery;
import java.util.Calendar;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class JPACRUDTest extends AbstractJPATest {

    @Test
    public void testDelete_success() {
        Asm asm = emAsm.find(Asm.class, 1);

        emAsm.getTransaction().begin();
        emAsm.remove(asm);
        emAsm.getTransaction().commit();

        List<Asm> asms = emAsm.createNamedQuery("Asm.getAll", Asm.class).getResultList();

        assertEquals(0, asms.size());
    }

    @Test
    public void testGetAll_success() {
        List<Asm> asms = emAsm.createNamedQuery("Asm.getAll", Asm.class).getResultList();
        assertEquals(1, asms.size());
    }

    @Test
    public void testGetObjectById_success() {
        Asm asm = emAsm.find(Asm.class, 1);
        assertNotNull(asm);
    }

    @Test
    public void testPersist_success() {
        emAsm.getTransaction().begin();
        final Asm asm = new Asm();
        String id = asm.getId();
        asm.setIssueDate(Calendar.getInstance().getTime());
        asm.setExpirationDate(Calendar.getInstance().getTime());
        emAsm.persist(asm);
        emAsm.getTransaction().commit();

        TypedQuery<Asm> consultaAlumnos = emAsm.createNamedQuery("Asm.getById", Asm.class);
        consultaAlumnos.setParameter("id", id);
        List<Asm> asms = consultaAlumnos.getResultList();

        assertNotNull(asms);
        assertEquals(1, asms.size());
        assertEquals(id, asms.get(0).getId());
    }

}