package com.araguacaima.gsa.persistence.utils;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

public class JPAEntityManagerUtils {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("gsa");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();

    public static void closeAll() {
        close(entityManager, entityManagerFactory);
    }

    public static void close() {
        close(entityManager, null);
    }

    private static void close(EntityManager em, EntityManagerFactory emf) {
        if (em != null) {
            em.clear();
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }

    public static void initializeDatabase() {
        entityManager.unwrap(Session.class);
    }

    public static EntityManager getEntityManager() {
        return entityManager;
    }


    public static <T> T find(Class<T> clazz, Object key) {
        return entityManager.find(clazz, key);
    }


    public static <T> List<T> executeQuery(Class<T> clazz, String query) {
        return executeQuery(clazz, query, null);
    }

    public static <T> List<T> executeQuery(Class<T> clazz, String query, Map<String, Object> params) {
        TypedQuery<T> namedQuery = entityManager.createNamedQuery(query, clazz);
        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                namedQuery.setParameter(param.getKey(), param.getValue());
            }
        }
        return namedQuery.getResultList();
    }

    public static void persist(Object entity) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
        } finally {
            close();
        }
    }

    public static void delete(Object entity) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(entity);
            entityManager.detach(entity);
            entityManager.getTransaction().commit();
        } finally {
            close();
        }
    }

    public static void update(Object entity) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
        } finally {
            close();
        }
    }

}