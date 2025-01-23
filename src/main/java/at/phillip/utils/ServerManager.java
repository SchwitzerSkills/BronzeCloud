package at.phillip.utils;

import at.phillip.sql.ServersSQL;
import at.phillip.states.ServerStates;

import java.io.File;
import java.util.List;

public class ServerManager {

    private final ServersSQL serversSQL;
    public ServerManager(ServersSQL serversSQL) {
        this.serversSQL = serversSQL;
    }

    public void stopAllServers(){
        ProcessManager processManager = new ProcessManager();
        List<String> servers = serversSQL.getServers();

        servers.forEach(server -> {
            if(serversSQL.getServerState(server) == ServerStates.STARTING || serversSQL.getServerState(server) == ServerStates.STARTED || serversSQL.getServerState(server) == ServerStates.STOPPING) {
                processManager.destroyProcess(server);
                serversSQL.updateStates(server, ServerStates.STOPPED);
                System.out.println(server + ": stopped");
            }
        });
    }

    public void startAllServers(){
        ProcessManager processManager = new ProcessManager();
        List<String> servers = serversSQL.getServers();

        servers.forEach(server -> {
            if(serversSQL.getServerState(server) == ServerStates.STOPPED) {
                String serverSoftware = serversSQL.getServerSoftware(server).toLowerCase();
                String serverVersion = serversSQL.getServerVersion(server).toLowerCase();
                processManager.makeProcess(server, new File(""), 0, serverSoftware + "-" + serverVersion + ".jar");
                serversSQL.updateStates(server, ServerStates.STARTING);
                System.out.println(server + ": start");
            }
        });
    }

}
