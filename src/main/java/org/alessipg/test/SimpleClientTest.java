package org.alessipg.test;

import java.io.IOException;
import java.net.Socket;

public class SimpleClientTest {
    public static void main(String[] args) {
        String serverIp = "127.0.0.1";
        int serverPort = 8080; // Altere se necessário
        try {
            System.out.println("Conectando ao servidor " + serverIp + ":" + serverPort);
            Socket socket = new Socket(serverIp, serverPort);
            System.out.println("Conexão estabelecida!");
            while(true){}
        } catch (IOException e) {
            System.err.println("Erro ao conectar: " + e.getMessage());
        }
    }
}
