//package com.company;

import java.util.ArrayList;
import java.util.Random;


/**
 * <p>A ZorkEvent is anything that alters the game state when it occurs. (see {@link GameState}
 * for a summary of what information the game tracks.) A common way to implement events is to
 * associate one or more of them with an item-specific command in a .zork dungeon file, such that
 * they will be triggered when the player uses an item in a way that is recognizable to the game.</p>
 *
 * <ul>
 *   <li>When using {@link EventFactory#parse} to instantiate a ZorkEvent object, the proper syntax
 * for the event string is as follows: eventName or eventName(optional parameters).</li>
 *
 *   <li>If attached to an item-specific command in a .zork file, an event must be written in
 * the following [] syntax: verb[eventName(optional parameters)]:message.</li>
 * </ul>
 *
 * <p>Here is a detailed example:</p>
 * <pre>
 *  DrPepper,can,soda
 *  10
 *  kick[Drop]:The can skitters down the hallway.
 *  shake:A liquid fizzes menacingly inside the can.
 *  drink[Transform(emptyCan),Wound(-1)]:Gulp, gulp -- that was GOOD!  *belch*</pre>
 *
 * <p>ZorkEvents can exist independently of a dungeon file; they can be incorporated
 * in a hardcoded dungeon as well. Also, there is nothing preventing a ZorkEvent's occurrence
 * from depending on factors besides item interaction (e.g. an enemy that instantly kills the
 * player when the player enters a room). This is so the users of this API have greater
 * flexibility in adding their own features.</p>
 * @author John Thomas
 * @version 1.6
 * 16 July 2020
 */
public abstract class ZorkEvent {
    protected String message;

    /**
     * Activates this event, which modifies the game state.
     * @return this event's message
     */
    abstract String trigger(String noun);
}

/**
 * A ScoreEvent represents a {@link ZorkEvent} that, when triggered, changes the player's score
 * by a positive number of points.
 * Note that while the {@link GameState#setScore} method can also affect the player's score,
 * a ScoreEvent will only increase it.
 * @author John Thomas
 * @author Richard Volynski
 * @version 1.5
 * 16 July 2020
 */
class ScoreEvent extends ZorkEvent {
    private int points;

    /**
     * Constructs a ScoreEvent with the given number of points.
     * @param points the number of points to be added to the player's score
     * @throws IllegalArgumentException if the number of points is less than or equal to 0
     */
    ScoreEvent(int points) throws IllegalArgumentException {
        if (points <= 0) {
            throw new IllegalArgumentException("Number of points for a ScoreEvent must be positive.");
        } else {
            this.points = points;
        }
    }
    
    /**
     * Adds this event's number of points to the player's score.
     * @return this event's message
     */
    String trigger(String noun) {
        int currentScore = GameState.instance().getScore();
        currentScore += points;
        GameState.instance().setScore(currentScore);
        this.message = String.format("Score +%s", points);
        return this.message;
    }
}

/**
 * A WoundEvent represents a {@link ZorkEvent} that, when triggered, changes the player's health
 * by a nonzero number of points.
 * Note that a negative number of points will effectively heal the player.
 * @author John Thomas (for phase 1)
 * @author Richard Volynski
 * @version 1.7
 * 21 July 2020
 */
class WoundEvent extends ZorkEvent {
    private int damagePoints;

    /**
     * Constructs a WoundEvent with the given number of damage points.
     * @param damagePoints the number of points to be subtracted from the player's health
     * @throws IllegalArgumentException if the number of damage points is equal to 0
     */
    WoundEvent(int damagePoints) throws IllegalArgumentException {
        if (damagePoints == 0) {
            throw new IllegalArgumentException("Number of points for a WoundEvent must be nonzero.");
        } else { 
            this.damagePoints = damagePoints;
        }
    }
    
    /**
     * Subtracts this event's number of damage points from the player's health.
     * @return a non-numeric message indicating the player's physical condition
     */
    String trigger(String noun) {
        int playersHealth = GameState.instance().getHealth();
        playersHealth -= damagePoints;
        GameState.instance().setHealth(playersHealth);
        String healthMsg = GameState.instance().getHealthMsg();
        this.message = String.format("Damage -%s"+ "\nHealth: %s", damagePoints, playersHealth);
        return this.message;
    }
}

/**
 * A DieEvent represents a {@link ZorkEvent} that, when triggered, ends the game with the player defeated.
 * Note that if the player loses the game, the {@link Interpreter} will stop processing typed commands;
 * it will instead prompt the player with a question asking them if they want to continue or start over.
 * @author John Thomas
 * @version 1.4
 * 16 July 2020
 */
class DieEvent extends ZorkEvent {

    /**
     * Constructs a new DieEvent with the default message.
     */
    DieEvent() {
        GameState.instance().setHealth(0);
        this.message = GameState.instance().getHealthMsg();
    }

    /**
     * Constructs a new DieEvent with the given message (hardcoded).
     * @param message the message indicating that the player is dead and has lost the game
     */
    DieEvent(String message) {
        this.message = message;
    }

    /**
     * Ends the game in a "lose" state, and returns a losing message telling the player they are dead.
     * @return this event's "lose" message
     */
    String trigger(String noun) {
        GameState.instance().killPlayer();
        GameState.instance().endGame();
        return this.message;
    }
}

/**
 * A WinEvent represents a {@link ZorkEvent} that, when triggered, ends the game with the player victorious.
 * Note that if the player wins the game, the {@link Interpreter} will stop processing typed commands;
 * it will instead prompt the player with a yes/no question asking them if they want to start over.
 * @author John Thomas
 * @author Richard Volynski
 * @version 1.5
 * 16 July 2020
 */
class WinEvent extends ZorkEvent {

    /**
     * Constructs a new WinEvent with the default message.
     */
    WinEvent() {
        this.message = "You have won! \nFinal score: " + GameState.instance().getScore()
                + "\nRank: " + GameState.instance().getRank();
    }

    /**
     * Constructs a new WinEvent with the given message (hardcoded).
     * @param message the message indicating that the player has won
     */
    WinEvent(String message) {
        this.message = message;
    }

    /**
     * Ends the game in a "win" state, and returns a message telling the player they have won and ask
     * if they want to start over.
     *
     * If the user types yes, reset the game. If user types no, end the game without saving user progress.
     * @return this event's "win" message
     */
    String trigger(String noun) {
        GameState.instance().endGame();
        return this.message;
    }
}

/**
 * A DropEvent represents a {@link ZorkEvent} that, when triggered, places an item in the current room,
 * mimicking the effect of the player typing a drop command.
 * @author John Thomas
 * @version 1.5
 * 16 July 2020
 */
class DropEvent extends ZorkEvent {
    private String itemName;

    /**
     * Constructs a new DropEvent with the given item name.
     * @param itemName the name of the item to be dropped into the current room
     * @throws NoItemException if the item does not exist in the current room or the player's inventory
     */
    DropEvent(String itemName) throws NoItemException {
        this.itemName = itemName;
    }

    /**
     * Removes the item from the player's inventory and adds it to the current room, if the item exists.
     * If the item already exists in the current room, both the room's contents
     * and the player's inventory remain unchanged.
     * @return this event's message
     */
    String trigger(String noun) {
        Item item = null;
        Room currentRoom = GameState.instance().getAdventurersCurrentRoom();
        try {
            item = GameState.instance().getItemFromInventoryNamed(this.itemName);
            GameState.instance().removeFromInventory(item);
            currentRoom.add(item);
            this.message = String.format("\"%s\" was dropped from inventory and placed in %s",
                    item, currentRoom);
        } catch (NoItemException e) {
            item = currentRoom.getItemNamed(this.itemName);
            if (item == null) {
                this.message = String.format("%s not found", this.itemName);
            }
        }
        return this.message;
    }
}

/**
 * A DisappearEvent represents a {@link ZorkEvent} that, when triggered, removes an item
 * from the game entirely: the item will no longer exist in the current room,
 * the player's inventory, or the dungeon.
 * @author John Thomas
 * @author Richard Volynski
 * @version 1.6
 * 16 July 2020
 */
class DisappearEvent extends ZorkEvent {
    private String itemName;

    /**
     * Constructs a new DisappearEvent with the given item name.
     * @param itemName the name of the item to be removed from the game
     * @throws NoItemException if this name does not correspond to any item in the current room,
     * the player's inventory, or the dungeon
     */
    DisappearEvent(String itemName) throws NoItemException {
        this.itemName = itemName;
    }

    /**
     * Removes the item corresponding to this name from either the current room or the player's inventory,
     * as well as from the dungeon itself, if it exists in any of these places. In other words, if the item
     * does not exist in the current room or the inventory, it will simply be removed from the
     * dungeon (if it exists there).
     * @return this event's message
     */
    String trigger(String noun) {
        Room currentRoom = GameState.instance().getAdventurersCurrentRoom();
        currentRoom.removeItem(this.itemName);
        try {
            GameState.instance().removeFromInventory(this.itemName);
        } catch (Exception e) {
        }
        GameState.instance().getDungeon().removeItem(this.itemName);
        this.message = String.format("\n%s was removed from user's inventory, %s, and %s.",
                this.itemName, currentRoom, GameState.instance().getDungeon().getTitle());
        return this.message;
    }
}

/**
 * A TransformEvent represents a {@link ZorkEvent} that, when triggered, removes an item
 * from the game entirely and replaces it with a previously nonexistent item.
 * @author Richard Volynski
 * @version 1.6
 * 16 July 2020
 */
class TransformEvent extends ZorkEvent {
    private String nameOfItemToReplace;
    private String primaryNameOfNewItem;

    /**
     * Constructs a new TransformEvent with the given item names.
     * @param primaryNameOfNewItem the name of the brand-new item
     * @throws NoItemException if the item to be replaced does not exist in the current room or the player's inventory,
     * or if the new item does not exist in the dungeon
     */
    TransformEvent(String primaryNameOfNewItem) {
        this.primaryNameOfNewItem = primaryNameOfNewItem;
    }

    /**
     * Removes the existing item and replaces it with the new item, if both of these items
     * are known by these names, respectively.
     * @return this event's message
     */
    String trigger(String noun) {
        this.nameOfItemToReplace = noun;
        Item itemToReplace = null;
        Dungeon dungeon = GameState.instance().getDungeon();
        Room currentRoom = GameState.instance().getAdventurersCurrentRoom();
        if (GameState.instance().itemExistsInInventory(this.nameOfItemToReplace)) {
            try {
                itemToReplace = GameState.instance().getItemFromInventoryNamed(this.nameOfItemToReplace);
            } catch (NoItemException e) {
            }
            GameState.instance().removeFromInventory(itemToReplace);
            Item newItem = dungeon.getItem(primaryNameOfNewItem);
            if (newItem != null) {
                GameState.instance().addToInventory(newItem);
            }
            this.message = String.format("%s was removed from user's inventory and replaced by %s",
                    itemToReplace, newItem);
        }
        else if (currentRoom.hasItemNamed(this.nameOfItemToReplace)) {
            itemToReplace = currentRoom.getItemNamed(this.nameOfItemToReplace);
            currentRoom.removeItem(this.nameOfItemToReplace);
            currentRoom.addItem(primaryNameOfNewItem);
            this.message = String.format("%s was removed from %s and replaced by %s",
                    itemToReplace, currentRoom, this.primaryNameOfNewItem);
        }
//        GameState.instance().getDungeon().removeItem(nameOfItemToReplace);

        return this.message;
    }
}

/**
 * A TeleportEvent represents a {@link ZorkEvent} that, when triggered, moves the player
 * to a random room in the dungeon.
 * Note that this room may be one the player has already visited, or even one
 * that is not otherwise reachable (e.g. a room with no exits).
 * @author John Thomas
 * @version 1.6
 * 16 July 2020
 */
class TeleportEvent extends ZorkEvent {
    private Room newRoom;
    private Random rng;

    /**
     * Constructs a new TeleportEvent with no room name.
     */
    TeleportEvent() {
        rng = new Random();
    }

    /**
     * Constructs a new TeleportEvent with the given room name (hardcoded).
     * @param roomName the name of the room to teleport the player to
     * @throws Exception if no room by this name exists in the dungeon
     */
    TeleportEvent(String roomName) throws Exception {
        this.newRoom = GameState.instance().getDungeon().getRoom(roomName);
        if (this.newRoom == null) { 
            throw new Exception("No room by this name exists in the dungeon.");
        }
    }

    /**
     * Randomly moves the player to another room in the dungeon, if the room exists.
     * @return this event's message
     */
    String trigger(String noun) {
        ArrayList<Room> rooms = GameState.instance().getDungeon().getRooms();
        Room currentRoom = GameState.instance().getAdventurersCurrentRoom();

        switch (rooms.size()) {
            case 1:
                this.message = String.format("\n%s", currentRoom.describe());
                break;
            default:
                boolean sameRoom = false;
                do {
                    int randomNumber = rng.nextInt(rooms.size());
                    this.newRoom = rooms.get(randomNumber);
                    String newRoomName = this.newRoom.getName();
                    String currentRoomName = currentRoom.getName();
                    sameRoom = newRoomName.equals(currentRoomName);
                } while (sameRoom);
                    
                GameState.instance().setAdventurersCurrentRoom(this.newRoom);
                this.message = String.format("\n%s", this.newRoom.describe());
                break;
        }
        return this.message;
    }
}

/**
 * A PotionEffect is a {@link ZorkEvent} that when triggered, alters the way the player interacts with the dungeon.
 * For example, a dizzy effect will cause the player's movement commands to send them through a random exit, despite
 * what direction they give.  Another example, the "babble" effect will cause printed statements to come out reversed.
 * @author Robert Carroll
 */
class PotionEffect extends ZorkEvent {
    private String effect;

    /**
     * Constructs a new PotionEffect based on the given effect.
     */
    PotionEffect(String effect) {
        //TODO implement
    }

    /**
     * Triggers the PotionEffect.
     */
    String trigger(String noun) {
        return "Event will be implemented soon";    //TODO implement
    }
}

/**
 * An UnlockEvent represents a {@link ZorkEvent} that, when triggered, opens a locked
 * exit in the current room.
 * @author John Thomas
 * @version 1.1
 * 14 July 2020
 */
class UnlockEvent extends ZorkEvent {
    private String dir;
    
    /**
     * Constructs a new UnlockEvent with the given direction.
     * @param dir the direction of the exit to be unlocked
     */
    UnlockEvent(String dir) {
        this.dir = dir;
    }
    
    /**
     * Unlocks the exit corresponding to this direction in the current room.
     * @return this event's message indicating that the exit has been unlocked
     * or that the unlocking was unsuccessful
     */
    String trigger(String noun) {
        try {
            Room currentRoom = GameState.instance().getAdventurersCurrentRoom();
            currentRoom.unlockExit(this.dir);
            this.message = "Exit unlocked.";
        } catch (Exception e) {
            this.message = e.getMessage();
        }
        return this.message;
    }
}
