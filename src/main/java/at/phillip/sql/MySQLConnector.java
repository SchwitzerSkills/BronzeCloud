package at.phillip.sql;

import org.jline.reader.LineReader;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class MySQLConnector {
    private String URL = "jdbc:mysql://localhost:3306";
    private String DATABASE;
    private String USER;
    private String PASSWORD;
    private final String CONFIG_FILE = "database/mysql.conf";

    public Connection connection;

    public void connect() throws SQLException, ClassNotFoundException {
        if (connection == null || connection.isClosed()) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Verbindung zur MySQL-Datenbank hergestellt.");
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Verbindung zur MySQL-Datenbank geschlossen.");
            } catch (SQLException e) {
                System.err.println("Fehler beim Schließen der Verbindung: " + e.getMessage());
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void loadConfig(LineReader reader) {
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                Properties properties = new Properties();
                properties.load(fis);

                USER = properties.getProperty("mysql.username");
                PASSWORD = properties.getProperty("mysql.password");
                DATABASE = properties.getProperty("mysql.database");

                URL += "/" + DATABASE;

                System.out.println("Konfiguration aus der Datei geladen.");
            } catch (IOException e) {
                System.err.println("Fehler beim Laden der Konfiguration: " + e.getMessage());
            }
        } else {
            System.out.println("Konfigurationsdatei existiert nicht. Benutzer wird nach den Daten gefragt.");
            promptForDatabaseDetails(reader);
        }
    }

    public void saveConfig() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            Properties properties = new Properties();
            properties.setProperty("mysql.username", USER);
            properties.setProperty("mysql.password", PASSWORD);
            properties.setProperty("mysql.database", DATABASE);

            properties.store(fos, "MySQL Database Configuration");

            System.out.println("Konfiguration gespeichert.");
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Konfiguration: " + e.getMessage());
        }
    }

    public void promptForDatabaseDetails(LineReader reader) {
        try {
            USER = reader.readLine("Bitte gib deinen MySQL-Benutzernamen ein: ").trim();
            PASSWORD = reader.readLine("Bitte gib dein MySQL-Passwort ein: ").trim();
            DATABASE = reader.readLine("Bitte gib den Namen der Datenbank ein: ").trim();

            URL += "/" + DATABASE;

            saveConfig();

            connect();

        } catch (UserInterruptException e) {
            System.out.println("Benutzer hat die Eingabe abgebrochen.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
