# VoteFlix - Como Executar

## 🚀 Comandos Maven

### **Cliente JavaFX (Padrão)**
```bash
mvn clean compile exec:java
```
ou
```bash
mvn clean compile javafx:run
```

### **Servidor Swing**
```bash
mvn clean compile exec:java -Dexec.mainClass="org.alessipg.server.main.ServerMain"
```

### **Gerenciador de Usuários**
```bash
mvn clean compile exec:java -Dexec.mainClass="org.alessipg.client.ui.UserManager"
```

## 📋 Aplicações

| Comando | Aplicação | Tecnologia |
|---------|-----------|------------|
| `exec:java` | Cliente | JavaFX |
| `javafx:run` | Cliente | JavaFX |
| `-Dexec.mainClass="...ServerMain"` | Servidor | Swing |
| `-Dexec.mainClass="...UserManager"` | UserManager | Swing |

## 🔄 Fluxo Recomendado

1. **Servidor primeiro:**
   ```bash
   mvn clean compile exec:java -Dexec.mainClass="org.alessipg.server.main.ServerMain"
   ```

2. **Cliente depois:**
   ```bash
   mvn clean compile exec:java
   ```

