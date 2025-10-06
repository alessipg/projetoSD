package org.alessipg.client.app.clientservice;

import java.io.IOException;

import org.alessipg.client.infra.session.SessionManager;
import org.alessipg.client.infra.tcp.TcpClient;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.records.request.MovieCreateRequest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MovieClientService {

    private final Gson gson;
    private final TcpClient client;
    public MovieClientService(Gson gson){
        this.gson = gson;
        this.client = SessionManager.getClient();
    }
    public StatusTable criar(MovieCreateRequest msg) throws IOException {
        String json = gson.toJson(msg);
        client.send(json);
        String response = client.receive();
        if (response != null) {
            JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
            String status = jsonObject.has("status") ? jsonObject.get("status").getAsString() : "";
            switch (status) {
                case "201":
                    
                    break;
            
                default:
                    break;
            }
        }

        return null;
    }
}
