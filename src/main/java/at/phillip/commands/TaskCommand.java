package at.phillip.commands;

import at.phillip.interfaces.Command;
import at.phillip.sql.ServersSQL;
import at.phillip.states.ServerStates;
import at.phillip.utils.DownloadManager;
import at.phillip.utils.ProcessManager;
import at.phillip.utils.ScreenManager;
import org.jline.terminal.Terminal;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TaskCommand implements Command {

    private final DownloadManager downloadManager = new DownloadManager();
    private final ServersSQL serversSQL;
    private final ScreenManager screenManager;
    private final Terminal terminal;
    public TaskCommand(ServersSQL serversSQL, ScreenManager screenManager, Terminal terminal){
        this.serversSQL = serversSQL;
        this.screenManager = screenManager;
        this.terminal = terminal;
    }

    @Override
    public void execute(String[] args) {
        if(args[1].equalsIgnoreCase("setup")) {
            if (args.length != 6) {
                System.out.println("Verwendung: task setup <SERVER_NAME> <SOFTWARE> <VERSION> <COUNT>");
                return;
            }

            if (args[3].equalsIgnoreCase("PAPER") && args[4].equalsIgnoreCase("latest")) {
                if(!new File("./software/paper-latest.jar").exists()) {
                    System.out.println("download paper latest...");
                    try {
                        downloadManager.downloadFile("https://api.papermc.io/v2/projects/paper/versions/1.21.4/builds/118/downloads/paper-1.21.4-118.jar", "./software", "paper-latest.jar");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("Software bereits heruntergeladen!");
                }

                if(serversSQL.isServerNameExist(args[2])){
                    System.out.println("Dieser Name existiert bereits!");
                    return;
                }

                if(!args[5].equalsIgnoreCase("1")){

                    for(int i = 1; i < Integer.parseInt(args[5]) + 1; i++) {
                        int port = findAvailablePort();

                        serversSQL.insertServer(args[2] + "-" + i, port, "paper", "latest", ServerStates.STARTED);

                        System.out.println("Server erstellt!");

                        ProcessManager processManager = new ProcessManager();
                        processManager.makeProcess(args[2] + "-" + i, new File("./software/paper-latest.jar"), port, "paper-latest.jar");
                    }

                    return;
                }

                int port = findAvailablePort();

                serversSQL.insertServer(args[2] + "-1", port, "paper", "latest", ServerStates.STARTED);

                System.out.println("Server erstellt!");

                ProcessManager processManager = new ProcessManager();
                processManager.makeProcess(args[2] + "-1", new File("./software/paper-latest.jar"), port, "paper-latest.jar");

            } else if (args[3].equalsIgnoreCase("BUNGEECORD") && args[4].equalsIgnoreCase("latest")) {
                if(!new File("./software/bungeecord-latest.jar").exists()) {
                    System.out.println("download bungeecord latest...");
                    try {
                        downloadManager.downloadFile("https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.jar", "./software", "bungeecord-latest.jar");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("Software bereits heruntergeladen!");
                }

                if(serversSQL.isProxyExist()){
                    System.out.println("Leider hast du schon ein Proxy!");
                    return;
                }

                if(serversSQL.isServerNameExist(args[2])){
                    System.out.println("Dieser Name existiert bereits!");
                    return;
                }

                serversSQL.insertServer(args[2], 25565, "bungeecord", "latest", ServerStates.STARTED);

                System.out.println("Server erstellt!");

                ProcessManager processManager = new ProcessManager();
                processManager.makeProcess(args[2], new File("./software/bungeecord-latest.jar"), 25565, "bungeecord-latest.jar");
            }
        }
        if(args[1].equalsIgnoreCase("log")) {
            if (args.length != 3) {
                System.out.println("Verwendung: task log <SERVER_NAME>");
                return;
            }

            new Thread(() -> screenManager.monitorLogs(terminal, args[2])).start();

        }

        if(args[1].equalsIgnoreCase("command")) {
            if (args.length < 4) {
                System.out.println("Verwendung: task command <SERVER_NAME> <COMMAND>");
                return;
            }
            String command = "";
            for(int i = 3; i < args.length; i++){
                command += " " + args[i];
            }

            try {
                screenManager.sendCommandToScreen(args[2], command.trim());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String getName() {
        return "task";
    }

    @Override
    public List<String> getAliases() {
        return List.of();
    }

    @Override
    public List<String> getCompletions(String[] argIndex) {
        if(argIndex.length == 2){
            return Arrays.asList("setup", "start", "stop", "log", "command");
        }
        if(argIndex[1].equalsIgnoreCase("setup")) {
            if (argIndex.length == 4) {
                return Arrays.asList("PAPER", "PURPUR", "BUNGEECORD");
            } else if (argIndex.length == 5) {
                return Arrays.asList("latest");
            } else if (argIndex.length == 6) {
                return Arrays.asList("1");
            }
        } else if (argIndex[1].equalsIgnoreCase("start")) {
            if(argIndex.length == 3){
                return Arrays.asList("test");
            }
        } else if (argIndex[1].equalsIgnoreCase("stop")) {
            if(argIndex.length == 3){
                return Arrays.asList("test");
            }
        } else if (argIndex[1].equalsIgnoreCase("log")) {
            if(argIndex.length == 3){
                if(serversSQL.getServers().isEmpty()) {
                    return Collections.emptyList();
                }
                return serversSQL.getServers();
            }
        } else if (argIndex[1].equalsIgnoreCase("command")) {
            if(argIndex.length == 3){
                if(serversSQL.getServers().isEmpty()) {
                    return Collections.emptyList();
                }
                return serversSQL.getServers();
            } else if(argIndex.length > 4){
                return Collections.emptyList();
            }
        }
        return List.of();
    }

    private final int MIN_PORT = 1024;
    private final int MAX_PORT = 65535;

    public int generateRandomPort() {
        Random random = new Random();
        return random.nextInt((MAX_PORT - MIN_PORT) + 1) + MIN_PORT;
    }

    private int findAvailablePort() {
        int port;
        do {
            port = generateRandomPort();
        } while (serversSQL.isPortUsed(port));
        return port;
    }

}
