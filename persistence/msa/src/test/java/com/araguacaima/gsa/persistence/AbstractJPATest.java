package com.araguacaima.gsa.persistence;

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

    static EntityManager emMeta;
    static EntityManagerFactory emfMeta;
    static EntityManager emPersons;
    static EntityManagerFactory emfPersons;
    static EntityManager emDiagrams;
    static EntityManagerFactory emfDiagrams;
    static EntityManager emMsa;
    static EntityManagerFactory emfMsa;
    static EntityManager emGsa;
    static EntityManagerFactory emfGsa;

    @BeforeClass
    public static void init()
            throws FileNotFoundException, SQLException {
        emfMeta = Persistence.createEntityManagerFactory("meta");
        emMeta = emfMeta.createEntityManager();
        emfPersons = Persistence.createEntityManagerFactory("persons");
        emPersons = emfPersons.createEntityManager();
        emfDiagrams = Persistence.createEntityManagerFactory("diagrams");
        emDiagrams = emfDiagrams.createEntityManager();
        emfMsa = Persistence.createEntityManagerFactory("msa");
        emMsa = emfMsa.createEntityManager();
        emfGsa = Persistence.createEntityManagerFactory("gsa");
        emGsa = emfGsa.createEntityManager();
    }

    @AfterClass
    public static void tearDown() {
        emMsa.clear();
        emMsa.close();
        emfMsa.close();
    }

    @Before
    public void initializeDatabase() {
        Session session = emMsa.unwrap(Session.class);

    }
}