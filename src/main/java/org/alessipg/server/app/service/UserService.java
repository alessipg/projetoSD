package org.alessipg.server.app.service;

import org.alessipg.server.infra.repo.DataAccessException;
import org.alessipg.server.app.model.User;
import org.alessipg.server.infra.repo.ReviewRepository;
import org.alessipg.server.infra.tcp.ConnectionManager;
import org.alessipg.shared.dto.response.UserGetAllResponse;
import org.alessipg.shared.dto.util.UserView;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.dto.response.StatusResponse;
import org.alessipg.shared.dto.response.UserSelfGetResponse;
import org.alessipg.server.infra.repo.UserRepository;
import org.alessipg.server.ui.ServerView;
import org.alessipg.server.util.JwtUtil;
import org.alessipg.shared.dto.util.UserRecord;

import java.util.List;
import java.util.Optional;
// Java
import java.util.regex.Pattern;

public class UserService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final MovieService movieService;
    private static final Pattern ALNUM_3_20 = Pattern.compile("^[a-zA-Z0-9]{3,20}$");

    public UserService(UserRepository usuarioRepository, ReviewRepository reviewRepository, MovieService movieService) {
        this.userRepository = usuarioRepository;
        this.reviewRepository = reviewRepository;
        this.movieService = movieService;
    }

    public StatusResponse create(UserRecord user) {
        String name = user.nome();
        String password = user.senha();
        if (isInvalidUserInfo(name, password))
            return new StatusResponse(StatusTable.INVALID_INPUT);
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

    private boolean isInvalidUserInfo(String name, String password) {
        if (name == null || password == null) return true;
        return !ALNUM_3_20.matcher(name).matches()
                || !ALNUM_3_20.matcher(password).matches();
    }
    public Optional<User> findByName(String name) throws DataAccessException {
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
            if(name == null)
                return new StatusResponse(StatusTable.UNPROCESSABLE_ENTITY);
            if (isInvalidUserInfo(name, password))
                return new StatusResponse(StatusTable.INVALID_INPUT);
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
                return new StatusResponse(StatusTable.UNAUTHORIZED);
            Optional<User> optUser = findByName(name);
            if (optUser.isEmpty())
                return new StatusResponse(StatusTable.NOT_FOUND);
            User user = optUser.get();
            if(user.getName().equals("admin"))
                return new StatusResponse(StatusTable.FORBIDDEN);
            deleteUserReviews(user.getId());
            userRepository.delete(user);

            // Find and remove connection by username
            String clientAddress = ConnectionManager.getInstance().findClientAddressByUsername(user.getName());
            if (clientAddress != null) {
                ConnectionManager.getInstance().unregisterConnection(clientAddress);
                ServerView.removeConnection(clientAddress);
            }

            return new StatusResponse(StatusTable.OK);
        } catch (Exception e) {
            System.out.println("Service - Erro ao apagar usuário: " + e.getMessage());
            return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
        }
    }

    private void deleteUserReviews(int id) {
        try{
            //listar filmes q tem review do user
            // apagar e atualizar rating dos filmes
            reviewRepository.getByUserId(id).forEach(review -> {
                review.getMovie().updateRating(review.getRating(), true);
                movieService.updateEntity(review.getMovie());
                reviewRepository.delete(review);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public UserGetAllResponse getAll(String token) {
        try {
            String funcao = JwtUtil.validarToken(token)
                    .getClaim("funcao").asString();
            if (!funcao.equals("admin"))
                return new UserGetAllResponse(StatusTable.FORBIDDEN,
                        StatusTable.FORBIDDEN.getMessage(), null);
            Optional<List<User>> users = userRepository.findAll();
            if (users.isEmpty())
                return new UserGetAllResponse(StatusTable.OK,
                        StatusTable.OK.getMessage(), List.of());
            List<UserView> userList = users.get().stream().map(
                    u -> new UserView(u.getId(), u.getName())
            ).toList(
            );
            return new UserGetAllResponse(StatusTable.OK,
                    StatusTable.OK.getMessage(), userList);
        } catch (Exception e) {
            System.out.println("Service - Erro ao obter todos os usuários: " + e.getMessage());
            return new UserGetAllResponse(StatusTable.INTERNAL_SERVER_ERROR,
                    StatusTable.INTERNAL_SERVER_ERROR.getMessage(), null);
        }
    }

    public StatusResponse adminUpdate(String token, int userId, String password) {
        try{
            String funcao = JwtUtil.validarToken(token)
                    .getClaim("funcao").asString();
            if (!funcao.equals("admin"))
                return new StatusResponse(StatusTable.FORBIDDEN);
            if (password == null || !ALNUM_3_20.matcher(password).matches())
                return new StatusResponse(StatusTable.INVALID_INPUT);
            Optional<User> optUser= userRepository.findById(userId);
            if(optUser.isEmpty())
                return new StatusResponse(StatusTable.NOT_FOUND);
            User user = optUser.get();
            user.setPassword(password);
            persist(user);
            return new StatusResponse(StatusTable.OK);
        }catch(Exception e){
            return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
        }
    }

    public StatusResponse adminDelete(String token, int userId) {
        try{
            String funcao = JwtUtil.validarToken(token)
                    .getClaim("funcao").asString();
            if (!funcao.equals("admin"))
                return new StatusResponse(StatusTable.FORBIDDEN);
            Optional<User> optUser= userRepository.findById(userId);
            if(optUser.isEmpty())
                return new StatusResponse(StatusTable.NOT_FOUND);
            User user = optUser.get();
            if(user.getName().equals("admin"))
                return new StatusResponse(StatusTable.FORBIDDEN);
            deleteUserReviews(user.getId());
            userRepository.delete(user);

            // Find and remove connection by username
            String clientAddress = ConnectionManager.getInstance().findClientAddressByUsername(user.getName());
            if (clientAddress != null) {
                ConnectionManager.getInstance().unregisterConnection(clientAddress);
                ServerView.removeConnection(clientAddress);
            }

            return new StatusResponse(StatusTable.OK);
        }catch(Exception e){
            return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
        }
    }

    public User getById(int id) {
        return userRepository.findById(id).orElse(null);
    }
}