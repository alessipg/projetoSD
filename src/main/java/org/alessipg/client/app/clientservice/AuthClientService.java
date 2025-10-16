package org.alessipg.client.app.clientservice;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.client.infra.tcp.TcpClient;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.util.Result;
import org.alessipg.shared.records.request.UserLoginRequest;
import org.alessipg.shared.records.request.UserLogoutRequest;

import java.io.IOException;

public class AuthClientService {

    private final Gson gson;
    private final TcpClient client;
    public AuthClientService(Gson gson) {
        this.gson = gson;
        this.client = SessionManager.getClient();
    }
    public Result<Void> login(String usuario, String senha) throws IOException {
        UserLoginRequest msg = new UserLoginRequest(usuario, senha);
        String json = gson.toJson(msg);
        client.send(json);
        String response = client.receive();
        if (response == null) {
            throw new IOException("Conexão encerrada pelo servidor ou resposta vazia");
        }
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        return StatusMapper.map(
                jsonObject,
                j -> {
                    String token = j.has("token") ? j.get("token").getAsString() : null;
                    if (token != null) {
                        SessionManager.getInstance().setToken(token);
                    }
                    return null;
                },
                code -> {
                    switch (code) {
                        case UNAUTHORIZED:
                            return "Usuário ou senha incorretos!";
                        case BAD:
                            return "Requisição inválida. Verifique os dados e tente novamente.";
                        case FORBIDDEN:
                            return "Acesso negado.";
                        case NOT_FOUND:
                            return "Usuário ou senha inválidos.";
                        case ALREADY_EXISTS:
                            return "Conflito ao processar a solicitação.";
                        case UNPROCESSABLE_ENTITY:
                            return "Dados faltantes ou fora do padrão.";
                        case INTERNAL_SERVER_ERROR:
                        default:
                            return "Erro no servidor. Tente novamente mais tarde.";
                    }
                }
        );
    }



    public StatusTable logout() throws IOException {
        String token = SessionManager.getInstance().getToken();
        UserLogoutRequest msg = new UserLogoutRequest(token);
        String json = gson.toJson(msg);
        client.send(json);
        String response = client.receive();
        if (response != null) {
            JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
            String status = jsonObject.has("status") ? jsonObject.get("status").getAsString() : "";
            switch (status) {
                case "200":
                    SessionManager.getInstance().setToken(null);
                    return StatusTable.OK;
                case "400":
                    return StatusTable.BAD;
                case "404":
                    return StatusTable.NOT_FOUND;
                case "500":
                    return StatusTable.INTERNAL_SERVER_ERROR;
                default:
                    return StatusTable.IM_TEAPOT;
            }
        } else {
            return StatusTable.INTERNAL_SERVER_ERROR;
        }
    }
}
