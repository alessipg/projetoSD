package org.alessipg.server.infra.repo;

import org.alessipg.shared.domain.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.NoResultException;

import java.util.Optional;

public class UsuarioRepository implements AutoCloseable {
    private EntityManagerFactory emf;
    
    public UsuarioRepository() {
        this.emf = Persistence.createEntityManagerFactory("projeto-sd");
    }

    public Usuario save(Usuario usuario) {
        EntityManager em = emf.createEntityManager();
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

    public Optional<Usuario> findByNome(String nome) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.nome = :nome", Usuario.class);
            query.setParameter("nome", nome);
            
            Usuario usuario = query.getSingleResult();
            return Optional.of(usuario);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar usuário por nome", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void close() throws Exception {
        if (emf != null && emf.isOpen()) {
            emf.close();
            System.out.println("EntityManagerFactory fechado com sucesso.");
        }
    }
}
