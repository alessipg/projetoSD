package org.alessipg.server.infra.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Central JPA provider to manage a single EntityManagerFactory per application.
 */
public final class Jpa {
    private static final String PU_NAME = "projeto-sd";
    private static volatile EntityManagerFactory emf;

    private Jpa() { }

    /**
     * Lazily initialize and return the singleton EntityManagerFactory.
     */
    public static EntityManagerFactory getEmf() {
        EntityManagerFactory ref = emf;
        if (ref == null) {
            synchronized (Jpa.class) {
                ref = emf;
                if (ref == null) {
                    emf = ref = Persistence.createEntityManagerFactory(PU_NAME);
                }
            }
        }
        return ref;
    }

    /**
     * Create a new EntityManager from the singleton factory.
     */
    public static EntityManager getEntityManager() {
        return getEmf().createEntityManager();
    }

    /**
     * Close the singleton EntityManagerFactory if open. Safe to call multiple times.
     */
    public static synchronized void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
