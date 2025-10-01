package org.alessipg.client.app.clientservice;

import java.io.IOException;

import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.records.MovieCreateRequest;

import com.google.gson.JsonObject;

public class FilmeClientService extends ClientService {

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
