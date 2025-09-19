package org.alessipg.client.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.regex.Pattern;
import org.alessipg.client.infra.tcp.TcpClient;

public class TcpConnectionDialog extends JPanel {
    private final JTextField ipField = new JTextField();
    private final JSpinner portSpinner = new JSpinner(new SpinnerNumberModel(502, 1, 65535, 1));
    private final JButton connectButton = new JButton("Conectar");
    private final JButton cancelButton = new JButton("Cancelar");
    private final JLabel statusLabel = new JLabel(" ");
    private SwingWorker<Void, Void> worker;

    // padrão simples para IPv4 (aceita também nomes de host ao tentar conectar)
    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d?\\d)$");

    public TcpConnectionDialog() {
        setLayout(new BorderLayout(8, 8));
        setBorder(new EmptyBorder(12, 12, 12, 12));
        initUI();
        initListeners();
        updateConnectButtonState();
    }

    private void initUI() {
        // Painel central com inputs
        JPanel inputs = new JPanel(new GridBagLayout());
        inputs.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label IP
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        inputs.add(new JLabel("IP / Host:"), gbc);

        // Field IP
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        ipField.setColumns(20);
        ipField.setToolTipText("Digite um endereço IPv4 (ex: 192.168.0.10) ou hostname (ex: example.com)");
        ipField.setText("127.0.0.1");
        inputs.add(ipField, gbc);

        // Label Porta
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        inputs.add(new JLabel("Porta:"), gbc);

        // Spinner porta
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        ((JSpinner.DefaultEditor) portSpinner.getEditor()).getTextField().setToolTipText("Porta TCP (1-65535)");
        inputs.add(portSpinner, gbc);
        portSpinner.setValue(8080);

        add(inputs, BorderLayout.CENTER);

        // Painel de botões
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        connectButton.setPreferredSize(new Dimension(110, 28));
        cancelButton.setPreferredSize(new Dimension(110, 28));
        cancelButton.setEnabled(false);
        buttons.add(cancelButton);
        buttons.add(connectButton);

        // Status embaixo
        JPanel south = new JPanel(new BorderLayout());
        south.add(statusLabel, BorderLayout.CENTER);
        south.add(buttons, BorderLayout.SOUTH);
        statusLabel.setBorder(new EmptyBorder(6, 0, 6, 0));

        add(south, BorderLayout.SOUTH);

        // Estética simples (flat)
        ipField.setBackground(Color.WHITE);
        portSpinner.setBackground(Color.WHITE);
        setPreferredSize(new Dimension(420, 160));
    }

    private void initListeners() {
        // Atualiza estado do botão ao digitar IP
        ipField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateConnectButtonState();
            }

            public void removeUpdate(DocumentEvent e) {
                updateConnectButtonState();
            }

            public void changedUpdate(DocumentEvent e) {
                updateConnectButtonState();
            }
        });

        // Atualiza quando porta muda
        portSpinner.addChangeListener(e -> updateConnectButtonState());

        connectButton.addActionListener(this::onConnect);
        cancelButton.addActionListener(e -> onCancel());
    }

    private void updateConnectButtonState() {
        String text = ipField.getText().trim();
        int port = (int) portSpinner.getValue();
        boolean ipNotEmpty = !text.isEmpty();
        boolean portOk = port >= 1 && port <= 65535;
        connectButton.setEnabled(ipNotEmpty && portOk && worker == null);
    }

    private void onConnect(ActionEvent evt) {
        String host = ipField.getText().trim();
        int port = (int) portSpinner.getValue();

        // Validação visual básica:
        if (host.isEmpty()) {
            showError("Informe um IP ou hostname.");
            return;
        }
        if (port < 1 || port > 65535) {
            showError("Porta inválida. Use 1–65535.");
            return;
        }

        // Ajusta UI
        statusLabel.setForeground(Color.DARK_GRAY);
        statusLabel.setText("Conectando a " + host + ":" + port + " ...");
        connectButton.setEnabled(false);
        cancelButton.setEnabled(true);

        // Worker para não bloquear a EDT
        worker = new SwingWorker<>() {
            private String message = null;
            private boolean success = false;

            @Override
            protected Void doInBackground() {
                // tentativa de conexão com timeout (ms)
                final int TIMEOUT_MS = 3000;
                try (Socket socket = new Socket()) {
                    InetSocketAddress endpoint = new InetSocketAddress(host, port);
                    socket.connect(endpoint, TIMEOUT_MS);
                    success = true;
                    message = "Conectado com sucesso a " + host + ":" + port;
                } catch (IOException ex) {
                    success = false;
                    message = "Falha: " + ex.getClass().getSimpleName() + " — " + ex.getMessage();
                }
                return null;
            }

            @Override
            protected void done() {
                connectButton.setEnabled(true);
                cancelButton.setEnabled(false);
                worker = null;

                if (isCancelled()) {
                    statusLabel.setForeground(Color.ORANGE.darker());
                    statusLabel.setText("Conexão cancelada.");
                    return;
                }

                if (success) {
                    statusLabel.setForeground(new Color(0, 128, 0));
                    statusLabel.setText(message);
                    // Crie o TcpClient e vá para o login
                    TcpClient client = new TcpClient(host, port);
                    try {
                        client.connect();
                        SwingUtilities.invokeLater(() -> {
                            LoginWindow tela = new LoginWindow(client);
                            tela.setVisible(true); // IMPORTANTE: tornar a janela visível
                        });
                        connectButton.setEnabled(false);
                    } catch (IOException ex) {
                        showError("Erro ao criar cliente: " + ex.getMessage());
                    }
                } else {
                    statusLabel.setForeground(Color.RED.darker());
                    statusLabel.setText(message);
                }
            }
        };

        worker.execute();
    }

    private void onCancel() {
        if (worker != null) {
            worker.cancel(true);
        }
        connectButton.setEnabled(true);
        cancelButton.setEnabled(false);
    }

    private void showError(String text) {
        statusLabel.setForeground(Color.RED.darker());
        statusLabel.setText(text);
    }

    public static void initClient() {
        // Opcional: look and feel nativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Teste de Conexão TCP");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new TcpConnectionDialog());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}