package at.phillip.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ProcessManager {

    public void makeProcess(String serverName, File softwarePath, int port, String softwareName){
        File workDirectory = new File("./static/Test-1");

        if(!workDirectory.exists()){
            if (workDirectory.mkdirs()) {
                System.out.println("Verzeichnis wurde erstellt: " + workDirectory.getAbsolutePath());
            } else {
                System.out.println("Fehler beim Erstellen des Verzeichnisses.");
            }
        }

        File softwareFile = new File(workDirectory, softwareName);
        if (!softwareFile.exists()) {
            try {
                Files.copy(Paths.get(softwarePath.getAbsolutePath()), Paths.get(softwareFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Software " + softwareName + " wurde kopiert.");
            } catch (IOException e) {
                System.out.println("Fehler beim Kopieren der Software: " + e.getMessage());
                return;
            }
        } else {
            System.out.println("Software-Datei existiert bereits: " + softwareName);
        }

        File eulaFile = new File(workDirectory, "eula.txt");
        if (!eulaFile.exists()) {
            try (FileWriter fileWriter = new FileWriter(eulaFile)) {
                fileWriter.write("eula=true");
                System.out.println("eula.txt wurde erstellt.");
            } catch (IOException e) {
                System.out.println("Fehler beim Erstellen von eula.txt: " + e.getMessage());
                return;
            }
        }

        File serverPropertiesFile = new File(workDirectory, "server.properties");
        if (!serverPropertiesFile.exists()) {
            try (FileWriter fileWriter = new FileWriter(serverPropertiesFile)) {
                fileWriter.write("server-port=" + port);
                System.out.println("server.properties wurde erstellt.");
            } catch (IOException e) {
                System.out.println("Fehler beim Erstellen von server.properties: " + e.getMessage());
                return;
            }
        }

        ProcessBuilder processBuilder = new ProcessBuilder(
                "screen", "-dmS", serverName, "java", "-jar", workDirectory.getAbsolutePath() + "/" + softwareName, "nogui");

        processBuilder.directory(workDirectory);
        try {
            Process process = processBuilder.start();

            System.out.println("Java-Prozess läuft in der 'screen'-Session: " + serverName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void destroyProcess(String serverName){
        ProcessBuilder processBuilder = new ProcessBuilder("screen", "-S", serverName, "-X", "quit");
        try {
            Process process = processBuilder.start();
            process.waitFor();
            System.out.println("Screen session " + serverName + " terminated.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
