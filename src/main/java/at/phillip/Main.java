package at.phillip;

import at.phillip.console.ConsoleManager;

public class Main {
    public static void main(String[] args) {
        try {
            new ConsoleManager().startConsole();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}