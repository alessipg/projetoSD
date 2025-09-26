package org.alessipg.client.ui;

import javax.swing.*;
import java.awt.*;

public class AdminWindow extends JFrame {

    private JButton btnUsuarios;
    private JButton btnFilmes;

    public AdminWindow() {
        setTitle("Área Admin");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null); // Centraliza a janela
        setLayout(new BorderLayout(10, 10));

        // ===== Título =====
        JLabel lblTitulo = new JLabel("Área de Administração", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(lblTitulo, BorderLayout.NORTH);

        // ===== Painel de Botões =====
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new GridLayout(2, 1, 15, 15));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        btnUsuarios = new JButton("Usuários");
        btnFilmes = new JButton("Filmes");

        painelBotoes.add(btnUsuarios);
        painelBotoes.add(btnFilmes);

        add(painelBotoes, BorderLayout.CENTER);

        btnUsuarios.addActionListener(e -> {
            UserManager userManager = new UserManager();
            userManager.setVisible(true);
        });

        btnFilmes.addActionListener(e -> {
            MovieManager movieManager = new MovieManager();
            movieManager.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminWindow tela = new AdminWindow();
            tela.setVisible(true);
        });
    }
}