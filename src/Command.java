import java.util.ArrayList;
import java.util.Iterator;

/**
 * Command Class - Abstract class of Objects of type Command represent (parsed) commands that the user has typed
 * and wants to invoke
 * @author Object Oriented Optimists (OOO)
 * @version 2.7
 * 1 July 2020
 */
abstract class Command {
    abstract String execute();
}

/**
 * TakeCommand - this class extends Command class, implements "take" command, which takes an item(s)
 * from the user's current room and puts it into the user's current inventory
 * @author Richard Volynski
 * @version 1.0
 * 6 July 2020
 */
class TakeCommand extends Command {
    private String itemName;

    /**
     * TakeCommand - default constructor
     * @param itemName
     */
    TakeCommand(String itemName) {
        this.itemName = itemName;
    }

    /**
     * execute() - this method executes "take" command and returns the text that should be printed to the user
     * in response to that command being entered
     * @return text description about which items the user can take
     */
    String execute() {
        GameState state = GameState.instance();
        Room currentRoom = state.getAdventurersCurrentRoom();
        switch (this.itemName) {
            case "":
                return "Take what? (usage: take <item in room>)";
            case "all":
                String result = "";
                ArrayList<Item> currentRoomContents = currentRoom.getContents();
                if (!currentRoomContents.isEmpty()) {
                    Iterator<Item> itr = currentRoomContents.iterator();
                    while (itr.hasNext()) {
                        Item item = itr.next();
                        if (state.inventoryWeightLimitReached(item)) {
                            result += String.format("You tried to take the %s, but your load was too heavy.\n", item);
                        } else {
                            state.addToInventory(item);
                            currentRoom.remove(itr);
                            result += String.format("Took %s from %s.\n", item, currentRoom.getName());
                        }
                    }
                } else {
                    result = String.format("There are no items in %s.", currentRoom.getName());
                }
                return result;
            default:
                Item item = currentRoom.getItemNamed(itemName);
                if (item != null) {
                    if (state.inventoryWeightLimitReached(item)) {
                        return String.format("Your load is too heavy.");
                    } else {
                        state.addToInventory(item);
                        currentRoom.remove(item);
                        return String.format("Took %s from %s.", item, currentRoom.getName());
                    }
                } else {
                    return String.format("%s not found in %s.", itemName, currentRoom.getName());
                }
        }
    }
}

/**
 * DropCommand - this class extends Command class and implements "drop" command, which removes
 * an item(s) from the user's current inventory
 * @author Richard Volynski
 * @version 1.0
 * 6 July 2020
 */
class DropCommand extends Command {
    private String itemName;

    /**
     * DropCommand - default constructor
     * @param itemName - user input
     */
    DropCommand(String itemName) {
        this.itemName = itemName;
    }

    /**
     * execute() - this method executes "drop" command and returns the text that should be printed to the user
     * in response to that command being executed
     * @return text description about which items the user can drop
     */
    String execute() {
        GameState state = GameState.instance();
        switch(this.itemName) {
            case "":
                return "Drop what? (usage: drop <item in inventory>)";
            case "all":
                String result = "";
                ArrayList<Item> inventory = state.getInventory();
                if (!inventory.isEmpty()) {
                    Iterator<Item> itr = inventory.iterator();
                    while (itr.hasNext()) {
                        Item item = itr.next();
                        state.removeFromInventory(itr, item);
                        Room currentRoom = state.getAdventurersCurrentRoom();
                        currentRoom.add(item);
                        result += (item + " dropped.\n");
                    }
                } else {
                    result = "You have no items to drop.";
                }
                return result;
            default:
                try {
                    Item item = state.getItemFromInventoryNamed(this.itemName);
                    state.removeFromInventory(item);
                    Room currentRoom = state.getAdventurersCurrentRoom();
                    currentRoom.add(item);
                    return item + " dropped.";
                } catch (NoItemException e) {
                    return e.getMessage();
                }
        }
    }
}


/**
 * MovementCommand - this class extends Command class and implements movement command(s), which moves the user into
 * the direction they entered, if a valid command was entered
 * @author OOO
 * @version 1.0
 * 6 July 2020
 */
class MovementCommand extends Command {
    private String dir;

    /**
     * MovementCommand - For now, this constructor takes a valid move command entered by the user and stores it
     * as an instance variable
     * @param dir - user inputted direction, including save
     */
    MovementCommand(String dir) {
        this.dir = dir;
    }

    /**
     * execute() - this method executes a movement command and returns the text that should be
     * printed to the user in response to that command being executed
     * @return text description where the user is going
     */
    String execute() {
        Room room = GameState.instance().getAdventurersCurrentRoom().leaveBy(dir);
        GameState.instance().setAdventurersCurrentRoom(room);
        String execute = GameState.instance().getAdventurersCurrentRoom().describe();
        return execute;
    }
}


/**
 * SaveCommand - this class extends Command class and implements a "save" command, which saves the
 * game at its current state and prompts the user to enter a file where the game data will be saved
 * @author OOO
 * @version 1.0
 * 6 July 2020
 */
class SaveCommand extends Command {
    private String saveFilename;

    /**
     * SaveCommand - default constructor
     * @param saveFilename - user input
     */
    SaveCommand(String saveFilename) {
        this.saveFilename = saveFilename;
    }

    /**
     * execute() - this method executes "save" and returns the text that should be printed to the user
     * in response to that command being entered
     * @return String
     */
    String execute() {
        try {
            GameState.instance().store(this.saveFilename);
            return "Game saved successfully.";
        } catch (IllegalSaveFormatException e) {
            return e.getMessage();
        }
    }
}


/**
 * UnknownCommand - this class extends Command class, implements an unknown command, and returns a
 * message to the user in response
 * to that command being entered
 * @author OOO
 * @version 1.0
 * 6 July 2020
 */
class UnknownCommand extends Command {
    private String bogusCommand;

    /**
     * UnknownCommand - default constructor
     * @param bogusCommand
     */
    UnknownCommand(String bogusCommand) {
        this.bogusCommand = bogusCommand;
    }

    /**
     * execute() - this method executes an unknown command and returns the text that should be printed
     * to the user in response to that command being entered
     * @return message
     */
    String execute() {
        return String.format("Sorry, I don't understand '%s'.", bogusCommand);
    }
}

/**
 * QuitCommand - this class extends Command class, implements the quit/q command and ends the
 * game without saving user data
 * @author OOO
 * @version 1.0
 * 6 July 2020
 */
class QuitCommand extends Command {

    /**
     * execute - this method executes the quit/q command and ends the program
     * @return null
     */
    String execute() {
        return null;
    }
}

/**
 * InventoryCommand - this class extends Command class, implements the inventory/i command, and
 * prints a message in response to that command being entered
 * @author OOO
 * @version 1.0
 * 6 July 2020
 */
class InventoryCommand extends Command {

    /**
     * InventoryCommand - default constructor
     */
    InventoryCommand() {
    }

    /**
     * execute - this method executes the inventory/i command and returns the user's current inventory
     * @return result
     */
    String execute() {
        ArrayList<Item> inventory = GameState.instance().getInventory();
        if (inventory.isEmpty()) {
            return "You have no items.";
        } else {
            String result = "You are carrying:\n";
            for (Item item : inventory) {
                result += ("  " + item + "\n");
            }
            return result;
        }
    }
}


/**
 * ItemSpecificCommand - this class extends Command class, implements a(n) item-specific command,
 * and prints a message for certain items
 * @author OOO
 * @version 1.0
 * 6 July 2020
 */
class ItemSpecificCommand extends Command {
    private String verb;
    private String noun;

    /**
     * ItemSpecificCommand - default constructor
     * @param verb
     * @param noun
     */
    ItemSpecificCommand(String verb, String noun) {
        this.noun = noun;
        this.verb = verb;
    }

    /**
     * execute - this method executes the item-specific commands and prints a message about a certain item
     * @return String message
     */
    String execute() {
        try {
            Item i = GameState.instance().getItemInVicinityNamed(this.noun);
            if (i.getMessageForVerb(this.verb) != null) {
                return i.getMessageForVerb(this.verb);
            } else {
                return String.format("You cannot '%s' the %s.", verb, i);
            }
        } catch (NoItemException e) {
            return e.getMessage();
        }
    }
}

/**
 * LookCommand - this class extends Command class, implements the "look" command, returns a message
 * containing a description of the user's current room, surrounding rooms, and surrounding exits
 * @author Richard Volynski
 * @version 1.0
 * 6 July 2020
 */
class LookCommand extends Command {

    /**
     * LookCommand - default constructor
     */
    LookCommand() {
    }

    /**
     * execute - this method executes the "look" command and returns the description of the user's current room
     * and nearest exits
     * @return execute
     */
    String execute() {
        GameState.instance().getAdventurersCurrentRoom().setRoomDescriptionNeeded();
        String execute = GameState.instance().getAdventurersCurrentRoom().describe();
        return execute;
    }
}

/**
 * ScoreCommand - this class extends Command class, returns current score, and user's rank
 * @author Richard Volynski
 * @version 1.0
 * 6 July 2020
 */
class ScoreCommand extends Command {

    /**
     * ScoreCommand - default constructor
     */
    ScoreCommand() {
    }

    /**
     * execute - this method executes "score" command, returns a message with accumulated points, and current rank
     * @return user score and current rank
     */
    String execute() {
        GameState state = GameState.instance();
        String scoreMsg = "You have accumulated " + state.getScore() + " points. ";
        if (state.rankAssigned()) {
            scoreMsg += "This gives you a rank of " + state.getRank() + ".";
        } else {
            scoreMsg += "You don't have a rank assigned.";
        }
        return scoreMsg;
    }
}


/**
 * HealthCommand - this class extends Command class and implements "health" command, which returns user's
 * current health as a String message
 * @author Richard Volynski
 * @version 1.0
 * 6 July 2020
 */
class HealthCommand extends Command {

    /**
     * HealthCommand - default constructor
     */
    HealthCommand() {
    }

    /**
     * execute - this method executes health command and returns current user's health as a message
     * @return string message
     */
    String execute() {
        GameState state = GameState.instance();
        String healthMsg;
        if (state.healthInfoAvailable()) {
            healthMsg = state.getHealthMsg();
        } else {
            healthMsg = "No health info available";
        }
        return healthMsg;
    }
}


/**
 * SwapCommand - this class extends Command class, implements swap command, which exchanges all items in the user's
 * current room with the user's current inventory (in both directions, e.g. items from Room B6 become items in the
 * user's current inventory, and items in the user's current inventory become items in Room B6)
 * @author Richard Volynski
 * @version 1.0
 * 6 July 2020
 */
class SwapCommand extends Command {

    /**
     * SwapCommand - default constructor
     */
    SwapCommand() {
        //TODO implement
    }

    /**
     * execute() - this method will swap items in the user's current room with the user's current inventory
     * @param //TODO implement
     * @return swap command message
     */
    String execute() {
        return null;    //TODO implement
    }
}

/**
 * KillCommand - this class extends Command class, implements kill command, which so far,
 * would kill a snake if it appeared in the room the user is currently in. If the user possesses a dagger
 * or sword, they will type “kill snake with sword” or “kill snake with dagger.” Once the user kills the snake,
 * the snake will be removed from the room.
 * @author Richard Volynski
 * @version 1.0
 * 6 July 2020
 */
class KillCommand extends Command {

    /**
     * KillCommand - default constructor
     */
    KillCommand() {
        //TODO implement
    }

    /**
     * execute() - this method will check if user has a dagger or sword (whatever was supplied). If yes,
     * remove the snake from the current room.
     * @return //TODO implement
     */
    String execute() {
        return null;    //TODO implement
    }
}




