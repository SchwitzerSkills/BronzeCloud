package at.phillip.commands;

import at.phillip.interfaces.Command;

import java.util.Arrays;
import java.util.List;

public class StartCommand implements Command {
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
}
