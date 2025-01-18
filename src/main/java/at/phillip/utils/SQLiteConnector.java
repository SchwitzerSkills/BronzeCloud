package at.phillip.utils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnector {

    private Connection connection;
    private final String dbPath = "database/database.sqlite";

    public void loadConfig() throws IOException {
        File dbFile = new File(dbPath);
        if (!dbFile.exists()) {
            dbFile.getParentFile().mkdirs();
            dbFile.createNewFile();
            System.out.println("SQLite-Datenbank wurde erstellt: " + dbPath);
        }
    }

    public Connection connect() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + new File("database/database.sqlite").getAbsoluteFile());
            System.out.println("Verbindung zur SQLite-Datenbank hergestellt.");
        } catch (SQLException e) {
            System.out.println("Fehler bei der Verbindung zur SQLite-Datenbank: " + e.getMessage());
        }
        return connection;
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Verbindung zur SQLite-Datenbank geschlossen.");
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Schließen der SQLite-Verbindung: " + e.getMessage());
        }
    }
}
