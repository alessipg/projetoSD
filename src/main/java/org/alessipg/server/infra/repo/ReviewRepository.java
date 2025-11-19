package org.alessipg.server.infra.repo;

import jakarta.persistence.EntityManager;
import org.alessipg.server.app.model.Review;
import org.alessipg.server.infra.config.Jpa;

import java.util.Optional;

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

    public Optional<Review> findByIdAndUserId(int i, int id) {
        try (EntityManager em = Jpa.getEntityManager()) {
            return Optional.ofNullable(em.createQuery("SELECT r FROM Review r WHERE r.id = :id AND r.user.id = :userId", Review.class)
                    .setParameter("id", i)
                    .setParameter("userId", id)
                    .getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void delete(Review existingReview) {
        try (EntityManager em = Jpa.getEntityManager()) {
            em.getTransaction().begin();
            Review review = em.merge(existingReview);
            em.remove(review);
            em.getTransaction().commit();
        }catch (Exception e) {
            throw new RuntimeException("Erro ao deletar review", e);
        }
    }
}
