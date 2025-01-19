package at.phillip.interfaces;

import java.util.List;

public interface Command {
    void execute(String[] args);
    String getName();
    List<String> getAliases();
    List<String> getCompletions(String[] argIndex);
}
