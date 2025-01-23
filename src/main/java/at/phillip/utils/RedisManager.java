package at.phillip.utils;

import at.phillip.sql.ServersSQL;
import at.phillip.states.ServerStates;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.io.File;

public class RedisManager {
    public Jedis jedis;
    public Thread redisThread;

    private ServersSQL serversSQL;
    public RedisManager(ServersSQL serversSQL){
        jedis = new Jedis("localhost", 6379);
        redisThread = new Thread(() -> listenToRedis(jedis));
        redisThread.start();
        this.serversSQL = serversSQL;
    }

    public void sendMessageToRedis(String channel, String[] message) {
        String messagePart = String.join(" ", message);

        new Thread(() -> {
            try (Jedis jedis = new Jedis("localhost", 6379)) {
                jedis.publish(channel, messagePart);
                System.out.println("Nachricht an Redis gesendet: " + messagePart);
            }
        }).start();
    }

    public void listenToRedis(Jedis jedis) {
        try {
            jedis.subscribe(new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    if ("channel1".equals(channel)) {
                        String[] messagePart = message.split(" ");

                        if (messagePart[0].equalsIgnoreCase("stopping")) {
                            serversSQL.updateStatesFromPort(Integer.parseInt(messagePart[1]), ServerStates.STOPPING);
                        } else if (messagePart[0].equalsIgnoreCase("stopped")) {
                            serversSQL.updateStatesFromPort(Integer.parseInt(messagePart[1]), ServerStates.STOPPED);
                            ProcessManager processManager = new ProcessManager();
                            processManager.destroyProcess(serversSQL.getServerName(Integer.parseInt(messagePart[1])));
                            if(!serversSQL.isServerBungee(Integer.parseInt(messagePart[1]))){
                                int bungee_port = serversSQL.getPortFromProxy();

                                sendMessageToRedis("channel2", new String[]{"server", bungee_port + "", Integer.parseInt(messagePart[1]) + "", "stopped"});

                                String serverName = serversSQL.getServerName(Integer.parseInt(messagePart[1]));
                                String serverSoftware = serversSQL.getServerSoftware(serverName).toLowerCase();
                                String serverVersion = serversSQL.getServerVersion(serverName).toLowerCase();
                                processManager.makeProcess(serverName, new File(""), 0, serverSoftware + "-" + serverVersion + ".jar");
                                serversSQL.updateStates(serverName, ServerStates.STARTING);
                                sendMessageToRedis("channel2", new String[]{"server", bungee_port + "", Integer.parseInt(messagePart[1]) + "", "starting"});
                            }
                        } else if (messagePart[0].equalsIgnoreCase("started")) {
                            serversSQL.updateStatesFromPort(Integer.parseInt(messagePart[1]), ServerStates.STARTED);
                            if(!serversSQL.isServerBungee(Integer.parseInt(messagePart[1]))){
                                int bungee_port = serversSQL.getPortFromProxy();

                                sendMessageToRedis("channel2", new String[]{"server", bungee_port + "", Integer.parseInt(messagePart[1]) + "", "started"});
                            }
                        }
                    }
                }
            }, "channel1");
        } catch (Exception e) {
        }
    }


    public void closeRedisConnection() {
        if (jedis != null) {
            try {
                System.out.println("Schließe Redis-Verbindung...");
                jedis.close();
            } catch (Exception e) {
            }
        }
    }
}
