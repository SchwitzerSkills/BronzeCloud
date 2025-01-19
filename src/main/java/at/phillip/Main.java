package at.phillip;

import at.phillip.console.ConsoleManager;
import at.phillip.sql.SQLiteConnector;
import at.phillip.utils.SetupManager;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            ConsoleManager consoleManager = new ConsoleManager();
            SQLiteConnector connector = new SQLiteConnector();

            connector.loadConfig();
            connector.connect();

            new SetupManager().createFolders();
            consoleManager.startConsole(connector);

        } catch (IOException e) {
            System.out.println("Fehler beim Laden der Konfiguration: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}