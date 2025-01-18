package at.phillip.interfaces;

public interface Command {
    void execute(String[] args);
    String getName();
}
