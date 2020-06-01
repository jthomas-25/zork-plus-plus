/**
 * GameState Class - represents the current state of the game: which dungeon is being played
 * and what room the adventurer is currently in.
 * @author Richard Volynski
 * @version 1.0
 * 1 June 2020
 */


class GameState {

    public GameState instance() {
        return this;
    }

    private GameState() {
    }

    public void initialize(Dungeon dungeon) {
    }

    public Room getAdventurersCurrentRoom() {
        return null; //TODO return Room
    }

    public void setAdventurersCurrentRoom(Room room) {
    }

    public Dungeon getDungeon() {
        return null; //TODO return Dungeon
    }
}
