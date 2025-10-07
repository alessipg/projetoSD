package org.alessipg.client.infra.session;

import org.alessipg.client.app.clientservice.AuthClientService;
import org.alessipg.client.app.clientservice.MovieClientService;
import org.alessipg.client.app.clientservice.UserClientService;
import org.alessipg.client.infra.tcp.TcpClient;

import lombok.Getter;
import lombok.Setter;

public final class SessionManager {
    private static volatile SessionManager instance;

    @Getter
    @Setter
    private String token;
    @Getter
    @Setter
    public static TcpClient client;
    @Getter
    private final AuthClientService authClientService;
    @Getter
    private final MovieClientService movieClientService;
    @Getter
    private final UserClientService userClientService;

    private SessionManager(AuthClientService auth, MovieClientService movie,
    UserClientService user) {
        this.authClientService = auth;
        this.movieClientService = movie;
        this.userClientService = user;
    }

    public static synchronized void init(AuthClientService auth, MovieClientService movie, UserClientService user) {
        if (instance != null)
            throw new IllegalStateException("SessionManager já inicializado");
        instance = new SessionManager(auth, movie,user);
    }

    public static SessionManager getInstance() {
        if (instance == null)
            throw new IllegalStateException("Chame SessionManager.init(...) antes");
        return instance;
    }
}
