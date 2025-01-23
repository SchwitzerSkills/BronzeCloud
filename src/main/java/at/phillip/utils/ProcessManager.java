package at.phillip.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class ProcessManager {

    public void makeProcess(String serverName, File softwarePath, int port, String softwareName){
        File workDirectory = new File("./static/" + serverName);

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

        if(!softwareName.contains("bungeecord")) {
            createConfig(workDirectory, "eula=true", "eula.txt");
            try {
                if (!Files.exists(Paths.get("./software/server.properties"))) {
                    new SetupManager().makeConfigYML();
                }
                if (!Files.exists(Paths.get(workDirectory.getAbsolutePath() + "/server.properties"))) {
                    Files.copy(Paths.get("./software/server.properties"), Paths.get(workDirectory.getAbsolutePath() + "/server.properties"), StandardCopyOption.REPLACE_EXISTING);
                    replaceFile(workDirectory.getAbsolutePath() + "/server.properties", port, "25565",port + "", "server-port=");
                }
            } catch (Exception e){
            }
            try {
                if (!Files.exists(Paths.get("./software/spigot.yml"))) {
                    new SetupManager().makeConfigYML();
                }
                if (!Files.exists(Paths.get(workDirectory.getAbsolutePath() + "/spigot.yml"))) {
                    new SetupManager().createServerDefaultTemplate(workDirectory);
                    Files.copy(Paths.get("./database/mysql.conf"), Paths.get(workDirectory.getAbsolutePath() + "/plugins/SpigotBronzeModule/mysql.conf"), StandardCopyOption.REPLACE_EXISTING);
                    Files.copy(Paths.get("./software/spigot.yml"), Paths.get(workDirectory.getAbsolutePath() + "/spigot.yml"), StandardCopyOption.REPLACE_EXISTING);
                    Files.copy(Paths.get("./modules/SpigotBronzeModule.jar"), Paths.get(workDirectory.getAbsolutePath() + "/plugins/SpigotBronzeModule.jar"), StandardCopyOption.REPLACE_EXISTING);

                }
            } catch (Exception e){
            }
        } else {
            try {
                if(!Files.exists(Paths.get("./software/config.yml"))){
                    new SetupManager().makeConfigYML();
                }
                if(!Files.exists(Paths.get(workDirectory.getAbsolutePath() + "/config.yml"))) {
                    Files.copy(Paths.get("./software/config.yml"), Paths.get(workDirectory.getAbsolutePath() + "/config.yml"), StandardCopyOption.REPLACE_EXISTING);
                    replaceFile(workDirectory.getAbsolutePath() + "/config.yml", port, "0.0.0.0:25577", "0.0.0.0:" + port,"  host:");
                    new SetupManager().createProxyDefaultTemplate(workDirectory);
                    Files.copy(Paths.get("./database/mysql.conf"), Paths.get(workDirectory.getAbsolutePath() + "/plugins/BungeeBronzeModule/mysql.conf"), StandardCopyOption.REPLACE_EXISTING);
                    Files.copy(Paths.get("./modules/BungeeBronzeModule.jar"), Paths.get(workDirectory.getAbsolutePath() + "/plugins/BungeBronzeModule.jar"), StandardCopyOption.REPLACE_EXISTING);
                }


            } catch (IOException e) {
                throw new RuntimeException(e);
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
        ProcessBuilder processBuilder = new ProcessBuilder("screen", "-X", "-S", serverName, "quit");

        try {
            Process process = processBuilder.start();
            process.waitFor();
            System.out.println("Screen session " + serverName + " terminated.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void createConfig(File workDirectory, String write, String fileName){
        File serverPropertiesFile = new File(workDirectory, fileName);
        if (!serverPropertiesFile.exists()) {
            try (FileWriter fileWriter = new FileWriter(serverPropertiesFile)) {
                fileWriter.write(write);
                System.out.println(fileName + " wurde erstellt.");
            } catch (IOException e) {
                System.out.println("Fehler beim Erstellen: " + e.getMessage());
                return;
            }
        }
    }

    public void replaceFile(String filePath, int port, String oldString, String newString, String startWith){
        String newHost = newString;
        String oldHost = oldString;
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);

                if (line.startsWith(startWith) && line.contains(oldHost)) {
                    line = line.replace(oldHost, newHost);
                    lines.set(i, line);
                    System.out.println("Ersetze Host: " + oldHost + " -> " + newHost);
                }
            }

            Files.write(Paths.get(filePath), lines);
            System.out.println("Host erfolgreich in der config.yml ersetzt!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
