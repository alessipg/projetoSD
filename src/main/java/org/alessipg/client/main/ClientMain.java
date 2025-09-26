package org.alessipg.client.main;

import javax.swing.*;
import static org.alessipg.client.ui.TcpConnectionDialog.initClient;

public class ClientMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initClient();
            }
        });
    }
}
