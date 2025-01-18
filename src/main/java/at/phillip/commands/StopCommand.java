package at.phillip.commands;

import at.phillip.interfaces.Command;

public class StopCommand implements Command {
    @Override
    public void execute(String[] args) {
        System.out.println("Server wird gestoppt...");
    }

    @Override
    public String getName() {
        return "stop";
    }
}
