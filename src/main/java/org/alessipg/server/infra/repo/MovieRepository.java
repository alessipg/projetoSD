package org.alessipg.server.infra.repo;

import java.util.List;

import org.alessipg.server.infra.config.Jpa;
import org.alessipg.server.app.model.Movie;
import jakarta.persistence.EntityManager;

public class MovieRepository {

    public Movie save(Movie movie) {
        EntityManager em = Jpa.getEntityManager();
        try {
            em.getTransaction().begin();
            if (movie.getId() == 0) {
                em.persist(movie);
            } else {
                movie = em.merge(movie);
            }
            em.getTransaction().commit();
            return movie;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao salvar filme", e);
        } finally {
            em.close();
        }
    }
    // TODO:Returning Enum instead token
    public List<Movie> getAll() {
        EntityManager em = Jpa.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT m FROM Movie m LEFT JOIN FETCH m.genres",
                    Movie.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    public Movie findById(int id) {
        EntityManager em = Jpa.getEntityManager();
        try {
            return em.find(Movie.class, id);
        } finally {
            em.close();
        }
    }

    public void delete(Movie movie) {
        EntityManager em = Jpa.getEntityManager();
        try {
            em.getTransaction().begin();
            Movie toRemove = em.find(Movie.class, movie.getId());
            if (toRemove != null) {
                em.remove(toRemove);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao deletar filme", e);
        } finally {
            em.close();
        }
    }
}
