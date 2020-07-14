import java.io.File;
import java.io.IOException;
//import java.util.Hashtable;
import java.util.Scanner;

/**
 * CPSC 240
 * Team Programming Assignment #1 — "Zork III"
 * Interpreter Class -  this is the main() class that directs operations. It creates a Dungeon,
 * initializes the GameState with it, and repeatedly prompts the user for input. Each time the user
 * inputs a command, it should use the CommandFactory to instantiate a new Command object and execute it.
 * If the user enters "q", it terminates the program.
 * @author Object Oriented Optimists (OOO)
 * @version 2.9
 * 14 July 2020
 */
public class Interpreter {
    private String commandEntered;
    //private Hashtable<String, String> specialCommands = new Hashtable<>();;

    /**
     * The main method, argument(s) should be the file path of the dungeon or save (dot sav file) to be played/restored.
     * This method does this by first getting the file and then calling on various methods within GameState class to
     * hydrate, creating a new game.
     * If no save or dungeon file is provided, the method exits with a message indicating the right arguments.
     * If a dungeon file format is Illegal, the method exits while throwing an IllegalSaveFormatException exception.
     *
     */
    public static void main(String[] args) throws IllegalSaveFormatException, NoItemException, NoRoomException {
        Scanner stdin = new Scanner(System.in);

        String defaultZorkFile = "sample.zork";
        if (args.length > 0) {
            defaultZorkFile = args[0];
        } else {
            assert true; // essentially do nothing
        }

        Dungeon dungeon = null;
        try {
            if (defaultZorkFile.endsWith(".sav")) {
                GameState.instance().restore(defaultZorkFile);
                dungeon = GameState.instance().getDungeon();
            } else {
                dungeon = new Dungeon(defaultZorkFile, true);
                if (dungeon.getEntry() == null) {
                    return;
                }
                GameState.instance().initialize(dungeon);
            }
        } catch (Exception e) {
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

            switch (commandEntered) {
                case "save":
                    System.out.print("Enter the name of your save file: ");
                    String saveFilename = stdin.nextLine();
                    command = CommandFactory.instance().parse(commandEntered + " " + saveFilename);
                break;
                case "unlock exit":
                    System.out.print("Which exit? Enter a direction: ");
                    String exitDir = stdin.nextLine();
                    command = CommandFactory.instance().parse(commandEntered.replace(" ", String.format(" %s ", exitDir)));
                break;
                default:
                    command = CommandFactory.instance().parse(commandEntered);
                    break;
            }
            if (command != null) {
                String output = command.execute();
                if (output == null) {
                    System.out.println("See you next time!");
                } else {
                    System.out.println(output);
                }
            }
        }
    }
}
