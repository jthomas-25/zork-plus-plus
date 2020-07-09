/**
 * A ZorkEvent is anything that alters the game state when it occurs. (see {@link GameState}
 * for a summary of what information the game tracks.) A common way to implement ZorkEvents
 * is to associate them with item-specific commands in a .zork dungeon file, such that they
 * will be triggered when the player uses an item in a way that is recognizable to the game.
 * 
 * Note that ZorkEvents can exist independently of a dungeon file; they can be incorporated
 * in a hardcoded dungeon as well. Also, there is nothing preventing a ZorkEvent's occurrence
 * from depending on factors besides item interaction (e.g. an enemy that instantly kills the
 * player when the player enters a room). This is so the users of this API have greater
 * flexibility in adding their own features.
 * @author Object Oriented Optimists (OOO)
 * @author John Thomas
 * @author Richard Volynski
 * @version 1.3
 * 9 July 2020
 */
abstract class ZorkEvent {
    protected String message;

    /**
     * Activates this event, which modifies the game state.
     * @return this event's message
     */
    abstract String trigger();
}

/**
 * A ScoreEvent represents a ZorkEvent that, when triggered, changes the player's score
 * by a positive number of points.
 * Note that while the {@link GameState#setScore} method can also affect the player's score,
 * a ScoreEvent will only increase it.
 * @author Object Oriented Optimists (OOO)
 * @author John Thomas
 * @author Richard Volynski
 * @version 1.3
 * 9 July 2020
 */
class ScoreEvent extends ZorkEvent {
    private int points;

    /**
     * Constructs a ScoreEvent with the given number of points.
     * @param points the number of points to be added to the player's score
     * @throws IllegalArgumentException if the number of points is less than or equal to 0
     */
    ScoreEvent(int points) throws IllegalArgumentException {
        //TODO implement
    }

    /**
     * Adds this event's number of points to the player's score.
     * @return this event's message
     */
    String trigger() {
        return null;    //TODO implement
    }
}

/**
 * A WoundEvent represents a ZorkEvent that, when triggered, changes the player's health
 * by a nonzero number of points.
 * Note that a negative number of points will effectively heal the player.
 * @author Object Oriented Optimists (OOO)
 * @author John Thomas
 * @author Richard Volynski
 * @version 1.3
 * 9 July 2020
 */
class WoundEvent extends ZorkEvent {
    private int points;

    /**
     * Constructs a WoundEvent with the given number of points.
     * @param points the number of points to be subtracted from the player's health
     * @throws IllegalArgumentException if the number of points is equal to 0
     */
    WoundEvent(int points) throws IllegalArgumentException {
        //TODO implement
    }

    /**
     * Subtracts this event's number of points from the player's health.
     * @return a non-numeric message indicating the player's physical condition
     */
    String trigger() {
        return null;    //TODO implement
    }
}

/**
 * A DieEvent represents a ZorkEvent that, when triggered, ends the game with the player defeated.
 * Note that if the player loses the game, the {@link Interpreter} will stop processing typed commands;
 * it will instead prompt the player with a question asking them if they want to continue or start over.
 * @author Object Oriented Optimists (OOO)
 * @author John Thomas
 * @author Richard Volynski
 * @version 1.3
 * 9 July 2020
 */
class DieEvent extends ZorkEvent {

    /**
     * Constructs a new DieEvent with the given message.
     * @param message the message indicating that the player is dead and has lost the game
     */
    DieEvent(String message) {
        //TODO implement
    }

    /**
     * Ends the game in a "lose" state, and returns a losing message telling the player they are dead.
     * @return this event's "lose" message
     */
    String trigger() {
        return null;    //TODO implement
    }
}

/**
 * A WinEvent represents a ZorkEvent that, when triggered, ends the game with the player victorious.
 * Note that if the player wins the game, the {@link Interpreter} will stop processing typed commands;
 * it will instead prompt the player with a yes/no question asking them if they want to start over.
 * @author Object Oriented Optimists (OOO)
 * @author John Thomas
 * @author Richard Volynski
 * @version 1.3
 * 9 July 2020
 */
class WinEvent extends ZorkEvent {

    /**
     * Constructs a new WinEvent with a given message.
     * @param message the message indicating that the player has won
     */
    WinEvent(String message) {
        //TODO implement
    }

    /**
     * Ends the game in a "win" state, and returns a message telling the player they have won and ask
     * if they want to start over. TODO implement/don't delete
     *
     * If the user types yes, reset the game. If user types no, end the game without saving user progress.
     * @return this event's "win" message   TODO implement/don't delete
     */
    String trigger() {
        return null;    //TODO implement
    }
}

/**
 * A DropEvent represents a ZorkEvent that, when triggered, places an item in the current room,
 * mimicking the effect of the player typing a drop command.
 * @author Object Oriented Optimists (OOO)
 * @author John Thomas
 * @author Richard Volynski
 * @version 1.3
 * 9 July 2020
 */
class DropEvent extends ZorkEvent {
    private Item item;

    /**
     * Constructs a new DropEvent with the given item.
     * @param item the item to be dropped into the current room
     * @throws NoItemException if the item does not exist in the current room or the player's inventory
     */ 
    DropEvent(Item item) throws NoItemException {
        //TODO implement
    }

    /**
     * Removes the item from the player's inventory and adds it to the current room, if the item exists.
     * If the item already exists in the current room, both the room's contents
     * and the player's inventory remain unchanged.
     * @return this event's message
     */
    String trigger() {
        return null;    //TODO implement
    }
}

/**
 * A DisappearEvent represents a ZorkEvent that, when triggered, removes an item
 * from the game entirely: the item will no longer exist in the current room,
 * the player's inventory, or the dungeon.
 * @author Object Oriented Optimists (OOO)
 * @author John Thomas
 * @author Richard Volynski
 * @version 1.3
 * 9 July 2020
 */
class DisappearEvent extends ZorkEvent {
    private String itemName;

    /**
     * Constructs a new DisappearEvent with the given item name.
     * @param itemName the name of the item to be removed from the game
     * @throws NoItemException if this name does not correspond to any item in the current room,
     * the player's inventory, or the dungeon.
     */
    DisappearEvent(String itemName) throws NoItemException {
        //TODO implement
    }

    /**
     * Removes the item corresponding to this name from either the current room or the player's inventory,
     * as well as from the dungeon itself, if it exists in any of these places. In other words, if the item
     * does not exist in the current room or the inventory, it will simply be removed from the
     * dungeon (if it exists there).
     * @return this event's message
     */
    String trigger() {
        return null;    //TODO implement
    }
}

/**
 * A TransformEvent represents a ZorkEvent that, when triggered, removes an item
 * from the game entirely and replaces it with a previously nonexistent item.
 * @author Object Oriented Optimists (OOO)
 * @author John Thomas
 * @author Richard Volynski
 * @version 1.3
 * 9 July 2020
 */
class TransformEvent extends ZorkEvent {
    private String nameOfItemToReplace;
    private String primaryNameOfNewItem;

    /**
     * Constructs a new TransformEvent with the given item names.
     * @param nameOfItemToReplace the name of the item to be replaced
     * @param primaryNameOfNewItem the name of the brand-new item
     * @throws NoItemException if the item to be replaced does not exist in the current room or the player's inventory,
     * or if the new item does not exist in the dungeon
     */
    TransformEvent(String nameOfItemToReplace, String primaryNameOfNewItem) {
        //TODO implement
    }

    /**
     * Removes the existing item and replaces it with the new item, if both of these items
     * are known by these names, respectively.
     * @return this event's message
     */
    String trigger() {
        return null;    //TODO implement
    }
}

/**
 * A TeleportEvent represents a ZorkEvent that, when triggered, moves the player
 * to a random room in the dungeon.
 * Note that this room may be one the player has already visited, or even one
 * that is not otherwise reachable (e.g. a room with no exits).
 * @author Object Oriented Optimists (OOO)
 * @author John Thomas
 * @author Richard Volynki
 * @version 1.3
 * 9 July 2020
 */
class TeleportEvent extends ZorkEvent {
    private String roomName;

    /**
     * Constructs a new TeleportEvent with the given room name.
     * @param roomName the name of the room to teleport the player to
     * @throws NoRoomException if no room by this name exists in the dungeon
     */
    TeleportEvent(String roomName) throws NoRoomException {
        //TODO implement
    }

    /**
     * Moves the player to the room corresponding to this name, if the room exists.
     * @return this event's message
     */
    String trigger() {
        return null;    //TODO implement
    }
}

