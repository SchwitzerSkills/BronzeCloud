package at.phillip.commands;

import at.phillip.interfaces.Command;

public class StartCommand implements Command {
    @Override
    public void execute(String[] args) {
        System.out.println("Server wird gestartet...");
    }

    @Override
    public String getName() {
        return "start";
    }
}
