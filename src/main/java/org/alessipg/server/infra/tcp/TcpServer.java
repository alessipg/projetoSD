
package org.alessipg.server.infra.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TcpServer {
  public static interface ClientConnectionListener {
    void onClientConnected(String clientAddress, int clientPort);
  }

  private ClientConnectionListener connectionListener;

  public void setClientConnectionListener(ClientConnectionListener listener) {
    this.connectionListener = listener;
  }

  private final int port;
  private boolean running = false;
  private final ExecutorService threadPool;

  public TcpServer(int port) {
    this.port = port;
    this.threadPool = Executors.newCachedThreadPool(); // grows/shrinks dynamically
  }

  public void start() {
    running = true;
    try (ServerSocket serverSocket = new ServerSocket(port)) {
      System.out.println("Server started on port " + port);

      while (running) {
        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connected: " + clientSocket.getRemoteSocketAddress());

        // Notifica a interface sobre a nova conexão
          if (connectionListener != null) {
          String clientIp = clientSocket.getInetAddress().getHostAddress();
          int clientPort = clientSocket.getPort();
          connectionListener.onClientConnected(clientIp, clientPort);
        }

        // Spawn handler in separate thread
        ClientHandler handler = new ClientHandler(clientSocket);
        threadPool.submit(handler);
      }

    } catch (IOException e) {
      System.err.println("Error in server: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public void stop() {
    running = false;
    threadPool.shutdownNow();
    System.out.println("Server stopped.");
  }
}
