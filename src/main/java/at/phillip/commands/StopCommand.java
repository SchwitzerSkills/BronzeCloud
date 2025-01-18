package at.phillip.commands;

import at.phillip.interfaces.Command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StopCommand implements Command {

    @Override
    public void execute(String[] args) {
        System.out.println("Server wird gestoppt...");
    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("stopping", "st");
    }

    @Override
    public List<String> getCompletions(int argIndex) {
        if (argIndex == 1) {
            return Arrays.asList("server14", "server25", "server36");
        } else if (argIndex == 2) {
            return Arrays.asList("force", "noforce");
        }
        return Collections.emptyList();
    }
}
