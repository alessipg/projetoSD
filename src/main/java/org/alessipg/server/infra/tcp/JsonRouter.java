package org.alessipg.server.infra.tcp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import lombok.Setter;
import org.alessipg.shared.domain.model.Usuario;
import org.alessipg.server.app.service.UsuarioService;
import org.alessipg.server.util.JwtUtil;
import org.alessipg.shared.records.StatusResponse;
import org.alessipg.shared.records.UserLoginResponse;
import org.alessipg.shared.util.IntegerAsStringAdapter;

import java.util.Optional;

public class JsonRouter {
    // Setter para injetar dependências
    @Setter
    private static UsuarioService usuarioService;

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Integer.class, new IntegerAsStringAdapter())
            .create();

    // Validar JSON antes de processar
    public static String parse(String message) {
        if (message == null || message.trim().isEmpty()) {
            System.err.println("Empty message received");
            return gson.toJson(new StatusResponse("400"));
        }

        try {
            JsonObject json = JsonParser.parseString(message).getAsJsonObject();
            // Validar campos obrigatórios
            if (!json.has("operacao")) {
                System.err.println("Missing operation");
                return gson.toJson(new StatusResponse("400"));
            }

            String operacao = json.get("operacao").getAsString();

            // Switch na operação
            switch (operacao) {
                case "LOGIN":
                    return login(json);
                case "LOGOUT":
                    String token = json.get("token").getAsString();
                    return logout();
                default:
                    return null;
            }
        } catch (JsonSyntaxException e) {
            System.err.println("Invalid JSON format: " + e.getMessage());
            return gson.toJson(new StatusResponse("400"));
        }
    }

    private static String logout() {
        return "";
    }

    private static String login(JsonObject json) {
        String user = json.get("user").getAsString();
        String senha = json.get("senha").getAsString();
        System.out.println("Login attempt for user: " + user);

        if (usuarioService != null) {
            Optional<Usuario> usuarioOpt = usuarioService.buscarPorNome(user);

            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();

                // Validar senha (você pode implementar hash depois)
                if (usuario.getSenha().equals(senha)) {
                    // Gerar token JWT
                    String token = JwtUtil.gerarToken(
                            usuario.getId(),
                            usuario.getNome(),
                            "user" // ou campo funcao se existir
                    );
                    System.out.println("Login successful. Token: " + token);
                    UserLoginResponse msg = new UserLoginResponse("200", token);
                    String ret = gson.toJson(msg, UserLoginResponse.class);
                    System.out.println("Retornando: " + ret);
                    return ret;
                    // Aqui você enviaria o token de volta para o cliente
                } else {
                    return gson.toJson(new StatusResponse("401"));
                }
            } else {
                return gson.toJson(new StatusResponse("404"));
            }
        }
        return gson.toJson(new StatusResponse("405"));
    }
}
