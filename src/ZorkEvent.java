/**
 * ZorkEvent class
 * @author John Thomas
 * July 2020
 */
abstract class ZorkEvent {
    abstract String trigger();
    //abstract void trigger();
}

/**
 * A ScoreEvent represents a ZorkEvent that, when triggered, changes the player's score by a positive number of points.
 * 
 * Note that while the {@link GameState#setScore} method can also affect the player's score, a ScoreEvent will only increase it.
 * @author John Thomas
 */
class ScoreEvent extends ZorkEvent {
    private int points;

    /**
     * Constructs a new ScoreEvent with the given number of points.
     * 
     * @param points the number of points to be added to the player's score
     * @throws IllegalArgumentException if the number of points is less than or equal to 0
     */
    ScoreEvent(int points) throws IllegalArgumentException {
    }

    /**
     * Adds this event's number of points to the player's score.
     * @return "" (the empty string) 
     */
    String trigger() {
        return "";
    }
}

/**
 * A WoundEvent represents a ZorkEvent that, when triggered, changes the player's health by a nonzero number of points.
 * Note that a negative number of points will effectively heal the player.
 * @author John Thomas
 */
class WoundEvent extends ZorkEvent {
    private int points;

    /**
     * Constructs a new WoundEvent with the given number of points.
     * 
     * @param points the number of points to be added to the player's health
     * @throws IllegalArgumentException if the number of points is equal to 0
     */
    WoundEvent(int points) throws IllegalArgumentException {
    }

    /**
     * Adds this event's number of points to the player's health.
     * If the number of points is negative, it will be converted to a positive number before the addition.
     * 
     * @return a non-numeric message indicating the player's physical condition
     */
    String trigger() {
        return null;    //TODO implement
    }
}

/**
 * A DieEvent represents a ZorkEvent that, when triggered, ends the game with the player defeated.
 * @author John Thomas
 */
class DieEvent extends ZorkEvent {

    /**
     * Constructs a new DieEvent.
     */
    DieEvent() {
    }

    /**
     *
     */
    String trigger() {
        return null;    //TODO implement
    }
}

/**
 * A WinEvent represents a ZorkEvent that, when triggered, ends the game with the player victorious.
 * @author John Thomas
 */
class WinEvent extends ZorkEvent {

    /**
     * Constructs a new WinEvent.
     */
    WinEvent() {
    }

    /**
     *
     */
    String trigger() {
        return null;    //TODO implement
    }
}

/**
 * A DropEvent represents a ZorkEvent that, when triggered, places an item in the current room,
 * mimicking the effect of the player typing a drop command.
 * @author John Thomas
 */
class DropEvent extends ZorkEvent {
    private Item item;

    /**
     * Constructs a new DropEvent with the given item.
     * @param item the item to be dropped into the current room
     * @throws NoItemException if the item does not exist in the current room or the player's inventory
     */ 
    DropEvent(Item item) throws NoItemException {
    }

    /**
     * Removes the item from the player's inventory and adds it to the current room, if the item exists.
     * If the item already exists in the current room, both the room's contents and the player's inventory remain unchanged.
     * @return "" (the empty string)
     */
    String trigger() {
        return "";
    }
}

/** A DisappearEvent represents a ZorkEvent that, when triggered, removes an item from the game entirely:
 * the item will no longer exist in the current room, the player's inventory, or the dungeon.
 * @author John Thomas
 */
class DisappearEvent extends ZorkEvent {
    private String name;

    /**
     * Constructs a new DisappearEvent with the given name.
     * @param name the name of the item to be removed from the game
     * @throws NoItemException if this name does not correspond to any item in the current room, the player's inventory, or the dungeon.
     */
    DisappearEvent(String name) throws NoItemException {
    }

    /**
     * Removes the item corresponding to this name from either the current room or the player's inventory,
     * as well as from the dungeon itself. In other words, if the item does not exist in the current room
     * or the inventory, it will simply be removed from the dungeon.
     * @return "" (the empty string)
     */
    String trigger() {
        return "";
    }
}

/**
 * A TransformEvent represents a ZorkEvent that, when triggered,
 * @author John Thomas
 */
class TransformEvent extends ZorkEvent {

    /**
     * Constructs a new TransformEvent.
     */
    TransformEvent() {
    }

    /**
     * 
     */
    String trigger() {
        return null;    //TODO implement
    }
}

/**
 * A TeleportEvent represents a ZorkEvent that, when triggered,
 * @author John Thomas
 */
class TeleportEvent extends ZorkEvent {

    /**
     * Constructs a new TeleportEvent.
     */
    TeleportEvent() {
    }

    /**
     * 
     */
    String trigger() {
        return null;    //TODO implement
    }
}
