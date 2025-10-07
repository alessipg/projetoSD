package org.alessipg.server.main;

import javax.swing.*;
import org.alessipg.server.infra.config.ServerContainer;

import static org.alessipg.server.ui.ServerView.createAndShowGUI;

public class ServerMain {

    public static void main(String[] args) {
        ServerContainer container = null;
        
        try {
            System.out.println("Antes de inicializar ApplicationContainer");
            container = new ServerContainer();
            System.out.println("ApplicationContainer inicializado");

            // Criar interface gráfica
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    System.out.println("Chamando createAndShowGUI");
                    createAndShowGUI();
                }
            });
            
            // Adicionar shutdown hook para garantir fechamento correto do banco
            final ServerContainer finalContainer = container;
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Iniciando shutdown do servidor...");
                try {
                    if (finalContainer != null) {
                        finalContainer.close();
                    }
                } catch (Exception e) {
                    System.err.println("Erro durante shutdown: " + e.getMessage());
                    e.printStackTrace();
                }
                System.out.println("Shutdown concluído.");
            }));
            
            System.out.println("Servidor iniciado com sucesso!");
            
        } catch (Exception e) {
            System.err.println("Erro ao inicializar servidor: " + e.getMessage());
            e.printStackTrace();
            
            // Tentar fechar container se algo deu errado
            if (container != null) {
                try {
                    container.close();
                } catch (Exception closeError) {
                    System.err.println("Erro ao fechar container: " + closeError.getMessage());
                }
            }
            
            System.exit(1);
        }
    }
}