package at.phillip.commands;

import at.phillip.interfaces.Command;
import at.phillip.utils.ServerManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StartCommand implements Command {

    private final ServerManager serverManager;
    public StartCommand(ServerManager serverManager){
        this.serverManager = serverManager;
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Server wird gestartet...");
    }

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("starting", "s");
    }

    @Override
    public List<String> getCompletions(int argIndex) {
        if (argIndex == 1) {
            return serverManager.getServerNames();
        } else if (argIndex == 2) {
            return Arrays.asList("force");
        }
        return Collections.emptyList();
    }
}
