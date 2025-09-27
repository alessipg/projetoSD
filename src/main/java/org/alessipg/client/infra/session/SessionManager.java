package org.alessipg.client.infra.session;

import lombok.Getter;
import lombok.Setter;

public class SessionManager {
    private static SessionManager instance;
    @Getter
    @Setter
    private String token;

    public SessionManager(){

    }
    public static synchronized SessionManager getInstance() {
        if(instance == null)
            instance = new SessionManager();
        return instance;
    }

}
