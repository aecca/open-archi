package com.araguacaima.gsa.persistence.meta;

import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

public class AbstractJPATest {

    static EntityManager emGsa;
    static EntityManagerFactory emfGsa;

    @BeforeClass
    public static void init()
            throws FileNotFoundException, SQLException {
        emfGsa = Persistence.createEntityManagerFactory("open-archi");
        emGsa = emfGsa.createEntityManager();
    }

    @AfterClass
    public static void tearDown() {
        close(emGsa, emfGsa);
    }

    private static void close(EntityManager em, EntityManagerFactory emf) {
        if (em == null) {
            try {
                FileUtils.forceDeleteOnExit(new File("D:\\tmp\\gsa\\db"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            em.clear();
            em.close();
            emf.close();
        }
    }

    @Before
    public void initializeDatabase() {
        Session session = emGsa.unwrap(Session.class);
    }
}