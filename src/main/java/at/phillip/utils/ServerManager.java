package at.phillip.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerManager {

    private final List<String> serverNames = new ArrayList<>(Arrays.asList("server1", "server2", "server3"));

    public List<String> getServerNames() {
        return serverNames;
    }
}
