package org.alessipg.server.infra.repo;

import jakarta.persistence.EntityManager;
import org.alessipg.server.app.model.Review;
import org.alessipg.server.infra.config.Jpa;

import java.util.List;
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
            List<Review> results = em.createQuery(
                    "SELECT r FROM Review r JOIN FETCH r.movie JOIN FETCH r.user WHERE r.id = :id AND r.user.id = :userId",
                    Review.class)
                    .setParameter("id", i)
                    .setParameter("userId", id)
                    .getResultList();
            return results.stream().findFirst();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Review> findById(int id) {
        try (EntityManager em = Jpa.getEntityManager()) {
            List<Review> results = em.createQuery(
                    "SELECT r FROM Review r JOIN FETCH r.movie JOIN FETCH r.user WHERE r.id = :id",
                    Review.class)
                    .setParameter("id", id)
                    .getResultList();
            return results.stream().findFirst();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void delete(Review existingReview) {
        EntityManager em = Jpa.getEntityManager();
        try {
            em.getTransaction().begin();
            Review review = em.merge(existingReview);
            em.remove(review);
            em.getTransaction().commit();
            System.out.println("Review deletada com sucesso: " + existingReview.getId());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Erro ao deletar review: " + e.getMessage());
            throw new RuntimeException("Erro ao deletar review", e);
        } finally {
            em.close();
        }
    }

    public List<Review> getByUserId(int id) {
        try (EntityManager em = Jpa.getEntityManager()) {
            return em.createQuery("SELECT r FROM Review r JOIN FETCH r.movie WHERE r.user.id = :userId", Review.class)
                    .setParameter("userId", id)
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar reviews de usuário", e);
        }
    }

    public boolean existsByUserIdAndMovieId(int userId, int movieId) {
        try (EntityManager em = Jpa.getEntityManager()) {
            Long count = em.createQuery("SELECT COUNT(r) FROM Review r WHERE r.user.id = :userId AND r.movie.id = :movieId", Long.class)
                    .setParameter("userId", userId)
                    .setParameter("movieId", movieId)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar existência de review", e);
        }
    }

    public void deleteByUserId(int id) {
        EntityManager em = Jpa.getEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Review r WHERE r.user.id = :userId")
                    .setParameter("userId", id)
                    .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao deletar reviews do usuário", e);
        } finally {
            em.close();
        }
    }
}
