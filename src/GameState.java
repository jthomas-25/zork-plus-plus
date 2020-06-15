/**
 * GameState Class - represents the current state of the game: which dungeon is being played
 * and what room the adventurer is currently in.
 * @author Richard Volynski
 * @version 1.9
 * 15 June 2020
 */


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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

    void store(String saveName) throws IOException {
        this.gameFile = saveName;
        PrintWriter printWriter = new PrintWriter(saveName);
        printWriter.write("Zork II save data\n" + "Dungeon file: C:\\Temp\\ZorkII.zork\n");
        dungeon.storeState(printWriter);
        printWriter.write(currentRoom.getName());
        printWriter.flush();
        printWriter.close();
    }

    void restore(String fileName) {
    }
}

