package com.araguacaima.open_archi.persistence.utils;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

public class JPAEntityManagerUtils {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("open-archi");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();
    private static boolean autocommit = true;

    private static Logger log = LoggerFactory.getLogger(JPAEntityManagerUtils.class);

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

    public static <T> T findByQuery(Class<T> clazz, String query) {
        return findByQuery(clazz, query, null);
    }

    public static <T> T findByQuery(Class<T> clazz, String query, Map<String, Object> params) {
        TypedQuery<T> namedQuery = entityManager.createNamedQuery(query, clazz);
        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                namedQuery.setParameter(param.getKey(), param.getValue());
            }
        }
        try {
            return namedQuery.getSingleResult();
        } catch (javax.persistence.NoResultException ignored) {
            return null;
        }
    }


    public static void merge(Object entity) {
        merge(entity, getAutocommit());
    }

    public static void merge(Object entity, boolean autocommit) {
        entityManager.merge(entity);
    }

    public static void persist(Object entity) {
        persist(entity, getAutocommit());
    }

    public static void persist(Object entity, boolean autocommit) {
        entityManager.persist(entity);
    }

    public static void delete(Object entity) {
        begin();
        try {
            entityManager.remove(entity);
            flush();
            commit();
        } catch (Throwable t) {
            log.error(t.getMessage());
            rollback();
        }
    }

    public static void delete(Class<?> clazz, String key) {
        begin();
        try {
            Session session = entityManager.unwrap(Session.class);
            Object entity = find(clazz, key);
            Query query = session.createQuery("delete " + clazz.getName() + " where id = :id");
            query.setParameter("id", key);
            query.executeUpdate();
            session.detach(entity);
            session.flush();
            session.evict(entity);
            flush();
            commit();
        } catch (Throwable t) {
            log.error(t.getMessage());
            rollback();
        }
    }

    public static void detach(Object entity) {
        detach(entity, true);
    }

    public static void detach(Object entity, boolean autocommit) {
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

    public static void flush() {
        entityManager.flush();
    }
}