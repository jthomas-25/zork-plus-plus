/**
 * GameState Class - represents the current state of the game: which dungeon is being played
 * and what room the adventurer is currently in.
 * @author Richard Volynski
 * @version 1.3
 * 4 June 2020
 */


class GameState {

    private Dungeon dungeon = null;
    private Room currentRoom = null;
    private String dungeonDesc = "Welcome to the Dungeon. Enjoy but you won't come out how you came in!";


    private static GameState single_instance = null;
    public static GameState instance() {
        if (single_instance == null)
            single_instance = new GameState();

        return single_instance;
    }


    private GameState() {
    }

    public void initialize(Dungeon dungeon) {
        this.dungeon = new Dungeon(currentRoom, dungeonDesc);
        this.currentRoom = dungeon.getCurrentRoom();
    }

    public Room getAdventurersCurrentRoom() {
        return currentRoom;
    }

    public void setAdventurersCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }
}

