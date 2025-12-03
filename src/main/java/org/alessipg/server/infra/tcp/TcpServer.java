
package org.alessipg.server.infra.tcp;

import org.alessipg.server.ui.ServerView;

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
      System.out.println("[TcpServer] Server started on port " + port);

      while (running) {
        System.out.println("[TcpServer] Waiting for client connection...");
        Socket clientSocket = serverSocket.accept();
        System.out.println("[TcpServer] Client accepted: " + clientSocket.getRemoteSocketAddress());

        // Get client address for listener notification
        String clientIp = clientSocket.getInetAddress().getHostAddress();
        int clientPort = clientSocket.getPort();
        String clientAddress = clientIp + ":" + clientPort;

        System.out.println("[TcpServer] Client details - IP: " + clientIp + ", Port: " + clientPort + ", Full address: " + clientAddress);

        // Notifica a interface sobre a nova conexão
        if (connectionListener != null) {
          System.out.println("[TcpServer] Notifying connection listener...");
          connectionListener.onClientConnected(clientIp, clientPort);
        } else {
          System.out.println("[TcpServer] No connection listener registered");
        }

        // Spawn handler in separate thread (handler will register the connection)
        System.out.println("[TcpServer] Creating ClientHandler for: " + clientAddress);
        ClientHandler handler = new ClientHandler(clientSocket);

        System.out.println("[TcpServer] Submitting handler to thread pool...");
        threadPool.submit(handler);
        System.out.println("[TcpServer] Handler submitted successfully for: " + clientAddress);
      }

    } catch (IOException e) {
      System.err.println("[TcpServer] Error in server: " + e.getMessage());
      e.printStackTrace();
    }
    System.out.println("[TcpServer] Server stopped (exited main loop)");
  }

  public void stop() {
    running = false;
    threadPool.shutdownNow();
    ConnectionManager.getInstance().clearAll();
    ServerView.clearAllConnections();
    System.out.println("Server stopped.");
  }
}
