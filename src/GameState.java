/**
 * GameState Class - represents the current state of the game: which dungeon is being played
 * and what room the adventurer is currently in.
 * @author Richard Volynski
 * @version 2.1
 * 17 June 2020
 */


import java.io.*;
import java.util.Scanner;

class GameState {

    private Dungeon dungeon = null;
    private Room currentRoom = null;
    private String dungeonDesc = "Welcome to the Dungeon. Enjoy but you won't come out how you came in!";
    private String gameFile = null;


    //Singleton instance of GameState class
    private static GameState single_instance = null;


    /**
     * instance - this method returns single instance of GameState class
     * @return single instance of GameState class
     * */
    public static GameState instance() {
        if (single_instance == null)
            single_instance = new GameState();

        return single_instance;
    }


    /**
     * Default Constructor GameState
     */
    private GameState() {
    }

    /**
     * initialize - This method initializes the dungeon and name of the starting room.
     * @param dungeon - controls entire game function
     */
    void initialize(Dungeon dungeon) {
//        this.dungeon = new Dungeon(currentRoom, dungeonDesc);
        this.dungeon = dungeon;
        this.currentRoom = dungeon.getEntry();
    }

    /**
     * getAdventurersCurrentRoom - this method returns the current room the user is in. The room updates as the
     * user moves.
     * @return currentRoom
     */
    Room getAdventurersCurrentRoom() {
        return currentRoom;
    }

    /**
     * setAdventurersCurrentRoom - this method sets the current room the user is in. The room updates as the
     * user moves.
     * @param room - new current room
     */
    void setAdventurersCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    /**
     * getDungeon - this method gets the Dungeon
     */
    Dungeon getDungeon() {
        return dungeon;
    }

    /**
     * store - this method stores the input from the file so that when the user saves the game, the user wll be
     * able to resume playing where they left off.
     * @param saveName
     * @exception IOException
     */
    void store(String saveName) throws IllegalSaveFormatException {
        try {
            this.gameFile = saveName;
            PrintWriter printWriter = new PrintWriter(saveName);
            printWriter.write("Zork II save data\n");
            dungeon.storeState(printWriter);
            printWriter.write("Current room: " + currentRoom.getName());
            printWriter.flush();
            printWriter.close();
        }
        catch (Exception ex) {
            throw new IllegalSaveFormatException("Failed to save game state");
        }
    }

    /**
     * restore - this method restores the game at the state which it was saved.
     * @param fileName
     * @exception IllegalSaveFormatException
     * @exception NoExitException
     * @exception FileNotFoundException
     * */
    void restore(String fileName) throws FileNotFoundException, NoRoomException, IllegalDungeonFormatException {
        File file = new File(fileName);
        Scanner gameScanner = new Scanner(file);
        String firstLine = gameScanner.nextLine();    //skip first line because it has generic comment
        String secLine = gameScanner.nextLine();  //reads Dungeon file name
        String[] secLineSplit = secLine.split(": ");    //parse by colon and space
        String zorkFileName = secLineSplit[1];  //Zork file name
        dungeon = new Dungeon(zorkFileName);
        initialize(dungeon);

        dungeon.restoreState(gameScanner);

        String currentRoomLine = gameScanner.nextLine();
        String[] currentRoomSplit = currentRoomLine.split(": ");
        String currentRoomName = currentRoomSplit[1];
        currentRoom = dungeon.getRoom(currentRoomName);
        dungeon.setEntry(currentRoom);
        gameScanner.close();
    }
}

/**
 * class IllegalDungeonFormatException is a custom exception
 */
class IllegalSaveFormatException extends Exception {

    /**
     * IllegalDungeonFormatException - default constructor
     */
    public IllegalSaveFormatException(String errorMsg) {
    }
}

