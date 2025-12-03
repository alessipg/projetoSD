package org.alessipg.server.ui;

import org.alessipg.server.infra.tcp.ConnectionManager;
import org.alessipg.server.infra.tcp.TcpServer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ServerView {
    private static JList<String> activeConnectionsList;
    private static DefaultListModel<String> listModel;
    private static JTextField portField;
    private static JLabel statusLabel;

    public static void createAndShowGUI() {
        JFrame jFrame = new JFrame("Server Management Interface");
        jFrame.setLayout(new BorderLayout());
        jFrame.setSize(600, 500);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create main panels
        JPanel topPanel = createTopPanel();
        JPanel centerPanel = createCenterPanel();
        JPanel bottomPanel = createBottomPanel();

        jFrame.add(topPanel, BorderLayout.NORTH);
        jFrame.add(centerPanel, BorderLayout.CENTER);
        jFrame.add(bottomPanel, BorderLayout.SOUTH);

        jFrame.setVisible(true);

    }

    private static JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Server Configuration"));

        JLabel portLabel = new JLabel("Port:");
        portField = new JTextField("8080", 10);
        JButton startButton = new JButton("Start Server");
        JButton stopButton = new JButton("Stop Server");

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String port = portField.getText();
                statusLabel.setText("Server started on port " + port);
                TcpServer tcpServer = new TcpServer(Integer.parseInt(port));

                // Listener para novas conexões
                tcpServer.setClientConnectionListener((clientAddress, clientPort) -> {
                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText("Cliente conectado: " + clientAddress + ":" + clientPort);
                    });
                });

                // Inicia o servidor em uma thread separada
                Thread serverThread = new Thread(() -> tcpServer.start());
                serverThread.setDaemon(true);
                serverThread.start();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusLabel.setText("Server stopped");
            }
        });

        panel.add(portLabel);
        panel.add(portField);
        panel.add(startButton);
        panel.add(stopButton);

        return panel;
    }

    private static JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Active Connections"));

        listModel = new DefaultListModel<>();
        activeConnectionsList = new JList<>(listModel);
        activeConnectionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(activeConnectionsList);
        scrollPane.setPreferredSize(new Dimension(550, 300));

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private static JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Status"));

        statusLabel = new JLabel("Server ready");
        statusLabel.setForeground(Color.BLUE);

        panel.add(statusLabel);

        return panel;
    }

    /**
     * Add a new connection to the list (initially without username)
     */
    public static void addConnection(String clientAddress) {
        System.out.println("[ServerView] addConnection called for: " + clientAddress);
        if (listModel == null) {
            System.err.println("[ServerView] ERROR: listModel is null! UI not initialized?");
            return;
        }
        SwingUtilities.invokeLater(() -> {
            try {
                String displayText = formatConnectionDisplay(clientAddress, null);
                System.out.println("[ServerView] Adding to list: " + displayText);
                listModel.addElement(displayText);
                System.out.println("[ServerView] Successfully added to list. Total connections: " + listModel.getSize());
            } catch (Exception e) {
                System.err.println("[ServerView] ERROR adding connection: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Update connection with username after login
     */
    public static void updateConnection(String clientAddress, String username) {
        System.out.println("[ServerView] updateConnection called for: " + clientAddress + " with username: " + username);
        if (listModel == null) {
            System.err.println("[ServerView] ERROR: listModel is null! UI not initialized?");
            return;
        }
        SwingUtilities.invokeLater(() -> {
            try {
                // Find and remove old entry for this client address
                for (int i = 0; i < listModel.getSize(); i++) {
                    String element = listModel.getElementAt(i);
                    if (element.startsWith(clientAddress)) {
                        System.out.println("[ServerView] Removing old entry: " + element);
                        listModel.remove(i);
                        break;
                    }
                }
                // Add updated entry
                String displayText = formatConnectionDisplay(clientAddress, username);
                System.out.println("[ServerView] Adding updated entry: " + displayText);
                listModel.addElement(displayText);
                System.out.println("[ServerView] Successfully updated. Total connections: " + listModel.getSize());
            } catch (Exception e) {
                System.err.println("[ServerView] ERROR updating connection: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Remove a connection from the list
     */
    public static void removeConnection(String clientAddress) {
        System.out.println("[ServerView] removeConnection called for: " + clientAddress);
        if (listModel == null) {
            System.err.println("[ServerView] ERROR: listModel is null! UI not initialized?");
            return;
        }
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("[ServerView] Removing connection: " + clientAddress);
                for (int i = 0; i < listModel.getSize(); i++) {
                    String element = listModel.getElementAt(i);
                    if (element.startsWith(clientAddress)) {
                        System.out.println("[ServerView] Found and removing: " + element);
                        listModel.remove(i);
                        System.out.println("[ServerView] Successfully removed. Total connections: " + listModel.getSize());
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println("[ServerView] ERROR removing connection: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Format connection display as "IP:port - username" or "IP:port - Not logged in"
     */
    private static String formatConnectionDisplay(String clientAddress, String username) {
        if (username != null && !username.isEmpty()) {
            return clientAddress + " - " + username;
        } else {
            return clientAddress + " - Not logged in";
        }
    }

    /**
     * Clear all connections (for server shutdown)
     */
    public static void clearAllConnections() {
        SwingUtilities.invokeLater(() -> {
            listModel.clear();
        });
    }

}
