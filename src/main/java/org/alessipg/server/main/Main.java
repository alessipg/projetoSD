package org.alessipg.server.main;

import javax.swing.*;
import org.alessipg.server.infra.repo.UsuarioRepository;
import org.alessipg.server.app.service.UsuarioService;
import org.alessipg.server.infra.tcp.JsonRouter;

import static org.alessipg.server.ui.ServerWindow.createAndShowGUI;

public class Main {

    public static void main(String[] args) {
        // Inicializar repositório e serviços
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        UsuarioService usuarioService = new UsuarioService(usuarioRepository);
        
        // Configurar o JsonRouter com as dependências
        JsonRouter.setUsuarioService(usuarioService);
        
        // Criar interface gráfica
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
        
        // Adicionar shutdown hook para fechar o banco corretamente
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            usuarioRepository.close();
        }));
    }
}