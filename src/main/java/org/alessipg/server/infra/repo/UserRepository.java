package org.alessipg.server.infra.repo;

import org.alessipg.server.infra.config.Jpa;
import org.alessipg.shared.domain.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.NoResultException;

import java.util.Optional;

public class UserRepository {

    public User save(User usuario) {
    EntityManager em = Jpa.getEntityManager();
        try {
            em.getTransaction().begin();
            
            if (usuario.getId() == 0) {
                // Novo usuário
                em.persist(usuario);
            } else {
                // Atualizar usuário existente
                usuario = em.merge(usuario);
            }
            
            em.getTransaction().commit();
            return usuario;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao salvar usuário", e);
        } finally {
            em.close();
        }
    }

    public Optional<User> findByNome(String nome) {
    EntityManager em = Jpa.getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.name = :name", User.class);
            query.setParameter("name", nome);
            
            User usuario = query.getSingleResult();
            return Optional.of(usuario);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar usuário por nome", e);
        } finally {
            em.close();
        }
    }
}
