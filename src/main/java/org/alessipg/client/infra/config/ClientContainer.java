package org.alessipg.client.infra.config;

import org.alessipg.client.app.clientservice.AuthClientService;
import org.alessipg.client.app.clientservice.MovieClientService;
import org.alessipg.client.app.clientservice.UserClientService;
import org.alessipg.client.infra.session.SessionManager;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ClientContainer {

    public static void initialize() {
        Gson gson = new GsonBuilder().create();

        AuthClientService auth = new AuthClientService(gson);
        MovieClientService movie = new MovieClientService(gson);
        UserClientService user = new UserClientService(gson);
        SessionManager.init(auth, movie, user);
    }

    
}
