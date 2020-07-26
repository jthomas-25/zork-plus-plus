//package com.company;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Command Class - Abstract class of Objects of type Command represent commands (parsed) that the user has typed
 * and wants to invoke: for instance, "take", "drop", "i/inventory". Each command has a subclass, which extends
 * this abstract Command class.
 * @author Object Oriented Optimists (OOO)
 * @author Richard Volynski
 * @version 3.4
 * 21 July 2020
 */
abstract class Command {
    
    /**
     * execute - this is an abstract command, which will be implemented at the subclass level.
     * @return String command message
     */
    abstract String execute() throws InterruptedException;
}

/**
 * TakeCommand - this class extends Command class, implements "take" command, which takes an item(s)
 * from the user's current room and puts it into the user's current inventory
 * @author Object Oriented Optimists (OOO)
 * @author Richard Volynski
 * @version 1.0
 * 6 July 2020
 */
class TakeCommand extends Command {
    private String itemName;

    /**
     * TakeCommand - default constructor
     * @param itemName item to take from a room and add to user's inventory
     */
    TakeCommand(String itemName) {
        this.itemName = itemName;
    }

    /**
     * execute - this method executes "take" command and returns the text that should be printed to the user
     * in response to this command being entered
     * @return text description about which items the user took from a room
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
                            result += String.format("Took %s from %s.\n", item, currentRoom);
                        }
                    }
                }
                else {
                    result = String.format("There are no items in %s.", currentRoom);
                }
                return result;
            default:
                Item item = currentRoom.getItemNamed(itemName);
                if (item != null) {
                    if (state.inventoryWeightLimitReached(item)) {
                        return String.format("Your load is too heavy");
                    }
                    else {
                        state.addToInventory(item);
                        currentRoom.remove(item);
                        return String.format("Took %s from %s.", item, currentRoom);
                    }
                }
                else if (GameState.instance().itemExistsInInventory(itemName)) {
                    return String.format("You already took %s", itemName);
                }
                else {
                    return String.format("%s not found in %s.", itemName, currentRoom);
                }
        }
    }
}

/**
 * DropCommand - this class extends Command class and implements "drop" command, which removes
 * an item(s) from the user's current inventory
 * @author Object Oriented Optimists (OOO)
 * @author Richard Volynski
 * @version 1.0
 * 6 July 2020
 */
class DropCommand extends Command {
    private String itemName;

    /**
     * DropCommand - default constructor
     * @param itemName - item name to drop (user input)
     */
    DropCommand(String itemName) {
        this.itemName = itemName;
    }

    /**
     * execute() - this method executes "drop" command, removes items from user's inventory
     * and returns the text that should be printed to the user in response to that command being executed
     * @return text description about which items were removed from user's inventory
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
 * MovementCommand - this class extends Command class and implements movement command(s), which moves the user
 * into/towards the direction they entered, if a valid command was entered
 * @author Object Oriented Optimists (OOO)
 * @author Richard Volynski
 * @version 1.1
 * 19 July 2020
 */
class MovementCommand extends Command {
    private String dir;

    /**
     * MovementCommand - For now, this constructor takes a valid move command entered by the user and stores it
     * as an instance variable
     * @param dir - direction (user input)
     */
    MovementCommand(String dir) {
        this.dir = dir;
    }

    /**
     * execute() - this method executes a movement command, moves the user into/towards the direction
     * they entered, and returns the text that should be printed to the user
     * in response to that command being executed
     * @return text description about where the user is going
     */
    String execute() throws InterruptedException {
        try {
            boolean daggerDropped = false;
            Room room = GameState.instance().getAdventurersCurrentRoom().leaveBy(dir);
            GameState.instance().setAdventurersCurrentRoom(room);
            String execute = GameState.instance().getAdventurersCurrentRoom().describe();
            if (GameState.instance().itemExistsInInventory("dagger") && GameState.instance().toDropOrNot()) {
                DropCommand dropCommand = new DropCommand("dagger");
                dropCommand.execute();
                daggerDropped = true;
            }
            if (daggerDropped) {
                execute+= "\n\nHotel guests stole some of your valuables";
            }


            // Should really be in a gamestate counter or something.
            if (GameState.instance().getNpcs().size() > 0) {
                NPC onlyNPC = GameState.instance().getNpcs().get(0);
                if ((int)(Math.random() * (5 + 1) + 0) == 1) {
                    onlyNPC.moveTowards(GameState.instance().getAdventurersCurrentRoom().getName());
                }
                if (onlyNPC.inSameRoom()) {
                    String npcText = "";
//                    npcText += new PotionEffect("gib", execute).trigger("none");
                    npcText += onlyNPC.sameRoom();


                    return npcText;
                }
                return execute += "\n\n" + onlyNPC.getLogistics("evil presence");
            }


            return execute;
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }
}


/**
 * SaveCommand - this class extends Command class and implements a "save" command, which saves the
 * game vitals at its current state and prompts the user to enter a file name where the game data will be saved
 * @author Object Oriented Optimists (OOO)
 * @author Richard Volynski
 * @version 1.1
 * 7 July 2020
 */
class SaveCommand extends Command {
    private String saveFilename;

    /**
     * SaveCommand - default constructor
     * @param saveFilename - file name (user input)
     */
    SaveCommand(String saveFilename) {
        this.saveFilename = saveFilename;
    }

    /**
     * execute() - this method executes "save" and returns text that should be printed to the user
     * in response to that command being entered or returns an error message if an exception happened when the user
     * tried to save the game.
     * @return message
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
 * UnknownCommand - this class extends Command class, implements an unknown command, and returns an
 * error message to the user in response to that command being entered
 * @author Object Oriented Optimists (OOO)
 * @author Richard Volynski
 * @version 1.1
 * 7 July 2020
 */
class UnknownCommand extends Command {
    private String bogusCommand;

    /**
     * UnknownCommand - default constructor
     * @param bogusCommand - unknown command
     */
    UnknownCommand(String bogusCommand) {
        this.bogusCommand = bogusCommand;
    }

    /**
     * execute() - this method executes an unknown command and returns an error message (String)
     * in response to that command being entered
     * @return message
     */
    String execute() {
        return String.format("Sorry, I don't understand '%s'.", bogusCommand);
    }
}

/**
 * QuitCommand - this class extends Command class, implements the quit/q command and ends the
 * game without saving user data
 * @author Object Oriented Optimists (OOO)
 * @author Richard Volynski
 * @version 1.1
 * 15 July 2020
 */
class QuitCommand extends Command {

    /**
     * Default constructor
     */
    QuitCommand() {
    }

    /**
     * execute - this method executes the quit/q command and ends the program
     * @return null
     */
    String execute() {
        System.exit(1);
        return null;
    }
}

/**
 * InventoryCommand - this class extends Command class, implements the inventory/i command, and
 * prints the user's current items or prints a message that user has no items
 * @author Object Oriented Optimists (OOO)
 * @author Richard Volynski
 * @version 1.1
 * 7 July 2020
 */
class InventoryCommand extends Command {

    /**
     * InventoryCommand - default constructor
     */
    InventoryCommand() {
    }

    /**
     * execute - this method executes the inventory/i command and returns the user's current inventory
     * @return inventory items
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
 * and prints a message for certain items or an error message
 * @author Object Oriented Optimists (OOO)
 * @author Richard Volynski
 * @version 1.3
 * 15 July 2020
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
        this.verb = verb;
        this.noun = noun;
    }

    /**
     * execute - this method executes the item-specific commands and prints a message about a certain item
     * or prints an error message
     * @return String message
     */
    String execute() {
        switch (this.noun) {
            case "":
                return String.format("%s what? (usage: <verb> <noun>)", this.verb, this.noun);
            default:
                String returnMessage = "";
                if (this.verb.equalsIgnoreCase("steal") && (this.noun.equalsIgnoreCase("wallet")
                        || this.noun.equalsIgnoreCase("StaffWallet")) && GameState.instance().isGuardAlive()) {
                    returnMessage = String.format("You cannot %s the %s because a guard is protecting it!", this.verb, this.noun);
                    return returnMessage;
                }
                else {
                    try {
                        Item item = GameState.instance().getItemInVicinityNamed(this.noun);
                        String message = item.getMessageForVerb(this.verb);
                        if (message != null) {
                            returnMessage = message;
                            String[] eventStrings = item.getEventStrings(this.verb);
                            boolean verbHasEvents = eventStrings != null;
                            if (verbHasEvents) {
                                for (String eventString : eventStrings) {
                                    switch (eventString) {
                                        case "Drop":
                                        case "Disappear":
                                            eventString = String.format("%s(%s)", eventString, item);
                                            break;
                                    }
                                    ZorkEvent event = EventFactory.instance().parse(eventString);
                                    if (event != null) {
                                        String eventMessage = event.trigger(noun);
                                        if (eventMessage != null) {
                                            returnMessage += String.format("\n%s", eventMessage);
                                        }
                                    }
                                }
                            }
                            else if (this.verb.equals("take") || this.verb.equals("drop") && (this.noun.equals("guard"))) {
                                returnMessage = String.format("You cannot '%s' the %s. That would be " +
                                        "stupid.", this.verb, this.noun);
                            }
                        }
                        else {
                            returnMessage = String.format("You cannot '%s' the %s.", this.verb, this.noun);
                        }
                        return returnMessage;
                    }
                    catch (NoItemException e) {
                        return e.getMessage();
                    }
                }
        }
    }
}

/**
 * LookCommand - this class extends Command class, implements the "look" command, returns a message
 * containing a description of the user's current room, surrounding rooms, and surrounding exits
 * @author Object Oriented Optimists (OOO)
 * @author Richard Volynski
 * @version 1.1
 * 7 July 2020
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
     * @return room description and nearest exits
     */
    String execute() {
        GameState.instance().getAdventurersCurrentRoom().setRoomDescriptionNeeded();
        String execute = GameState.instance().getAdventurersCurrentRoom().describe();
        return execute;
    }
}

/**
 * ScoreCommand - this class extends Command class, returns current score, and user's rank
 * @author Object Oriented Optimists (OOO)
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
        int currentScore = state.getScore();
        String scoreMsg = "You have accumulated " + currentScore + " " + (currentScore == 1 ? "point" : "points") + ". ";
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
 * @author Object Oriented Optimists (OOO)
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
     * @return health message
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
 * @author Object Oriented Optimists (OOO)
 * @author Richard Volynski
 * @version 1.1
 * 13 July 2020
 */
class SwapCommand extends Command {
    private String userItemName;    //item in user's inventory
    private String itemInRoomName;

    /**
     * SwapCommand - default constructor
     */
    SwapCommand(String userItemName, String itemInRoomName) {
        this.userItemName = userItemName;
        this.itemInRoomName = itemInRoomName;
    }

    /**
     * execute() - this method will swap items in the user's current room with the user's current inventory
     * @return swap command message
     */
    String execute() {

        GameState state = GameState.instance();
        Room currentRoom = state.getAdventurersCurrentRoom();
        switch (userItemName) {
            case "":
                return "Swap what? (usage: swap <user item with room item>)";
            case "all":

                int numItemsInUserInventory = GameState.instance().getInventory().size();

                String result = "";
                ArrayList<Item> currentRoomContents = currentRoom.getContents();
                if (!currentRoomContents.isEmpty()) {
                    Iterator<Item> itr = currentRoomContents.iterator();
                    while (itr.hasNext()) {
                        Item roomItem = itr.next();
                        state.addToInventory(roomItem);
                    }
                    while (itr.hasNext()) {
                        Item roomItem = itr.next();
                        currentRoom.remove(roomItem);
                    }
                    for (int i = 0; i < numItemsInUserInventory; i++) {
                        Item itemInInventory = GameState.instance().getInventory().get(i);
                        currentRoom.add(itemInInventory);
                    }
                    for (int i = numItemsInUserInventory-1; i >= 0; i--) {
                        Item itemInInventory = GameState.instance().getInventory().get(i);
                        GameState.instance().removeFromInventory(itemInInventory);
                    }
                    if (userItemName.equalsIgnoreCase("all")) {
                        result += String.format("Swapped all items");
                    }
                    else {
                        result += String.format("Swapped %s with %s", userItemName, itemInRoomName);
                    }
                }
                else {
                    result = String.format("There are no items in %s.", currentRoom);
                }
                return result;
            default:
                Item userItem;
                Item itemInRoom;
                try {
                    userItem = GameState.instance().getItemFromInventoryNamed(userItemName);
                    itemInRoom = GameState.instance().getAdventurersCurrentRoom().getItemNamed(itemInRoomName);
                }
                catch (NoItemException e) {
                    result = String.format("No item %s found in user's inventory", userItemName);
                    return result;
                }
                state.removeFromInventory(userItem);
                state.addToInventory(itemInRoom);
                currentRoom.remove(itemInRoom);
                currentRoom.add(userItem);
                if (userItemName.equalsIgnoreCase("all")) {
                    return String.format("Swapped all items");
                }
                else {
                    return String.format("Swapped %s with %s.", userItemName, itemInRoomName);
                }
        }
    }
}

/**
 * KillCommand - this class extends Command class, implements kill command, which
 * would kill the security guard guarding a safe with a staff member's wallet.
 * If the user possesses a dagger or sword, they will type “kill guard.”
 * Once the user kills the guard, the guard will be removed from the room and the
 * user will have to take the wallet from the safe.
 * @author Richard Volynski
 * @version 1.2
 * 20 July 2020
 */
class KillCommand extends Command {
    private String itemName;
    /**
     * KillCommand - constructor
     */
    KillCommand(String itemName) {
        this.itemName = itemName;
    }

    /**
     * execute() - this method will check if user has a dagger or sword (whatever was supplied). If yes,
     * remove the snake from the current room.
     * @return string
     */
    String execute() {
        if (GameState.instance().getAdventurersCurrentRoom().hasItemNamed("guard")) {
            if (GameState.instance().itemExistsInInventory("dagger") ||
                    GameState.instance().itemExistsInInventory("sword")) {
                GameState.instance().getAdventurersCurrentRoom().removeItem("guard");
                GameState.instance().setGuardAlive(false);
                return String.format("%s killed in %s", itemName.replace(itemName.charAt(0), itemName.toUpperCase().charAt(0)),
                        GameState.instance().getAdventurersCurrentRoom());
            }
            else {
                return "You don't have a dagger or sword to kill the guard";
            }
        }
        else {
           return "There is nothing to kill in this room.";
        }
    }
}

/**
 * 
 * @author John Thomas
 * @version 1.3
 * 20 July 2020
 */
class UnlockCommand extends Command {
    private String dir;
    private String nameOfObjectToUnlock;
    private String prep;
    private String itemName;

    /**
     * Constructs a new UnlockCommand.
     * @param dir if the name of the object is "exit", the direction of the exit to be unlocked
     * @param nameOfObjectToUnlock the name of the object to be unlocked
     * @param prep a preposition to link the object and item name
     * @param itemName the name of the item required to unlock this object
     */
    UnlockCommand(String dir, String nameOfObjectToUnlock, String prep, String itemName) {
        this.dir = dir;
        this.nameOfObjectToUnlock = nameOfObjectToUnlock;
        this.prep = prep;
        this.itemName = itemName;
    }

    String execute() {
        switch (this.nameOfObjectToUnlock) {
            case "":
                return "Unlock what? (usage: unlock <noun>)";
            case "exit":
                if (this.dir.equals("")) {// && !this.prep.equals("with")) {
                    return "Unlock which exit? (usage: unlock <direction> exit)";
                }
                Room currentRoom = GameState.instance().getAdventurersCurrentRoom();
                if (currentRoom.hasNoExits()) {
                    return "This room has no exits.";
                }
                Exit exit = currentRoom.getExit(this.dir);
                if (exit == null) {
                    return "No exit in that direction.";
                }
                if (exit.isLocked()) {
                    if (this.itemName.equals("")) {
                        return "What do you want to unlock this exit with? (usage: unlock <direction> exit with <noun>)";
                    } else {
                        ZorkEvent event = EventFactory.instance().parse(String.format("Unlock(%s,%s)", this.dir, this.itemName));
                        return event.trigger("");
                    }
                } else {
                    return "Already unlocked.";
                }
            default:
                return String.format("Can't unlock %s.", this.nameOfObjectToUnlock);
        }
    }
}
