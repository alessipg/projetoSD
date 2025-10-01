package org.alessipg.client.infra.session;

import org.alessipg.client.app.clientservice.AuthClientService;
import org.alessipg.client.app.clientservice.FilmeClientService;

import lombok.Getter;
import lombok.Setter;

public class SessionManager {
    private static SessionManager instance;

    @Getter @Setter
    private String token;

    @Getter
    private final AuthClientService authClientService = new AuthClientService();
    @Getter
    private final FilmeClientService filmeClientService = new FilmeClientService();
    
    private SessionManager() {}

    public static synchronized SessionManager getInstance() {
        if(instance == null)
            instance = new SessionManager();
        return instance;
    }
}
