/**
 * CPSC 240
 * Team Programming Assignment #1 — "Zork III"
 * Interpreter Class -  this is the main() class that directs operations. It creates a Dungeon,
 * initializes the GameState with it, and repeatedly prompts the user for input. Each time the user
 * inputs a command, it should use the CommandFactory to instantiate a new Command object and execute it.
 * If the user enters "q", it terminates the program.
 * @author Richard Volynski
 * @version 2.6
 * 1 July 2020
 */


import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.*;

public class Interpreter {
    private String commandEntered;

    /**
     * Main class - Argument can be either a .sav file or .ZORK file
     */
    public static void main(String[] args) throws IllegalSaveFormatException {
        Scanner stdin = new Scanner(System.in);


        String defaultZorkFile = "ZorkIII_Test_File.zork";
        if (args.length > 0) {
            defaultZorkFile = args[0];
        }
        else {
            assert true; // essentially do nothing
        }

        Dungeon dungeon = null;
        try {
            if (defaultZorkFile.endsWith(".sav")) {
                GameState.instance().restore(defaultZorkFile);
                dungeon = GameState.instance().getDungeon();
            }
            else {
                dungeon = new Dungeon(defaultZorkFile, true);
                GameState.instance().initialize(dungeon);
            }
        }
        catch (Exception e) {
            System.out.println("Exception happened: " + e);
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println(dungeon.getTitle());
        System.out.println();
        System.out.println(dungeon.getEntry().describe());

        String commandEntered = "";
        while (!commandEntered.equalsIgnoreCase("q")) {
            System.out.print("> ");
            commandEntered = stdin.nextLine();
            Command command;

            if (commandEntered.toLowerCase().equals("save")) {
                System.out.print("Enter the name of your save file: ");
                String saveFilename = stdin.nextLine();
                command = CommandFactory.instance().parse(commandEntered + " " + saveFilename);
            } else {
                command = CommandFactory.instance().parse(commandEntered);
            }
            if (command != null) {
                String output = command.execute();
                if (output == null) {
                    System.out.println("See you next time!");
                }
                else {
                    System.out.println(output);
                }
            }
        }
    }
}


