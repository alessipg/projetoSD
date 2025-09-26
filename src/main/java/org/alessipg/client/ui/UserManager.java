package org.alessipg.client.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UserManager extends JFrame {

    private JTable tabelaUsuarios;
    private DefaultTableModel modelo;
    private JButton btnEditar;
    private JButton btnExcluir;

    public UserManager() {
        setTitle("Lista de Usuários");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ===== Modelo da Tabela =====
        modelo = new DefaultTableModel(new Object[]{"Nome"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // não permite edição direta
            }
        };

        // Exemplo de dados
        modelo.addRow(new Object[]{"Alice"});
        modelo.addRow(new Object[]{"Bruno"});
        modelo.addRow(new Object[]{"Carla"});

        tabelaUsuarios = new JTable(modelo);
        tabelaUsuarios.setRowHeight(28);
        tabelaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tabelaUsuarios);
        add(scrollPane, BorderLayout.CENTER);

        // ===== Painel de Botões =====
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");

        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);

        add(painelBotoes, BorderLayout.SOUTH);

        // ===== Ações =====
        btnExcluir.addActionListener(e -> excluirUsuario());
        btnEditar.addActionListener(e -> editarUsuario());
    }

    private void excluirUsuario() {
        int row = tabelaUsuarios.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para excluir.");
            return;
        }

        String nome = modelo.getValueAt(row, 0).toString();
        int opcao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir o usuário \"" + nome + "\"?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION
        );
        
        if (opcao == JOptionPane.YES_OPTION) {
            modelo.removeRow(row);
            JOptionPane.showMessageDialog(this, "Usuário \"" + nome + "\" excluído com sucesso!");
        }
    }

    private void editarUsuario() {
        int row = tabelaUsuarios.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para editar.");
            return;
        }

        String nome = modelo.getValueAt(row, 0).toString();
        String senha = JOptionPane.showInputDialog(
                this,
                "Digite a nova senha para \"" + nome + "\":",
                "Editar Usuário",
                JOptionPane.PLAIN_MESSAGE
        );

        if (senha != null && !senha.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Senha de \"" + nome + "\" alterada com sucesso!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserManager tela = new UserManager();
            tela.setVisible(true);
        });
    }
}