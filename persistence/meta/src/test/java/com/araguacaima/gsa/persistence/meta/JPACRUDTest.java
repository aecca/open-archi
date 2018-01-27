package com.araguacaima.gsa.persistence.meta;

import org.junit.Test;

import javax.persistence.TypedQuery;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class JPACRUDTest extends AbstractJPATest {

    @Test
    public void testDelete_success() {
        Account account = emGsa.find(Account.class, 1);

        emGsa.getTransaction().begin();
        emGsa.remove(account);
        emGsa.getTransaction().commit();

        List<Account> accounts = emGsa.createNamedQuery("Account.getAll", Account.class).getResultList();

        assertEquals(0, accounts.size());
    }

    @Test
    public void testGetAll_success() {
        List<Account> accounts = emGsa.createNamedQuery("Account.getAll", Account.class).getResultList();
        assertEquals(1, accounts.size());
    }

    @Test
    public void testGetObjectById_success() {
        Account account = emGsa.find(Account.class, 1);
        assertNotNull(account);
    }

    @Test
    public void testPersist_success() {
        emGsa.getTransaction().begin();
        final Account account = new Account();
        String id = account.getId();
        emGsa.persist(account);
        emGsa.getTransaction().commit();

        TypedQuery<Account> consultaAlumnos = emGsa.createNamedQuery("", Account.class);
        consultaAlumnos.setParameter("id", id);
        List<Account> accounts = consultaAlumnos.getResultList();

        assertNotNull(accounts);
        assertEquals(1, accounts.size());
        assertEquals(id, accounts.get(0).getId());
    }

}