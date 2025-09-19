package org.alessipg.client.main;

import javax.swing.*;
import org.alessipg.client.ui.TcpConnectionDialog;

import static org.alessipg.client.ui.TcpConnectionDialog.initClient;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initClient();
            }
        });
    }
}
