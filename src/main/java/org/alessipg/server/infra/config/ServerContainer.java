package org.alessipg.server.infra.config;

import org.alessipg.server.app.controller.AuthController;
import org.alessipg.server.app.controller.MovieController;
import org.alessipg.server.app.controller.UserController;
import org.alessipg.server.app.service.AuthService;
import org.alessipg.server.app.service.MovieService;
import org.alessipg.server.app.service.UserService;
import org.alessipg.server.infra.repo.MovieRepository;
import org.alessipg.server.infra.repo.UserRepository;
import org.alessipg.server.infra.tcp.JsonRouter;

import com.google.gson.Gson;

public class ServerContainer implements AutoCloseable {

    // Repositories
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    // Services
    private final UserService userService;
    private final AuthService authService;
    private final MovieService movieService;
    // Controllers
    private final UserController userController;
    private final AuthController authController;
    private final MovieController movieController;

    public ServerContainer() {
        System.out.println("Iniciando ApplicationContainer...");

        // instantiating
        Jpa.getEntityManager();
        // repositories
        this.userRepository = new UserRepository();
        this.movieRepository = new MovieRepository();

        // services
        this.userService = new UserService(userRepository);
        this.movieService = new MovieService(movieRepository);
        this.authService = new AuthService(userService);

        // controllers
        Gson gson = Jsons.get();
        this.userController = new UserController(userService, gson);
        this.movieController = new MovieController(movieService, gson);
        this.authController = new AuthController(authService, gson);

        // Configurar injeção de dependência no JsonRouter
        this.configureJsonRouter();

        System.out.println("ApplicationContainer inicializado com sucesso.");
    }

    private void configureJsonRouter() {
        JsonRouter.setUserController(userController);
        JsonRouter.setAuthController(authController);
        JsonRouter.setMovieController(movieController);
    }

    @Override
    public void close() throws Exception {
        System.out.println("Fechando ApplicationContainer...");
        Jpa.close();

        System.out.println("ApplicationContainer fechado com sucesso.");
    }
}