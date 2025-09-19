package org.alessipg.server.ui;

import org.alessipg.server.infra.tcp.TcpServer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ServerWindow {
    private static JList<String> activeUsersList;
    private static DefaultListModel<String> listModel;
    private static JTextField portField;
    private static JLabel statusLabel;
    private static List<String> activeUsers = new ArrayList<>();

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

        // Add some sample users for demonstration
        addSampleUsers();
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
                tcpServer.start();
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
        panel.setBorder(BorderFactory.createTitledBorder("Active Users"));

        listModel = new DefaultListModel<>();
        activeUsersList = new JList<>(listModel);
        activeUsersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(activeUsersList);
        scrollPane.setPreferredSize(new Dimension(550, 300));

        panel.add(scrollPane, BorderLayout.CENTER);

        // Add user management buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addUserButton = new JButton("Add User");
        JButton removeUserButton = new JButton("Remove User");

        addUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userName = JOptionPane.showInputDialog("Enter username:");
                if (userName != null && !userName.trim().isEmpty()) {
                    listModel.addElement(userName);
                    statusLabel.setText("User '" + userName + "' added");
                }
            }
        });

        removeUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = activeUsersList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String removedUser = listModel.getElementAt(selectedIndex);
                    listModel.remove(selectedIndex);
                    statusLabel.setText("User '" + removedUser + "' removed");
                } else {
                    statusLabel.setText("Please select a user to remove");
                }
            }
        });

        buttonPanel.add(addUserButton);
        buttonPanel.add(removeUserButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

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

    private static void addSampleUsers() {
        listModel.addElement("user1");
        listModel.addElement("user2");
        listModel.addElement("admin");
    }
}

