package at.phillip.commands;

import at.phillip.interfaces.Command;
import at.phillip.sql.ServersSQL;
import at.phillip.states.ServerStates;
import at.phillip.utils.ProcessManager;
import at.phillip.utils.ServerManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StopCommand implements Command {

    private final ServersSQL serversSQL;
    public StopCommand(ServersSQL serversSQL){
        this.serversSQL = serversSQL;
    }

    @Override
    public void execute(String[] args) {
        if(args.length != 2){
            return;
        }
        String serverName = args[1];

        if (!serversSQL.isServerExist(serverName)) {
            System.out.println("Server existiert nicht!");
            return;
        }

        ProcessManager processManager = new ProcessManager();

        processManager.destroyProcess(serverName);

        serversSQL.updateStates(serverName, ServerStates.STOPPED);

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
            if(serversSQL.getServers().isEmpty()){
                return Collections.emptyList();
            }
            return serversSQL.getServers();
        } else if (argIndex == 2) {
            return Arrays.asList("force", "noforce");
        }
        return Collections.emptyList();
    }
}
