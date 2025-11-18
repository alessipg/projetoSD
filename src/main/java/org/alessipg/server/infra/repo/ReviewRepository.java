package org.alessipg.server.infra.repo;

import jakarta.persistence.EntityManager;
import org.alessipg.server.app.model.Review;
import org.alessipg.server.infra.config.Jpa;

public class ReviewRepository {
    public void save(Review review) {
        EntityManager em = Jpa.getEntityManager();
        try {
            em.getTransaction().begin();
            if (review.getId() == 0) {
                em.persist(review);
            } else {
                review = em.merge(review);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao salvar filme", e);
        } finally {
            em.close();
        }
    }
}
