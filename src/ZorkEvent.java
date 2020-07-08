/**
 * ZorkEvent class - An abstract class as the base class for all sub classes for Zork+.
 * @author Object Oriented Optimists (OOO)
 * @author John Thomas
 * @author Richard Volynski
 * @version 1.1
 * 8 July 2020
 */
abstract class ZorkEvent {

    /**
     * trigger - this is an abstract method that will be implemented at the subclass level
     * @return event return code
     */
    abstract String trigger();
}

/**
 * ScoreEvent - this class extends ZorkEvent class, when triggered, changes the player's score
 * by a positive number of points.
 * Note that while the {@link GameState#setScore} method can also affect the player's score,
 * a ScoreEvent will only increase it.
 * @author Object Oriented Optimists (OOO)
 * @author John Thomas
 * @version 1.0
 * 8 July 2020
 */
class ScoreEvent extends ZorkEvent {
    private int points;

    /**
     * ScoreEvent - Constructor to set a given number of points.
     * @param points the number of points to be added to the player's score
     * @throws IllegalArgumentException if the number of points is less than or equal to 0
     */
    ScoreEvent(int points) throws IllegalArgumentException {
    }

    /**
     * trigger - this method implements abstract method from the base class.
     * It adds this event's number of points to the player's score.
     * @return event message
     */
    String trigger() {
        return null;    //TODO implement
    }
}

/**
 * WoundEvent - this class extends ZorkEvent class, changes the player's health
 * by a nonzero number of points. Note that a negative number of points will
 * effectively heal the player.
 * @author Object Oriented Optimists (OOO)
 * @author John Thomas
 * @author Richard Volynski
 * @version 1.1
 * 8 July 2020
 */
class WoundEvent extends ZorkEvent {
    private int points;

    /**
     * WoundEvent - this constructor implements a wound event and awards a given number of points.
     * @param points - the number of points to be added to the player's health
     * @throws IllegalArgumentException if the number of points is less than or equal to 0
     */
    WoundEvent(int points) throws IllegalArgumentException {
    }

    /**
     * trigger - this method implements abstract method from the base class and adds
     * this event's number of points to the player's health.
     * If the number of points is negative, it will be converted to a positive number before the addition.
     * @return a non-numeric message indicating the player's physical condition
     */
    String trigger() {
        return null;    //TODO implement
    }
}

/**
 * DieEvent - this class extends ZorkEvent class and, when triggered, ends the game with the player defeated.
 * @author Object Oriented Optimists (OOO)
 * @author John Thomas
 * @author Richard Volynski
 * @version 1.1
 * 8 July 2020
 */
class DieEvent extends ZorkEvent {

    /**
     * DieEvent - default constructor
     */
    DieEvent() {
        //TODO implement
    }

    /**
     * trigger - this method implements abstract method from the base class and ends the game at its current state,
     * as well as resets user's health
     * @return String message indicating that the player is dead
     */
    String trigger() {
        return null;    //TODO implement
    }
}

/**
 * A WinEvent extends ZorkEvent class and, when triggered, ends the game with the player victorious.
 * @author Object Oriented Optimists (OOO)
 * @author John Thomas
 * @author Richard Volynski
 * @version 1.1
 * 8 July 2020
 */
class WinEvent extends ZorkEvent {

    /**
     * WinEvent - default constructor
     */
    WinEvent() {
        //TODO implement
    }

    /**
     * trigger - this method implements abstract method from the base class and ends the game at its current state.
     * This method will print a win message and ask the user if they want to start over. If the user types
     * yes, reset the game. If user types no, end the game without saving user progress.
     * @return event message
     */
    String trigger() {
        return null;    //TODO implement
    }
}

/**
 * DropEvent - this class extends ZorkEvent and, when triggered, places an item in the current room,
 * mimicking the effect of the player typing a drop command.
 * @author Object Oriented Optimists (OOO)
 * @author John Thomas
 * @author Richard Volynski
 * @version 1.1
 * 8 July 2020
 */
class DropEvent extends ZorkEvent {
    private Item item;

    /**
     * DropEvent - this constructor constructs a new DropEvent with the given item.
     * @param item the item to be dropped into the current room
     * @throws NoItemException if the item does not exist in the current room or the player's inventory
     */ 
    DropEvent(Item item) throws NoItemException {
    }

    /**
     * trigger - this method implements abstract method from the base class and removes the item
     * from the player's inventory and adds it to the current room, if the item exists. If the item
     * already exists in the current room, both the room's contents and the player's inventory
     * remain unchanged.
     * @return event message
     */
    String trigger() {
        return null;    //TODO implement
    }
}

/**
 * DisappearEvent - this class extends ZorkEvent class and, when triggered, removes an item
 * from the game entirely: the item will no longer exist in the current room, the player's inventory,
 * or the dungeon.
 * @author Object Oriented Optimists (OOO)
 * @author John Thomas
 * @author Richard Volynski
 * @version 1.1
 * 8 July 2020
 */
class DisappearEvent extends ZorkEvent {
    private String name;

    /**
     * DisappearEvent - this constructor constructs a new DisappearEvent with the given name.
     * @param name the name of the item to be removed from the game
     * @throws NoItemException if this name does not correspond to any item in the current room,
     * the player's inventory, or the dungeon.
     */
    DisappearEvent(String name) throws NoItemException {
    }

    /**
     * trigger - this method implements abstract method from the base class and removes the item
     * corresponding to this name from either the current room or the player's inventory, as well
     * as from the dungeon itself. In other words, if the item does not exist in the current room
     * or the inventory, it will simply be removed from the dungeon.
     * @return event message
     */
    String trigger() {
        return null;    //TODO implement
    }
}

/**
 * TransformEvent - this class extends ZorkEvent and, when triggered, remove an item from the user's current
 * room and replace it another item.
 * @author Object Oriented Optimists (OOO)
 * @author John Thomas
 * @author Richard Volynski
 * @version 1.1
 * 8 July 2020
 */
class TransformEvent extends ZorkEvent {

    /**
     * TransformEvent - default constructor
     */
    TransformEvent() {
        //TODO implement
    }

    /**
     * trigger - implements abstract method from the base class and removes an item from the user's current
     * room and replace it with another item.
     * @return event message
     */
    String trigger() {
        return null;    //TODO implement
    }
}

/**
 * TeleportEvent - this class extends ZorkEvent and, when triggered, move the user to another room,
 * regardless if the user already visited that room.
 * @author Object Oriented Optimists (OOO)
 * @author John Thomas
 * @author Richard Volynki
 * @version 1.1
 * 8 July 2020
 */
class TeleportEvent extends ZorkEvent {

    /**
     * TeleportEvent - default constructor
     */
    TeleportEvent() {
        //TODO implement
    }

    /**
     * trigger - implements abstract method from the base class and moves the user to another room
     * @return event message
     */
    String trigger() {
        return null;    //TODO implement
    }
}

