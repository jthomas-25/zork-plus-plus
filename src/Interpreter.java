/**
 * CPSC 240
 * Homework 3 - Zork 1
 * Interpreter Class -  this is the main() class that directs operations. It creates a Dungeon,
 * initializes the GameState with it, and repeatedly prompt the user for input. Each time the user
 * inputs a command, it should use the CommandFactory to instantiate a new Command object and execute it.
 * If the user enters "q", it terminates the program.
 * @author Richard Volynski
 * @version 1.0
 * 1 June 2020
 */


import java.util.Scanner;

public class Interpreter {

    public static void main(String[] args) {
        Scanner stdin = new Scanner(System.in);

        Room room = new Room("");

        String title = "";

        Dungeon dungeon = new Dungeon(room, title);    //TODO
//        GameState gameState = new GameState(dungeon);

        while (true) {
            System.out.print("Enter command: ");
            String command = stdin.nextLine();

            if (command.toLowerCase().equals("q")) {
                break;
            }
        }
    }

    private Dungeon buildSampleDungeon() {
        return null; //TODO return Dungeon
    }
}
