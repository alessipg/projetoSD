package org.alessipg.server.infra.tcp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import lombok.Setter;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.server.app.controller.AuthController;
import org.alessipg.server.app.controller.UserController;
import org.alessipg.server.app.controller.MovieController;
import org.alessipg.shared.records.StatusResponse;
import org.alessipg.shared.util.IntegerAsStringAdapter;



public class JsonRouter {
    // Setter para injetar dependências
    @Setter
    private static UserController UserController;
    @Setter
    private static AuthController AuthController;
    @Setter
    private static MovieController MovieController;

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Integer.class, new IntegerAsStringAdapter())
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
            System.out.println("Operation: " + operation);
            System.out.println("Full JSON: " + json);
            // Switch na operação
            switch (operation) {
                case "LOGIN":
                    return AuthController.login(json);
                case "LOGOUT":
                    return AuthController.logout(json);
                case "CRIAR_USUARIO":
                    return UserController.create(json);
                case "CRIAR_FILME":
                    return MovieController.create(json);
                default:
                    return null;
            }
        } catch (JsonSyntaxException e) {
            System.err.println("Invalid JSON format: " + e.getMessage());
            return gson.toJson(new StatusResponse(StatusTable.BAD));
        }
    }
}
