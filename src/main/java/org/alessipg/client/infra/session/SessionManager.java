package org.alessipg.client.infra.session;

import org.alessipg.client.app.clientservice.AuthClientService;
import org.alessipg.client.app.clientservice.MovieClientService;
import org.alessipg.client.app.clientservice.ReviewClientService;
import org.alessipg.client.app.clientservice.UserClientService;
import org.alessipg.client.infra.tcp.TcpClient;
import org.alessipg.shared.dto.util.MovieRecord;

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
    @Getter
    private final ReviewClientService reviewClientService;
    @Getter
    @Setter
    private MovieRecord currentMovie;

    private SessionManager(AuthClientService auth, MovieClientService movie,
    UserClientService user, ReviewClientService review) {
         this.authClientService = auth;
        this.movieClientService = movie;
        this.userClientService = user;
        this.reviewClientService = review;
    }

    public static synchronized void init(AuthClientService auth, MovieClientService movie, UserClientService user, ReviewClientService review) {
        if (instance != null)
            throw new IllegalStateException("SessionManager já inicializado");
        instance = new SessionManager(auth, movie,user, review);
    }

    public static SessionManager getInstance() {
        if (instance == null)
            throw new IllegalStateException("Chame SessionManager.init(...) antes");
        return instance;
    }
}
