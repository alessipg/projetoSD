package org.alessipg.client.app.clientservice;


import com.google.gson.Gson;
import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.client.infra.tcp.TcpClient;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.dto.request.UserLoginRequest;
import org.alessipg.shared.dto.request.UserLogoutRequest;
import org.alessipg.shared.dto.response.StatusResponse;
import org.alessipg.shared.dto.response.UserLoginResponse;

import java.io.IOException;

public class AuthClientService {

    private final Gson gson;
    private final TcpClient client;

    public AuthClientService(Gson gson) {
        this.gson = gson;
        this.client = SessionManager.getClient();
    }

    public UserLoginResponse login(String usuario, String senha) throws IOException {
        UserLoginRequest msg = new UserLoginRequest(usuario, senha);
        String json = gson.toJson(msg);
        client.send(json);
        String response = client.receive();
        if (response == null) {
            return new UserLoginResponse(StatusTable.INTERNAL_SERVER_ERROR, null);
        }
        try {
            UserLoginResponse res = gson.fromJson(response, UserLoginResponse.class);
            if (res == null) {
                return new UserLoginResponse(StatusTable.INTERNAL_SERVER_ERROR, null);
            }
            String status = res.status();
            switch (status) {
                case "200":
                    if (res.token() == null)
                        return new UserLoginResponse(StatusTable.INTERNAL_SERVER_ERROR, null);
                    SessionManager.getInstance().setToken(res.token());
                    return res;
                case "400":
                    return new UserLoginResponse(StatusTable.BAD, null);
                case "403":
                    return new UserLoginResponse(StatusTable.FORBIDDEN, null);
                case "422":
                    return new UserLoginResponse(StatusTable.UNPROCESSABLE_ENTITY, null);
                default:
                    return new UserLoginResponse(StatusTable.INTERNAL_SERVER_ERROR, null);
            }
        } catch (Exception e) {
            return new UserLoginResponse(StatusTable.INTERNAL_SERVER_ERROR, null);
        }
    }


    public StatusResponse logout() throws IOException {
        String token = SessionManager.getInstance().getToken();
        UserLogoutRequest msg = new UserLogoutRequest(token);
        String json = gson.toJson(msg);
        client.send(json);
        String response = client.receive();
        if (response == null) {
            return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
        }
        try {
            StatusResponse res = gson.fromJson(response, StatusResponse.class);
            if (res != null) {
                return res;
            } else {
                return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new StatusResponse(StatusTable.INTERNAL_SERVER_ERROR);
        }
    }
}
