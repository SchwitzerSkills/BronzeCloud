package at.phillip.commands;

import at.phillip.interfaces.Command;
import at.phillip.utils.ServerManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CreateCommand implements Command {

    private final ServerManager serverManager;
    public CreateCommand(ServerManager serverManager){
        this.serverManager = serverManager;
    }

    @Override
    public void execute(String[] args) {
        if(args.length == 2) {
            String serverName = args[1];

            if (serverManager.getServerNames().contains(serverName)) {
                System.out.println("Servername bereits vergeben: " + serverName);
            } else {
                serverManager.getServerNames().add(serverName);
                System.out.println("Server '" + serverName + "' wurde erstellt.");
            }
        } else {
            System.out.println("Zu viele oder zu wenige Argumente.");
        }
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("creating", "cr");
    }

    @Override
    public List<String> getCompletions(int argIndex) {
        if (argIndex == 1) {
            return serverManager.getServerNames(); // Return available server names for completion
        }
        return Collections.emptyList();
    }
}
