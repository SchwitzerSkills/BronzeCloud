package at.phillip.console;

import at.phillip.commands.*;
import at.phillip.interfaces.Command;
import at.phillip.utils.ConsoleColors;
import at.phillip.utils.ServerManager;
import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConsoleManager {

    private final Map<String, Command> commands = new HashMap<>();
    private ServerManager serverManager = new ServerManager();

    public ConsoleManager() {
        registerCommands();
    }

    private void registerCommands() {
        addCommand(new StartCommand(serverManager));
        addCommand(new StopCommand());
        addCommand(new CreateCommand(serverManager));
//        addCommand(new ListCommand());
//        addCommand(new ReloadCommand());
    }

    private void addCommand(Command command) {
        commands.put(command.getName(), command);
        for (String alias : command.getAliases()) {
            commands.put(alias, command);
        }
    }

    public void startConsole() throws IOException {
        Terminal terminal = TerminalBuilder.builder().system(true).build();

        Completer completer = getCompletion();

        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(completer)
                .parser(new DefaultParser())
                .build();

        String prompt = ConsoleColors.GREEN + "root@BronzeCloud" + ConsoleColors.BLUE + " : " + ConsoleColors.RESET;

        while (true) {
            try {
                String line = reader.readLine(prompt).trim();
                handleCommand(line);
            } catch (UserInterruptException | EndOfFileException e) {
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

    public Completer getCompletion(){
        return (reader, line, candidates) -> {
            if (line.wordIndex() == 0) {
                for (String command : commands.keySet()) {
                    candidates.add(new Candidate(command));
                }
            } else {
                String commandName = line.words().getFirst();
                Command command = commands.get(commandName);

                if (command != null) {
                    int argIndex = line.wordIndex();
                    List<String> completions = command.getCompletions(argIndex);

                    for (String completion : completions) {
                        candidates.add(new Candidate(completion));
                    }
                }
            }
        };
    }
}
