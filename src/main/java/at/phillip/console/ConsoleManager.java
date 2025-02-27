package at.phillip.console;

import at.phillip.commands.*;
import at.phillip.interfaces.Command;
import at.phillip.sql.*;
import at.phillip.utils.*;
import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConsoleManager {

    private final Map<String, Command> commands = new HashMap<>();
    private ServerManager serverManager;
    private MySQLConnector connector = new MySQLConnector();
    private ServersSQL serversSQL;
    private PermsSQL permsSQL;
    private PlayersSQL playersSQL;
    private final ScreenManager screenManager = new ScreenManager();
    private Terminal terminal;
    private RedisManager redisManager;

    public LineReader reader;


    private void registerSQL(){
        serversSQL = new ServersSQL(connector);
        serversSQL.createServerTable();

        permsSQL = new PermsSQL(connector);
        permsSQL.createServerTable();

        playersSQL = new PlayersSQL(connector);
        playersSQL.createServerTable();
    }

    private void registerCommands() {
        addCommand(new TaskCommand(serversSQL, screenManager, terminal, redisManager));
        addCommand(new PermsCommand(serversSQL, redisManager, permsSQL, playersSQL));
//        addCommand(new ListCommand());
//        addCommand(new ReloadCommand());
    }

    public void addCommand(Command command) {
        commands.put(command.getName(), command);
        for (String alias : command.getAliases()) {
            commands.put(alias, command);
        }
    }

    public void startConsole() throws IOException, SQLException, ClassNotFoundException {

        terminal = TerminalBuilder.builder().system(true).build();

        Completer completer = getCompletion();

        reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(completer)
                .parser(new DefaultParser())
                .build();

        connector.loadConfig(reader);
        connector.connect();

        registerSQL();
        redisManager = new RedisManager(serversSQL);
        registerCommands();
        serverManager = new ServerManager(serversSQL);
        serverManager.startAllServers();

        String prompt = ConsoleColors.GREEN + "root@BronzeCloud" + ConsoleColors.BLUE + " : " + ConsoleColors.RESET;



        while (true) {
            try {
                String line = reader.readLine(prompt).trim();
                handleCommand(line);
            } catch (UserInterruptException | EndOfFileException e) {
                serverManager.stopAllServers();
                if (connector != null) {
                    connector.disconnect();
                }
                if(redisManager.jedis != null){
                    redisManager.closeRedisConnection();
                }

                break;
            }
        }


    }

    private void handleCommand(String input) {
        if (input.isEmpty()) return;

        String[] args = input.split(" ");
        Command command = commands.get(args[0]);

        if (command != null) {
            command.execute(args);
        } else {
            System.out.println(ConsoleColors.RED + "Unbekannter Befehl: " + args[0] + ConsoleColors.RESET);
        }

    }

    public Completer getCompletion() {
        return (reader, line, candidates) -> {
            if (line.wordIndex() == 0) {
                String prefix = line.line().trim();
                for (String command : commands.keySet()) {
                    if (command.startsWith(prefix)) {
                        candidates.add(new Candidate(command));
                    }
                }
            } else {
                String commandName = line.words().getFirst().trim();
                Command command = commands.get(commandName);

                if (command != null) {
                    String[] argIndex = line.words().toArray(new String[0]);
                    List<String> completions = command.getCompletions(argIndex);

                    if(completions != null){
                        String currentArgument = line.words().get(line.wordIndex());
                        for (String completion : completions) {
                            if(completion.startsWith(currentArgument)) {
                                candidates.add(new Candidate(completion));
                            }
                        }
                    }
                }
            }
        };
    }

}
