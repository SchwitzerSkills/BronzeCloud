package at.phillip.commands;

import at.phillip.interfaces.Command;
import at.phillip.sql.PermsSQL;
import at.phillip.sql.PlayersSQL;
import at.phillip.sql.ServersSQL;
import at.phillip.utils.RedisManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PermsCommand implements Command {

    private final PermsSQL permsSQL;
    private final PlayersSQL playersSQL;
    private final RedisManager redisManager;
    private final ServersSQL serversSQL;

    public PermsCommand(ServersSQL serversSQL, RedisManager redisManager, PermsSQL permsSQL, PlayersSQL playersSQL){
        this.serversSQL = serversSQL;
        this.redisManager = redisManager;
        this.permsSQL = permsSQL;
        this.playersSQL = playersSQL;
    }

    @Override
    public void execute(String[] args) {
        if(args.length != 6){
            return;
        }

        if(args[1].equalsIgnoreCase("user") || args[3].equalsIgnoreCase("permission")){
            if(args[4].equalsIgnoreCase("set")){
                String perms = args[5];
                String playerName = args[2];
                int player_id = playersSQL.getPlayerId(playerName);

                permsSQL.insertPerms(player_id, perms);

                if (playersSQL.isPlayerOnline(playerName)) {
                    redisManager.sendMessageToRedis("channel2", new String[]{"perms", "all", "players_permissions", perms});
                }
                System.out.println("perms set");

            } else if(args[4].equalsIgnoreCase("unset")){
                String perms = args[5];
                String playerName = args[2];
                int player_id = playersSQL.getPlayerId(playerName);

                permsSQL.deletePerms(player_id, perms);

                if (playersSQL.isPlayerOnline(playerName)) {
                    redisManager.sendMessageToRedis("channel2", new String[]{"perms", "all", "unset_players_permissions", perms});
                }
                System.out.println("perms unset");
            }
        }
    }

    @Override
    public String getName() {
        return "perms";
    }

    @Override
    public List<String> getAliases() {
        return List.of();
    }

    @Override
    public List<String> getCompletions(String[] argIndex) {
        if(argIndex.length == 2){
            return Arrays.asList("user");
        }
        if(argIndex[1].equalsIgnoreCase("user")) {
            if(argIndex.length == 3){
                if(playersSQL.getPlayerNames().isEmpty()){
                    return Collections.emptyList();
                }

                return playersSQL.getPlayerNames();
            }
            if (argIndex.length == 4) {
                return Arrays.asList("permission");
            }
            if (argIndex.length == 5) {
                return Arrays.asList("set", "unset");
            }
        }
        return List.of();
    }
}
