package org.alessipg.client.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.concurrent.Task;
import org.alessipg.client.infra.tcp.TcpClient;
import org.alessipg.client.infra.tcp.TcpClientHolder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TcpConnectionController {

    @FXML
    private TextField ipField;
    @FXML
    private TextField portField;
    @FXML
    private Button connectButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label statusLabel;

    private Task<Void> connectionTask;

    @FXML
    public void initialize() {
        ipField.setText("127.0.0.1");
        portField.setText("8080");
        connectButton.setDisable(false);
        cancelButton.setDisable(true);

        ipField.textProperty().addListener((obs, oldVal, newVal) -> updateConnectButtonState());
        portField.textProperty().addListener((obs, oldVal, newVal) -> updateConnectButtonState());
    }

    private void updateConnectButtonState() {
        String ip = ipField.getText().trim();
        String portStr = portField.getText().trim();
        boolean ipNotEmpty = !ip.isEmpty();
        boolean portOk = false;
        try {
            int port = Integer.parseInt(portStr);
            portOk = port >= 1 && port <= 65535;
        } catch (NumberFormatException ignored) {
        }
        connectButton.setDisable(!(ipNotEmpty && portOk) || connectionTask != null);
    }

    @FXML
    private void onConnect() {
        String host = ipField.getText().trim();
        int port;
        try {
            port = Integer.parseInt(portField.getText().trim());
        } catch (NumberFormatException e) {
            showError("Porta inválida. Use 1–65535.");
            return;
        }

        if (host.isEmpty()) {
            showError("Informe um IP ou hostname.");
            return;
        }
        if (port < 1 || port > 65535) {
            showError("Porta inválida. Use 1–65535.");
            return;
        }

        statusLabel.setTextFill(Color.DARKGRAY);
        statusLabel.setText("Conectando a " + host + ":" + port + " ...");
        connectButton.setDisable(true);
        cancelButton.setDisable(false);

        connectionTask = new Task<>() {
            private String message = null;
            private boolean success = false;

            @Override
            protected Void call() {
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
            protected void succeeded() {
                finishConnection(success, message, host, port);
            }

            @Override
            protected void cancelled() {
                statusLabel.setTextFill(Color.ORANGE);
                statusLabel.setText("Conexão cancelada.");
                connectButton.setDisable(false);
                cancelButton.setDisable(true);
                connectionTask = null;
            }

            @Override
            protected void failed() {
                finishConnection(false, "Erro inesperado.", host, port);
            }
        };

        new Thread(connectionTask).start();
    }

    private void finishConnection(boolean success, String message, String host, int port) {
        Platform.runLater(() -> {
            connectButton.setDisable(false);
            cancelButton.setDisable(true);
            connectionTask = null;

            if (success) {
                statusLabel.setTextFill(Color.GREEN);
                statusLabel.setText(message);
                try {
                    // Criar e configurar o cliente TCP
                    TcpClient client = new TcpClient(host, port);
                    client.connect();
                    TcpClientHolder.set(client);
                    
                    // Navegar para a tela de login
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/org/alessipg/client/ui/LoginView.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) ipField.getScene().getWindow();
                    stage.setTitle("Login");
                    stage.setScene(new Scene(root));
                } catch (IOException ex) {
                    showError("Erro ao criar cliente: " + ex.getMessage());
                }
            } else {
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText(message);
            }
        });
    }

    @FXML
    private void onCancel() {
        if (connectionTask != null) {
            connectionTask.cancel();
        }
        connectButton.setDisable(false);
        cancelButton.setDisable(true);
    }

    private void showError(String text) {
        statusLabel.setTextFill(Color.RED);
        statusLabel.setText(text);
    }
}