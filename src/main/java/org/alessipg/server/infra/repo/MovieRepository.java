package org.alessipg.server.infra.repo;

import org.alessipg.server.infra.config.Jpa;
import org.alessipg.shared.domain.model.Movie;

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
}
