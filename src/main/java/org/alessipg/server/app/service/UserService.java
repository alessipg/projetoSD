package org.alessipg.server.app.service;

import org.alessipg.server.infra.repo.DataAccessException;
import org.alessipg.shared.domain.model.User;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.records.response.StatusResponse;
import org.alessipg.shared.records.response.UserSelfGetResponse;
import org.alessipg.server.infra.repo.UserRepository;
import org.alessipg.server.ui.ServerView;
import org.alessipg.server.util.JwtUtil;

import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository usuarioRepository) {
        this.userRepository = usuarioRepository;

    }

    public StatusResponse create(String name, String password) {
        if (name == null || name.isBlank() || password == null || password.isBlank())
            return new StatusResponse(StatusTable.BAD);
        try {
            if (findByName(name).isPresent())
                return new StatusResponse(StatusTable.ALREADY_EXISTS);
            persist(new User(name, password));
            return new StatusResponse(StatusTable.CREATED);
        } catch (Exception e) {
            System.out.println("Service - Erro ao criar usuário: " + e.getMessage());
            return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
        }
    }

    public Optional<User> findByName(String name) {
        return userRepository.findByNome(name);
    }

    private void persist(User user) throws DataAccessException {
        userRepository.save(user);
    }

    public UserSelfGetResponse selfGet(String token) {
        try {
            String user = JwtUtil.validarToken(token)
                    .getClaim("usuario").asString();
            if (user != null) {
                return new UserSelfGetResponse(StatusTable.OK, user);
            }
            return new UserSelfGetResponse(StatusTable.UNPROCESSABLE_ENTITY, null);
        } catch (Exception e) {
            System.out.println("Service - Erro ao obter usuário: " + e.getMessage());
            return new UserSelfGetResponse(StatusTable.INTERNAL_SERVER_ERROR, null);
        }
    }

    public StatusResponse update(String token, String password) {
        try {
            String name = JwtUtil.validarToken(token)
                    .getClaim("usuario").asString();
            if (name == null)
                return new StatusResponse(StatusTable.UNPROCESSABLE_ENTITY);
            Optional<User> user = findByName(name);
            if (user.isEmpty())
                return new StatusResponse(StatusTable.NOT_FOUND);
            user.get().setPassword(password);
            persist(user.get());
            return new StatusResponse(StatusTable.OK);
        } catch (Exception e) {
            System.out.println("Service - Erro ao atualizar usuário: " + e.getMessage());
            return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
        }
    }

    public StatusResponse selfDelete(String token) {
        try {
            String name = JwtUtil.validarToken(token)
                    .getClaim("usuario").asString();
            if (name == null)
                return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
            Optional<User> optUser = findByName(name);
            if (optUser.isEmpty())
                return new StatusResponse(StatusTable.NOT_FOUND);
            User user = optUser.get();
            userRepository.delete(user);
            ServerView.removeUser(user.getName());
            return new StatusResponse(StatusTable.OK);
        } catch (Exception e) {
            System.out.println("Service - Erro ao apagar usuário: " + e.getMessage());
            return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
        }
    }
}

