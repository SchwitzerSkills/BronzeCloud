package at.phillip.utils;


import at.phillip.sql.ServersSQL;
import org.jline.terminal.Terminal;

import java.io.*;

public class ScreenManager {

    public void monitorLogs(Terminal terminal, String serverName, ServersSQL serversSQL) {
        File logFile;
        if(serversSQL.getServerSoftware(serverName).toLowerCase().equalsIgnoreCase("bungeecord")){
            logFile = new File("./static/" + serverName + "/proxy.log.0");
        } else {
            logFile = new File("./static/" + serverName + "/logs/latest.log");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            long filePointer = 0;
            reader.skip(filePointer);

            String line;
            while ((line = reader.readLine()) != null) {
                terminal.writer().println("\033[F");
                terminal.writer().println(line);
                filePointer = logFile.length();
            }

            if (filePointer == logFile.length()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Überwachen der Logdatei: " + e.getMessage());
        }
    }

    public void sendCommandToScreen(String sessionName, String command) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder("screen", "-S", sessionName, "-X", "stuff", command + "\n");
        builder.redirectErrorStream(true);
        Process process = builder.start();
        process.waitFor();
    }

}
