package com.araguacaima.gsa.model.msa;


import org.eclipse.persistence.sessions.Session;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.FileNotFoundException;
import java.sql.SQLException;

public class AbstractJPATest {

    protected static EntityManager em;
    protected static EntityManagerFactory emf;

    @BeforeClass
    public static void init()
            throws FileNotFoundException, SQLException {
        emf = Persistence.createEntityManagerFactory("msa");
        em = emf.createEntityManager();
    }

    @AfterClass
    public static void tearDown() {
        em.clear();
        em.close();
        emf.close();
    }

    @Before
    public void initializeDatabase() {
        Session session = em.unwrap(Session.class);

    }
}