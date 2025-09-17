package org.alessipg;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.alessipg.model.Usuario;

public class Main {

    public static void main(String[] args) {
        Usuario u1 = new Usuario("Fulano", "abc1234");
        Usuario u2 = new Usuario("Cicrano", "abc1234");
        Usuario u3 = new Usuario("Beltrano", "abc1234");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("projeto-sd");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.persist(u1);
        em.persist(u2);
        em.persist(u3);
        em.getTransaction().commit();

        em.close();

    }
}