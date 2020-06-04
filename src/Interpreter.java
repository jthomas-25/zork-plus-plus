/**
 * CPSC 240
 * Homework 3 - Zork 1
 * Interpreter Class -  this is the main() class that directs operations. It creates a Dungeon,
 * initializes the GameState with it, and repeatedly prompts the user for input. Each time the user
 * inputs a command, it should use the CommandFactory to instantiate a new Command object and execute it.
 * If the user enters "q", it terminates the program.
 * @author Richard Volynski
 * @version 1.2
 * 4 June 2020
 */


import java.util.Scanner;

public class Interpreter {
    public static void main(String[] args) {
        Scanner stdin = new Scanner(System.in);

        Dungeon dungeon = buildSampleDungeon();    //TODO
//        GameState gameState = new GameState(dungeon);


        System.out.println(dungeon.getTitle());

        while (true) {
            System.out.print("Enter command: ");
            String command = stdin.nextLine();

            if (command.toLowerCase().equals("q")) {
                break;
            }
            else {
                System.out.println(dungeon.getEntry().getName());
            }
        }
    }

    private static Dungeon buildSampleDungeon() {

        String dungeonName = "Dungeon";
        String dungeonDesc = "Welcome to the Dungeon. Enjoy but you won't come out how you came in!";

        String room1Name = "West of House";
        String room1Desc = "You are standing in an open field west of a white house. The front door is locked";
        Room room1 = new Room(room1Name);
        room1.setDesc(room1Desc);

        String room2Name = "South of House";
        String room2Desc = "You are facing the south side of a white house. There are no doors, and the windows are" +
                "locked";
        Room room2 = new Room(room2Name);
        room2.setDesc(room2Name);

        String room3Name = "Behind House";
        String room3Desc = "You are behind the white house. There is an open window.";
        Room room3 = new Room(room3Name);
        room3.setDesc(room3Desc);

        //when user opens window, user should type "Enter house", afterwards a description of the kitchen will be given
        //description should be "You are in the kitchen. A table seems to been recently used. There is a passage
        //to the west and a staircase going up. A chimney will lead you down. Should you decide to go East, there is
        //an open window (the window you came from to get into the house)

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
        String room6Desc = "This is the living room. You can see a doorway to the east, a wooden door with graffiti " +
                "to the west, which appears to be nailed shut, and a trophy case. Below you is a trap door.";
        Room room6 = new Room(room6Name);
        room6.setDesc(room6Desc);




        Room startingRoom = room1;

        Dungeon dungeon = new Dungeon(startingRoom, dungeonDesc);
        dungeon.add(room1);
        dungeon.add(room2);
        dungeon.add(room3);
        dungeon.add(room4);
        dungeon.add(room5);
        dungeon.add(room6);
        return dungeon; //TODO return Dungeon
    }
}

