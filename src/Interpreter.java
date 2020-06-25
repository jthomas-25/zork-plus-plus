/**
 * CPSC 240
 * Team Programming Assignment #1 — "Zork III"
 * Interpreter Class -  this is the main() class that directs operations. It creates a Dungeon,
 * initializes the GameState with it, and repeatedly prompts the user for input. Each time the user
 * inputs a command, it should use the CommandFactory to instantiate a new Command object and execute it.
 * If the user enters "q", it terminates the program.
 * @author Object Oriented Optimists
 * @version 2.6
 * 25 June 2020
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
//            System.out.println("Zork file not provided, using \"ZorkII.zork\"");
        }
        Dungeon dungeon = null;
        try {
//            dungeon = buildSampleDungeon();
            if (defaultZorkFile.endsWith(".sav")) {
                dungeon = new Dungeon(defaultZorkFile, true);
//                GameState.instance().restore(defaultZorkFile);
                dungeon = GameState.instance().getDungeon();
            }
            else {
                dungeon = new Dungeon(defaultZorkFile,false);
                GameState.instance().initialize(dungeon);
            }
        }
        catch (Exception e) {
            System.out.println("Exception happened: " + e.toString());
            e.printStackTrace();
            return;
        }

        System.out.println(dungeon.getTitle());
        System.out.println();
        System.out.println(dungeon.getEntry().describe());

        String commandEntered = "";
        while (!commandEntered.equalsIgnoreCase("save")) {
            System.out.print("Enter command: ");
            commandEntered = stdin.nextLine();

            if (commandEntered.toLowerCase().equals("q")) {
                break;
            }
//            else if (!(commandEntered.equalsIgnoreCase("N")
//                    || commandEntered.equalsIgnoreCase("W")
//                    || commandEntered.equalsIgnoreCase("E")
//                    || commandEntered.equalsIgnoreCase("S")
//                    || commandEntered.equalsIgnoreCase("U")
//                    || commandEntered.equalsIgnoreCase("D")
//                    || commandEntered.equalsIgnoreCase("look")
//                    || commandEntered.equalsIgnoreCase("take")
//                    || commandEntered.equalsIgnoreCase("drop")
//                    || commandEntered.equalsIgnoreCase("i")
//                    || commandEntered.equalsIgnoreCase("save"))) {
//                System.out.println("I'm sorry I don't understand the command " + "\"" + commandEntered + "\"" + " yet."
//                        + " " + "\"" + commandEntered.toLowerCase() + "\"" + " will be implemented soon.");
//            }
            else {
//                System.out.println(dungeon.getEntry().getName());
//                Command command = new Command (commandEntered);
                if (commandEntered.isEmpty()) {
                    continue;
                }
                Command command = CommandFactory.instance().parse(commandEntered);
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
        stdin.close();
    }

    /**
     * buildSampleDungeon - this method creates rooms with names, descriptions, and directions
     * */
    private static Dungeon buildSampleDungeon() throws NoExitException {

        String dungeonName = "Dungeon";
        String dungeonDesc = "Welcome to the Dungeon. Enjoy but you won't come out how you came in!";

        String room1Name = "West of House";
        String room1Desc = "You are standing in an open field west of a white house. The front door is locked.";
        Room room1 = new Room(room1Name);
        room1.setDesc(room1Desc);

        String room2Name = "South of House";
        String room2Desc = "You are facing the south side of a white house. There are no doors, and the " +
                "windows are locked.";
        Room room2 = new Room(room2Name);
        room2.setDesc(room2Desc);

        String room3Name = "Behind House";
        String room3Desc = "You are behind the white house. There is an open window.";
        Room room3 = new Room(room3Name);
        room3.setDesc(room3Desc);

        String room4Name = "Kitchen";
        String room4Desc = "This is the kitchen. It seems like a table has been recently used. There is a passage" +
                " to the west and a staircase leading up. A chimney leads down and to the east is the open window" +
                " which you came from";
        Room room4 = new Room(room4Name);
        room4.setDesc(room4Desc);

        String room5Name = "North of House";
        String room5Desc = "There is no door here and all the windows are locked. However there is" +
                " a path to the North";
        Room room5 = new Room(room5Name);
        room5.setDesc(room5Desc);

        String room6Name = "Living Room";
        String room6Desc = "This is the living room. You can see a doorway to the east, a wooden door " +
                "with graffiti " + "to the west, which appears to be nailed shut, and a trophy case. " +
                "Below you is a trap door.";
        Room room6 = new Room(room6Name);
        room6.setDesc(room6Desc);

        room1.addExit(new Exit("S", room1, room2));
        room1.addExit(new Exit("N", room1, room5));
        room1.addExit(new Exit("W", room1, room6));

        room2.addExit(new Exit("N", room2, room1));
        room2.addExit(new Exit("E", room2, room3));

        room3.addExit(new Exit("W", room3, room4));
        room3.addExit(new Exit("S", room3, room2));
        room3.addExit(new Exit("W", room3, room5));

        room4.addExit(new Exit("E", room4, room3));
        room4.addExit(new Exit("W", room4, room6));

        room5.addExit(new Exit("E", room5, room3));
        room5.addExit(new Exit("S", room5, room1));

        room6.addExit(new Exit("E", room6, room4));


        Room startingRoom = room1;

        Dungeon dungeon = new Dungeon(startingRoom, dungeonDesc);
        dungeon.add(room1);
        dungeon.add(room2);
        dungeon.add(room3);
        dungeon.add(room4);
        dungeon.add(room5);
        dungeon.add(room6);
        return dungeon;
    }
}

