package at.phillip.commands;

import at.phillip.interfaces.Command;
import at.phillip.sql.ServersSQL;
import at.phillip.states.ServerStates;
import at.phillip.utils.DownloadManager;
import at.phillip.utils.ProcessManager;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TaskCommand implements Command {

    private final DownloadManager downloadManager = new DownloadManager();
    private final ServersSQL serversSQL;
    public TaskCommand(ServersSQL serversSQL){
        this.serversSQL = serversSQL;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 5) {
            System.out.println("Verwendung: task setup <SERVER_NAME> <SOFTWARE> <VERSION>");
            return;
        }
        if (args[1].equalsIgnoreCase("setup")) {
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

                serversSQL.insertServer(args[2], 25599, "paper", "latest", ServerStates.STARTED);

                System.out.println("Server erstellt!");

                ProcessManager processManager = new ProcessManager();
                processManager.makeProcess(args[2], new File("./software/paper-latest.jar"), 25599, "paper-latest.jar");

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
    public List<String> getCompletions(int argIndex) {
        if(argIndex == 1){
            return Arrays.asList("setup");
        } else if(argIndex == 3){
            return Arrays.asList("PAPER", "PURPUR", "BUNGEECORD");
        } else if(argIndex == 4){
            return Arrays.asList("latest");
        }
        return List.of();
    }
}
