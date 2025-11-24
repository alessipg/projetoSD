package org.alessipg.server.infra.tcp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import lombok.Setter;

import org.alessipg.server.app.controller.ReviewController;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.dto.response.StatusResponse;
import org.alessipg.server.app.controller.AuthController;
import org.alessipg.server.app.controller.UserController;
import org.alessipg.server.app.controller.MovieController;


public class JsonRouter {
    // Setter para injetar dependências
    //TODO: corrigir essa coisa feia
    @Setter
    private static UserController UserController;
    @Setter
    private static AuthController AuthController;
    @Setter
    private static MovieController MovieController;
    @Setter
    private static ReviewController ReviewController;

    private static final Gson gson = new GsonBuilder()
            .create();
    // Validar JSON antes de processar

    public static String parse(String message) {
        if (message == null || message.trim().isEmpty()) {
            System.err.println("Empty message received");
            return gson.toJson(new StatusResponse(StatusTable.BAD));
        }

        try {
            JsonObject json = JsonParser.parseString(message).getAsJsonObject();
            // Validar campos obrigatórios
            if (!json.has("operacao")) {
                System.err.println("Missing operation");
                return gson.toJson(new StatusResponse(StatusTable.UNPROCESSABLE_ENTITY));
            }
            String operation = json.get("operacao").getAsString();
            // Switch na operação
            // TODO: se sobrar tempo, transformar em um hashmap
            switch (operation) {
                // Auth
                case "LOGIN":
                    return AuthController.login(json);
                case "LOGOUT":
                    return AuthController.logout(json);
                // Create
                case "CRIAR_USUARIO":
                    return UserController.create(json);
                case "CRIAR_FILME":
                    return MovieController.create(json);
                case "CRIAR_REVIEW":
                    return ReviewController.create(json);
                // Read
                case "LISTAR_FILMES":
                    return MovieController.getAll();
                case "BUSCAR_FILME_ID":
                    return MovieController.getById(json);
                case "LISTAR_USUARIOS":
                    return UserController.getAll(json);
                case "LISTAR_PROPRIO_USUARIO":
                    return UserController.selfGet(json);
                case "LISTAR_REVIEWS_USUARIO":
                    return ReviewController.getByUser(json);
                // Update
                case "EDITAR_PROPRIO_USUARIO":
                    return UserController.update(json);
                case "EDITAR_FILME":
                    return MovieController.update(json);
                case "ADMIN_EDITAR_USUARIO":
                    return UserController.adminUpdate(json);
                case "EDITAR_REVIEW":
                    return ReviewController.update(json);
                // Delete
                case "EXCLUIR_PROPRIO_USUARIO":
                    return UserController.selfDelete(json);
                case "EXCLUIR_FILME":
                    return MovieController.delete(json);
                case "ADMIN_EXCLUIR_USUARIO":
                    return UserController.adminDelete(json);
                case "EXCLUIR_REVIEW":
                    return ReviewController.delete(json);
                default:
                    return gson.toJson(new StatusResponse(StatusTable.UNPROCESSABLE_ENTITY));
            }
        } catch (JsonSyntaxException e) {
            System.err.println("Invalid JSON format: " + e.getMessage());
            return gson.toJson(new StatusResponse(StatusTable.UNPROCESSABLE_ENTITY));
        }
    }
}
