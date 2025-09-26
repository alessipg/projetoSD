package org.alessipg.server.infra.config;

import org.alessipg.server.app.controller.AuthController;
import org.alessipg.server.app.controller.UsuarioController;
import org.alessipg.server.app.service.AuthService;
import org.alessipg.server.app.service.UsuarioService;
import org.alessipg.server.infra.repo.UsuarioRepository;
import org.alessipg.server.infra.tcp.JsonRouter;

import java.util.ArrayList;
import java.util.List;

/**
 * Container de injeção de dependência para organizar e gerenciar
 * todas as dependências da aplicação.
 */
public class ApplicationContainer implements AutoCloseable {
    
    private final List<AutoCloseable> resources;
    
    // Repositories
    private final UsuarioRepository usuarioRepository;
    
    // Services
    private final UsuarioService usuarioService;
    private final AuthService authService;
    
    // Controllers
    private final UsuarioController usuarioController;
    private final AuthController authController;
    
    public ApplicationContainer() {
        this.resources = new ArrayList<>();
        
        // Inicializar repositories
        this.usuarioRepository = new UsuarioRepository();
        this.resources.add(usuarioRepository);
        
        // Inicializar services
        this.usuarioService = new UsuarioService(usuarioRepository);
        this.authService = new AuthService(usuarioService);
        
        // Inicializar controllers
        this.usuarioController = new UsuarioController(usuarioService);
        this.authController = new AuthController(authService);
        
        // Configurar injeção de dependência no JsonRouter
        this.configureJsonRouter();
        
        System.out.println("ApplicationContainer inicializado com sucesso.");
    }
    
    private void configureJsonRouter() {
        JsonRouter.setUsuarioController(usuarioController);
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
    
    public UsuarioController getUsuarioController() {
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
                // Log do erro, mas continua fechando os outros recursos
            }
        }
        
        System.out.println("ApplicationContainer fechado com sucesso.");
    }
}