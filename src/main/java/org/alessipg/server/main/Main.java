package org.alessipg.server.main;

import javax.swing.*;

import static org.alessipg.server.ui.ServerWindow.createAndShowGUI;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}