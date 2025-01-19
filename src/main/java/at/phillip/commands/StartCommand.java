package at.phillip.commands;

import at.phillip.interfaces.Command;
import at.phillip.sql.ServersSQL;
import at.phillip.states.ServerStates;
import at.phillip.utils.ProcessManager;
import at.phillip.utils.ServerManager;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StartCommand implements Command {

    private final ServersSQL serversSQL;
    public StartCommand(ServersSQL serversSQL){
        this.serversSQL = serversSQL;
    }

    @Override
    public void execute(String[] args) {
        if(args.length != 2){
            return;
        }

        String serverName = args[1];
        String serverSoftware = serversSQL.getServerSoftware(serverName).toLowerCase();
        String serverVersion = serversSQL.getServerVersion(serverName).toLowerCase();

        ProcessManager processManager = new ProcessManager();
        processManager.makeProcess(serverName, new File(""), 0, serverSoftware + "-" + serverVersion + ".jar");

        serversSQL.updateStates(serverName, ServerStates.STARTED);

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
    public List<String> getCompletions(String[] argIndex) {
        if (argIndex.length == 2) {
            if(serversSQL.getServers().isEmpty()){
                return Collections.emptyList();
            }
            return serversSQL.getServers();
        } else if (argIndex.length == 3) {
            return Arrays.asList("force");
        }
        return Collections.emptyList();
    }
}
