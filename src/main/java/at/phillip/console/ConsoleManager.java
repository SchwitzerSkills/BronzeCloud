package at.phillip.console;

import at.phillip.commands.*;
import at.phillip.interfaces.Command;
import at.phillip.utils.ConsoleColors;
import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class ConsoleManager {

    private final Map<String, Command> commands = new HashMap<>();

    public ConsoleManager() {
        registerCommands();
    }

    private void registerCommands() {
        addCommand(new StartCommand());
        addCommand(new StopCommand());
//        addCommand(new ListCommand());
//        addCommand(new ReloadCommand());
    }

    private void addCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public void startConsole() throws IOException {
        Terminal terminal = TerminalBuilder.builder().system(true).build();
        String hostname = InetAddress.getLocalHost().getHostName();

        Completer completer = new ArgumentCompleter(
                new StringsCompleter(commands.keySet()),
                new StringsCompleter("server1", "server2", "all")
        );

        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(completer)
                .parser(new DefaultParser())
                .build();

        String prompt = ConsoleColors.GREEN + "root@" + hostname + ConsoleColors.BLUE + " : " + ConsoleColors.RESET;

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
}
