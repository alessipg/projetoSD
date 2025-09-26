package org.alessipg.server.infra.tcp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import lombok.Setter;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.server.app.controller.AuthController;
import org.alessipg.server.app.controller.UsuarioController;
import org.alessipg.shared.records.StatusResponse;
import org.alessipg.shared.util.IntegerAsStringAdapter;



public class JsonRouter {
    // Setter para injetar dependências
    @Setter
    private static UsuarioController usuarioController;
    @Setter
    private static AuthController authController;

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
            String operacao = json.get("operacao").getAsString();
            System.out.println("Operation: " + operacao);
            System.out.println("Full JSON: " + json);
            // Switch na operação
            switch (operacao) {
                case "LOGIN":
                    return authController.login(json);
                case "LOGOUT":
                    return authController.logout(json);
                case "CRIAR_USUARIO":
                    return usuarioController.criar(json);
                default:
                    return null;
            }
        } catch (JsonSyntaxException e) {
            System.err.println("Invalid JSON format: " + e.getMessage());
            return gson.toJson(new StatusResponse(StatusTable.BAD));
        }
    }
}
