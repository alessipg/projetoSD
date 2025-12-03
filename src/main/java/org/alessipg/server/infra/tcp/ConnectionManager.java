package org.alessipg.server.infra.tcp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe singleton manager for tracking client connections.
 * Maps client addresses (IP:port) to usernames.
 */
public class ConnectionManager {
    private static final ConnectionManager INSTANCE = new ConnectionManager();

    // Thread-safe map: clientAddress (IP:port) -> username
    private final ConcurrentHashMap<String, String> connections = new ConcurrentHashMap<>();

    private ConnectionManager() {}

    public static ConnectionManager getInstance() {
        return INSTANCE;
    }

    /**
     * Register a new connection with IP:port but no username yet
     */
    public void registerConnection(String clientAddress) {
        if (clientAddress == null || clientAddress.trim().isEmpty()) {
            System.err.println("[ConnectionManager] ERROR: Cannot register null or empty clientAddress");
            return;
        }

        System.out.println("[ConnectionManager] Registering connection: " + clientAddress);
        try {
            // ConcurrentHashMap doesn't accept null values, use empty string for "no username"
            connections.putIfAbsent(clientAddress, "");
            System.out.println("[ConnectionManager] Connection registered successfully: " + clientAddress);
            System.out.println("[ConnectionManager] Total connections: " + connections.size());
        } catch (Exception e) {
            System.err.println("[ConnectionManager] ERROR registering connection " + clientAddress + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Register username for a logged-in client
     */
    public void registerLogin(String clientAddress, String username) {
        if (clientAddress == null || clientAddress.trim().isEmpty()) {
            System.err.println("[ConnectionManager] ERROR: Cannot register login with null or empty clientAddress");
            return;
        }
        if (username == null || username.trim().isEmpty()) {
            System.err.println("[ConnectionManager] ERROR: Cannot register login with null or empty username");
            return;
        }

        System.out.println("[ConnectionManager] Registering login - Address: " + clientAddress + ", Username: " + username);
        try {
            connections.put(clientAddress, username);
            System.out.println("[ConnectionManager] User logged in successfully: " + username + " from " + clientAddress);
            System.out.println("[ConnectionManager] Total connections: " + connections.size());
        } catch (Exception e) {
            System.err.println("[ConnectionManager] ERROR registering login for " + clientAddress + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Reset username for a connection (logout without disconnecting)
     */
    public void resetUsername(String clientAddress) {
        if (clientAddress == null || clientAddress.trim().isEmpty()) {
            System.err.println("[ConnectionManager] ERROR: Cannot reset username for null or empty clientAddress");
            return;
        }

        System.out.println("[ConnectionManager] Resetting username for: " + clientAddress);
        try {
            String oldUsername = connections.get(clientAddress);
            connections.put(clientAddress, ""); // Reset to empty (not logged in)
            System.out.println("[ConnectionManager] Username reset for " + clientAddress + " (was: " + oldUsername + ")");
        } catch (Exception e) {
            System.err.println("[ConnectionManager] ERROR resetting username for " + clientAddress + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Unregister a connection (logout or disconnect)
     */
    public void unregisterConnection(String clientAddress) {
        if (clientAddress == null || clientAddress.trim().isEmpty()) {
            System.err.println("[ConnectionManager] ERROR: Cannot unregister null or empty clientAddress");
            return;
        }

        System.out.println("[ConnectionManager] Unregistering connection: " + clientAddress);
        try {
            String username = connections.remove(clientAddress);
            if (username != null && !username.isEmpty()) {
                System.out.println("[ConnectionManager] User disconnected: " + username + " from " + clientAddress);
            } else {
                System.out.println("[ConnectionManager] Connection closed: " + clientAddress + " (not logged in)");
            }
            System.out.println("[ConnectionManager] Total connections: " + connections.size());
        } catch (Exception e) {
            System.err.println("[ConnectionManager] ERROR unregistering connection " + clientAddress + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get username for a client address
     */
    public String getUsername(String clientAddress) {
        if (clientAddress == null || clientAddress.trim().isEmpty()) {
            return null;
        }
        String username = connections.get(clientAddress);
        // Return null if username is empty string (not logged in yet)
        return (username == null || username.isEmpty()) ? null : username;
    }

    /**
     * Get all active connections (thread-safe snapshot)
     */
    public Map<String, String> getAllConnections() {
        return Map.copyOf(connections);
    }

    /**
     * Find client address by username (for handling reconnections)
     */
    public String findClientAddressByUsername(String username) {
        return connections.entrySet().stream()
                .filter(entry -> username.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    /**
     * Clear all connections (for server shutdown)
     */
    public void clearAll() {
        connections.clear();
        System.out.println("All connections cleared");
    }
}

