package org.alessipg.server.infra.config;

import org.alessipg.server.app.controller.AuthController;
import org.alessipg.server.app.controller.UserController;
import org.alessipg.server.app.service.AuthService;
import org.alessipg.server.app.service.UsuarioService;
import org.alessipg.server.infra.repo.UsuarioRepository;
import org.alessipg.server.infra.tcp.JsonRouter;

import java.util.ArrayList;
import java.util.List;

public class ApplicationContainer implements AutoCloseable {
    
    private final List<AutoCloseable> resources;
    
    // Repositories
    private final UsuarioRepository usuarioRepository;
    
    // Services
    private final UsuarioService usuarioService;
    private final AuthService authService;
    
    // Controllers
    private final UserController usuarioController;
    private final AuthController authController;
    
    public ApplicationContainer() {
        System.out.println("Iniciando ApplicationContainer...");
        this.resources = new ArrayList<>();
        
        // Inicializar repositories
        System.out.println("Inicializando UsuarioRepository...");
        this.usuarioRepository = new UsuarioRepository();
        this.resources.add(usuarioRepository);
        System.out.println("UsuarioRepository inicializado.");
        
        // Inicializar services
        System.out.println("Inicializando UsuarioService...");
        this.usuarioService = new UsuarioService(usuarioRepository);
        System.out.println("UsuarioService inicializado.");
        
        System.out.println("Inicializando AuthService...");
        this.authService = new AuthService(usuarioService);
        System.out.println("AuthService inicializado.");
        
        // Inicializar controllers
        System.out.println("Inicializando UsuarioController...");
        this.usuarioController = new UserController(usuarioService);
        System.out.println("UsuarioController inicializado.");
        
        System.out.println("Inicializando AuthController...");
        this.authController = new AuthController(authService);
        System.out.println("AuthController inicializado.");
        
        // Configurar injeção de dependência no JsonRouter
        System.out.println("Configurando JsonRouter...");
        this.configureJsonRouter();
        System.out.println("JsonRouter configurado.");
        
        System.out.println("ApplicationContainer inicializado com sucesso.");
    }
    
    private void configureJsonRouter() {
        JsonRouter.setUserController(usuarioController);
        JsonRouter.setAuthController(authController);
    }
    
    // Getters para acessar componentes se necessário
    public UsuarioRepository getUsuarioRepository() {
        return usuarioRepository;
    }
    
    public UsuarioService getUsuarioService() {
        return usuarioService;
    }
    
    public AuthService getAuthService() {
        return authService;
    }
    
    public UserController getUsuarioController() {
        return usuarioController;
    }
    
    public AuthController getAuthController() {
        return authController;
    }
    
    @Override
    public void close() throws Exception {
        System.out.println("Fechando ApplicationContainer...");
        
        // Fechar todos os recursos em ordem reversa
        for (int i = resources.size() - 1; i >= 0; i--) {
            try {
                AutoCloseable resource = resources.get(i);
                resource.close();
                System.out.println("Recurso " + resource.getClass().getSimpleName() + " fechado com sucesso.");
            } catch (Exception e) {
                System.err.println("Erro ao fechar recurso: " + e.getMessage());
            }
        }
        
        System.out.println("ApplicationContainer fechado com sucesso.");
    }
}