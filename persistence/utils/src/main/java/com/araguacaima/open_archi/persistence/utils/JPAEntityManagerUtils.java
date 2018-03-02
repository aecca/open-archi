package com.araguacaima.open_archi.persistence.utils;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

public class JPAEntityManagerUtils {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("open-archi");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();
    private static boolean autocommit = true;

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

    public static <T> T find(Class<T> clazz, String query) {
        return find(clazz, query, null);
    }

    public static <T> T find(Class<T> clazz, String query, Map<String, Object> params) {
        TypedQuery<T> namedQuery = entityManager.createNamedQuery(query, clazz);
        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                namedQuery.setParameter(param.getKey(), param.getValue());
            }
        }
        try {
            return namedQuery.getSingleResult();
        }catch (javax.persistence.NoResultException ignored) {
            return null;
        }
    }

    public static void persist(Object entity) {
        persist(entity, getAutocommit());
    }

    public static void persist(Object entity, boolean autocommit) {
        entityManager.persist(entity);
    }

    public static void delete(Object entity) {
        delete(entity, getAutocommit());
    }

    public static void delete(Object entity, boolean autocommit) {
        entityManager.remove(entity);
        entityManager.detach(entity);
    }

    public static void update(Object entity) {
        update(entity, getAutocommit());
    }

    public static void update(Object entity, boolean autocommit) {
        entityManager.merge(entity);
    }

    public static void begin() {
        entityManager.getTransaction().begin();
    }

    public static void commit() {
        entityManager.getTransaction().commit();
    }

    public static void rollback() {
        entityManager.getTransaction().rollback();
    }

    public static boolean getAutocommit() {
        return autocommit;
    }

    public static boolean isAutocommit() {
        return autocommit;
    }

    public static void setAutocommit(boolean autocommit) {
        JPAEntityManagerUtils.autocommit = autocommit;
    }
}