package org.alessipg.client.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {

	public LoginWindow() {
		setTitle("Tela de Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 300);
		setLocationRelativeTo(null); // Centraliza na tela

		// Painel principal com GridBagLayout
		JPanel painel = new JPanel(new GridBagLayout());
		painel.setBackground(new Color(245, 245, 245));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10); // Espaçamento

		// Título
		JLabel titulo = new JLabel("Bem-vindo");
		titulo.setFont(new Font("Arial", Font.BOLD, 22));
		titulo.setForeground(new Color(50, 50, 150));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		painel.add(titulo, gbc);

		// Label usuário
		JLabel lblUsuario = new JLabel("Usuário:");
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		painel.add(lblUsuario, gbc);

		// Campo usuário
		JTextField txtUsuario = new JTextField(15);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		painel.add(txtUsuario, gbc);

		// Label senha
		JLabel lblSenha = new JLabel("Senha:");
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.LINE_END;
		painel.add(lblSenha, gbc);

		// Campo senha
		JPasswordField txtSenha = new JPasswordField(15);
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		painel.add(txtSenha, gbc);

		// Botão Login
		JButton btnLogin = new JButton("Login");
		btnLogin.setBackground(new Color(70, 130, 180));
		btnLogin.setForeground(Color.WHITE);
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.CENTER;
		painel.add(btnLogin, gbc);

		// Botão Criar Conta
		JButton btnCriarConta = new JButton("Criar Conta");
		btnCriarConta.setBackground(new Color(34, 139, 34));
		btnCriarConta.setForeground(Color.WHITE);
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.CENTER;
		painel.add(btnCriarConta, gbc);

		add(painel);
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}

		});
		btnCriarConta.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Criando Conta " + txtUsuario.getText() + " " + txtSenha.getText());
			}
		});
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			LoginWindow tela = new LoginWindow();
			tela.setVisible(true);
		});
	}
}