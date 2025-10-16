package org.alessipg.server.infra.repo;

import org.alessipg.server.infra.config.Jpa;
import org.alessipg.shared.domain.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.NoResultException;

import java.util.Optional;

public class UserRepository {

    public void save(User user) {
        EntityManager em = Jpa.getEntityManager();
        try {
            em.getTransaction().begin();
            if (user.getId() == 0)
                em.persist(user);
            else
                user = em.merge(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new DataAccessException("Erro ao salvar usuário", e);
        } finally {
            em.close();
        }
    }

    public Optional<User> findByNome(String name) {
        EntityManager em = Jpa.getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.name = :name", User.class);
            query.setParameter("name", name);

            User usuario = query.getSingleResult();
            return Optional.of(usuario);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new DataAccessException("Erro ao buscar usuário por nome", e);
        } finally {
            em.close();
        }
    }

    public void delete(User u) {
        EntityManager em = Jpa.getEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.contains(u) ? u : em.getReference(User.class, u.getId());
            em.remove(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new DataAccessException("Erro ao apagar usuário", e);
        } finally {
            em.close();
        }
    }
}
