package org.alessipg.client.app.clientservice;

import java.io.IOException;

import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.client.infra.tcp.TcpClient;
// removed unused StatusTable import
import org.alessipg.client.util.StatusMapper;
import org.alessipg.shared.records.request.UserDeleteRequest;
import org.alessipg.shared.records.request.UserRegisterRequest;
import org.alessipg.shared.records.request.UserSelfGetRequest;
import org.alessipg.shared.records.request.UserUpdateRequest;
import org.alessipg.shared.records.response.UserSelfGetResponse;
import org.alessipg.shared.records.util.UserRecord;
import org.alessipg.shared.util.Result;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class UserClientService {

    private final Gson gson;
    private final TcpClient client;

    public UserClientService(Gson gson) {
        this.gson = gson;
        this.client = SessionManager.getClient();
    }

    public Result<Void> create(String usuario, String senha) throws IOException {
        UserRegisterRequest msg = new UserRegisterRequest(usuario, senha);
        String json = gson.toJson(msg);
        client.send(json);
        String response = client.receive();
        if (response == null) {
            throw new IOException("Conexão encerrada pelo servidor ou resposta vazia");
        }
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        return StatusMapper.map(
            jsonObject,
            j -> null,
            code -> {
                switch (code) {
                    case BAD:
                        return "Verifique as informações e tente novamente.";
                    case ALREADY_EXISTS:
                        return "Usuário já cadastrado!";
                    case UNPROCESSABLE_ENTITY:
                        return "Dados faltantes ou fora do padrão.";
                    case INTERNAL_SERVER_ERROR:
                    default:
                        return "Erro no servidor. Tente novamente mais tarde.";
                }
            }
        );
    }

    public Result<UserSelfGetResponse> selfGet() throws IOException {
        String token = SessionManager.getInstance().getToken();
        UserSelfGetRequest msg = new UserSelfGetRequest(token);
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
                String status = j.has("status") ? j.get("status").getAsString() : "";
                String user = j.has("usuario") ? j.get("usuario").getAsString() : "";
                return new UserSelfGetResponse(status, user.isEmpty() ? null : user);
            },
            code -> {
                switch (code) {
                    case UNAUTHORIZED:
                        return "Token inválido ou expirado. Faça login novamente.";
                    case UNPROCESSABLE_ENTITY:
                        return "Token ausente ou inválido.";
                    case NOT_FOUND:
                        return "Usuário não encontrado.";
                    case INTERNAL_SERVER_ERROR:
                    default:
                        return "Erro no servidor. Tente novamente mais tarde.";
                }
            }
        );
    }

    public Result<Void> update(String newPassword) throws IOException {
        UserRecord user = new UserRecord(null,newPassword);
        UserUpdateRequest msg = new UserUpdateRequest(user,SessionManager.getInstance().getToken());
        String json = gson.toJson(msg);
        client.send(json);
        String response = client.receive();
        if (response == null) {
            throw new IOException("Conexão encerrada pelo servidor ou resposta vazia");
        }
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        return StatusMapper.map(
            jsonObject,
            j -> null,
            code -> {
                switch (code) {
                    case BAD:
                        return "Algo deu errado, tente novamente!";
                    case NOT_FOUND:
                        return "Usuário não encontrado. Faça login novamente.";
                    case FORBIDDEN:
                        return "Acesso negado.";
                    case INTERNAL_SERVER_ERROR:
                    default:
                        return "Erro no servidor. Tente novamente mais tarde.";
                }
            }
        );
    }

    public Result<Void> delete() throws IOException {
        UserDeleteRequest msg = new UserDeleteRequest(SessionManager.getInstance().getToken());
        String json = gson.toJson(msg);
        client.send(json);
        String response = client.receive();
        if (response == null) {
            throw new IOException("Conexão encerrada pelo servidor ou resposta vazia");
        }
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        return StatusMapper.map(
            jsonObject,
            j -> null,
            code -> {
                switch (code) {
                    case BAD:
                        return "Algo deu errado, tente novamente!";
                    case NOT_FOUND:
                        return "Usuário não encontrado. Faça login novamente.";
                    case FORBIDDEN:
                        return "Acesso negado.";
                    case INTERNAL_SERVER_ERROR:
                    default:
                        return "Erro no servidor. Tente novamente mais tarde.";
                }
            }
        );
    }
}
