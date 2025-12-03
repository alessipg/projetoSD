package org.alessipg.server.app.service;

import java.util.Optional;

import org.alessipg.server.infra.tcp.ConnectionManager;
import org.alessipg.server.ui.ServerView;
import org.alessipg.server.util.JwtUtil;
import org.alessipg.server.app.model.User;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.dto.response.StatusResponse;
import org.alessipg.shared.dto.response.UserLoginResponse;

import com.auth0.jwt.interfaces.DecodedJWT;

public class AuthService {

    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public UserLoginResponse login(String user, String password, String clientAddress) {
        try {
            System.out.println("[AuthService] Login attempt - User: " + user + ", ClientAddress: " + clientAddress);

            if (user == null || user.isBlank() || password == null || password.isBlank()) {
                System.err.println("[AuthService] Login failed: empty user or password");
                return new UserLoginResponse(StatusTable.FORBIDDEN, null);
            }

            // Validate clientAddress
            if (clientAddress == null || clientAddress.trim().isEmpty()) {
                System.err.println("[AuthService] ERROR: clientAddress is null or empty during login");
                return new UserLoginResponse(StatusTable.BAD, null);
            }

            Optional<User> usuarioOpt = userService.findByName(user);

            if (usuarioOpt.isEmpty()) {
                System.out.println("[AuthService] Login failed: user not found - " + user);
                return new UserLoginResponse(StatusTable.FORBIDDEN, null);
            }

            User userOpt = usuarioOpt.get();
            if (!userOpt.getPassword().equals(password)) {
                System.out.println("[AuthService] Login failed: invalid password for user - " + user);
                return new UserLoginResponse(StatusTable.FORBIDDEN, null);
            }

            String token = JwtUtil.generateToken(
                    userOpt.getId(),
                    userOpt.getName(),
                    userOpt.isAdmin() ? "admin" : "usuario");

            // Register the username with the client address
            System.out.println("[AuthService] Registering login in ConnectionManager...");
            ConnectionManager.getInstance().registerLogin(clientAddress, userOpt.getName());
            System.out.println("[AuthService] Updating ServerView...");
            ServerView.updateConnection(clientAddress, userOpt.getName());

            System.out.println("[AuthService] Login successful for user: " + userOpt.getName());
            return new UserLoginResponse(StatusTable.OK, token);

        } catch (Exception e) {
            System.err.println("[AuthService] ERROR during login: " + e.getMessage());
            e.printStackTrace();
            return new UserLoginResponse(StatusTable.INTERNAL_SERVER_ERROR, null);
        }
    }

    public StatusResponse logout(DecodedJWT decodedJwt, String clientAddress) {
        try {
            String user = decodedJwt.getClaim("usuario").asString();
            System.out.println("[AuthService] User " + user + " logging out from " + clientAddress);

            // Validate clientAddress
            if (clientAddress == null || clientAddress.trim().isEmpty()) {
                System.err.println("[AuthService] ERROR: clientAddress is null or empty during logout");
                return new StatusResponse(StatusTable.BAD);
            }

            // Reset username (connection remains but user is logged out)
            ConnectionManager.getInstance().resetUsername(clientAddress);
            ServerView.updateConnection(clientAddress, null);

            System.out.println("[AuthService] User " + user + " logged out successfully");
            return new StatusResponse(StatusTable.OK);
        } catch (Exception e) {
            System.err.println("[AuthService] ERROR during logout: " + e.getMessage());
            e.printStackTrace();
            return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean isAdmin(DecodedJWT decodedJwt) {
        try {
            String funcao = decodedJwt.getClaim("funcao").asString();
            return "admin".equals(funcao);
        } catch (Exception e) {
            return false;
        }
    }
}
