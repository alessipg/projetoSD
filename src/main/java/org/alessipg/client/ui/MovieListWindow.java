package org.alessipg.client.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MovieListWindow extends JFrame {

    private JTable tabelaFilmes;
    private JButton btnConfiguracoes;
    private JButton btnAdmin;

    public MovieListWindow() {
        setTitle("Lista de Filmes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null); // Centraliza a janela
        setLayout(new BorderLayout(10, 10));

        // ===== Modelo da Tabela =====
        String[] colunas = {"Título", "Ano", "Nota Geral", "Gênero", "Diretor"};
        Object[][] dadosExemplo = {
                {"O Poderoso Chefão", 1972, 9.2, "Crime/Drama", "Francis Ford Coppola"},
                {"Interestelar", 2014, 8.6, "Ficção Científica", "Christopher Nolan"},
                {"Parasita", 2019, 8.6, "Suspense/Drama", "Bong Joon-ho"},
                {"Cidade de Deus", 2002, 8.7, "Crime/Drama", "Fernando Meirelles"},
                {"A Origem", 2010, 8.8, "Ação/Ficção", "Christopher Nolan"}
        };

        DefaultTableModel modelo = new DefaultTableModel(dadosExemplo, colunas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // impede edição das células
            }
        };

        tabelaFilmes = new JTable(modelo);
        tabelaFilmes.setFillsViewportHeight(true);
        tabelaFilmes.setRowHeight(28);
        tabelaFilmes.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tabelaFilmes);
        add(scrollPane, BorderLayout.CENTER);

        // ===== Painel de Botões =====
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnConfiguracoes = new JButton("Configurações");
        btnAdmin = new JButton("Área Admin");

        painelBotoes.add(btnConfiguracoes);
        painelBotoes.add(btnAdmin);

        add(painelBotoes, BorderLayout.SOUTH);
        btnAdmin.addActionListener(e -> {
            AdminWindow adminWindow = new AdminWindow();
            adminWindow.setVisible(true);
        });
    }
}