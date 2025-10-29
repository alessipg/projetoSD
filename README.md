# VoteFlix

This project was proposed in the Distributed Systems course at the Federal University of Technology – Paraná (UTFPR). It is a client-server application that allows users to submit and view movie reviews. Communication between client and server uses the TCP protocol.

## Requirements

- **Java 21** or higher
- **Maven 3.6+**

## Dependencies

The project uses the following main libraries (managed by Maven):
- JavaFX 21.0.8 (GUI)
- Hibernate 7.1.1 (ORM)
- SQLite JDBC
- Gson (JSON serialization)
- JWT (Authentication)

## How to Run

### 1. Setup Database

The application uses SQLite and will automatically create the database file on first run.

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run the Server

```bash
mvn exec:java -Dexec.mainClass="org.alessipg.server.main.ServerMain"
```

### 4. Run the Client

The client uses JavaFX and requires additional JVM arguments:

```bash
mvn exec:java -Dexec.mainClass="org.alessipg.client.main.ClientMain" -Dexec.args="--add-opens javafx.graphics/javafx.scene=ALL-UNNAMED"
```

Or using the JavaFX Maven plugin:

```bash
mvn javafx:run
```

### Alternative: Run from IDE

If running from IntelliJ IDEA or another IDE, add the following VM options to the client run configuration:

```
--add-opens javafx.graphics/javafx.scene=ALL-UNNAMED
```

## Project Structure

- **Server**: Handles business logic, database operations, and TCP communication
- **Client**: JavaFX-based GUI application that connects to the server via TCP
- **Shared**: Common DTOs and domain models used by both client and server

