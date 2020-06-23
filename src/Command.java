/**
 * Command Class - Objects of type Command represent (parsed) commands that the user has typed
 * and wants to invoke
 * @author Richard Volynski
 * @version 2.5
 * 23 June 2020
 */

import com.sun.source.tree.BreakTree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


abstract class Command {
    abstract String execute();
}

class TakeCommand extends Command {

    private String itemName;
    TakeCommand(String itemName) { this.itemName = itemName; }
    String execute() {
        Room currentRoom = GameState.instance().getAdventurersCurrentRoom();
        switch (this.itemName) {
            case "":
                return "Take what?";
            case "all":
                String result = "";
                ArrayList<Item> currentRoomContents = currentRoom.getContents();
                if (!currentRoomContents.isEmpty()) {
                    Iterator<Item> itr = currentRoomContents.iterator();
                    while (itr.hasNext()) {
                        Item item = itr.next();
                        GameState.instance().addToInventory(item);
                        currentRoom.remove(itr);
                        result += String.format("Took %s from %s.\n", item.getPrimaryName(), currentRoom.getName());
                    }
                } else {
                    result = String.format("There are no items in %s.", currentRoom.getName());
                }
                return result;
            default:
                Item item = currentRoom.getItemNamed(itemName);
                if (item != null) {
                    GameState.instance().addToInventory(item);
                    currentRoom.remove(item);
                    return String.format("Took %s from %s.", item.getPrimaryName(), currentRoom.getName());
                } else {
                    return String.format("%s not found in %s.", itemName, currentRoom.getName());
                }
        }
    }
}

class DropCommand extends Command {
    private String itemName;
    
    DropCommand(String itemName) {
        this.itemName = itemName;
    }

    String execute() {
        switch(this.itemName) {
            case "":
                return "Drop what?";
            case "all":
                String result = "";
                ArrayList<Item> inventory = GameState.instance().getInventory();
                if (!inventory.isEmpty()) {
                    Iterator<Item> itr = inventory.iterator();
                    while (itr.hasNext()) {
                        Item item = itr.next();
                        GameState.instance().removeFromInventory(itr);
                        Room currentRoom = GameState.instance().getAdventurersCurrentRoom();
                        currentRoom.add(item);
                        result += (item.getPrimaryName() + " dropped.\n");
                    }
                } else {
                    result = "You have no items to drop.";
                }
                return result;
            default:
                try {
                    Item item = GameState.instance().getItemFromInventoryNamed(this.itemName);
                    GameState.instance().removeFromInventory(item);
                    Room currentRoom = GameState.instance().getAdventurersCurrentRoom();
                    currentRoom.add(item);
                    return item.getPrimaryName() + " dropped.";
                } catch (NoItemException e) {
                    return e.getMessage();
                }
        }
/*
        //Alternate if-else version of above switch statement
        if (this.itemName.equals("")) {
            return "Drop what?";
        } else if (this.itemName.equals("all")) {
            String result = "";
            ArrayList<Item> inventory = GameState.instance().getInventory();
            if (!inventory.isEmpty()) {
                Iterator<Item> itr = inventory.iterator();
                while (itr.hasNext()) {
                    Item item = itr.next();
                    GameState.instance().removeFromInventory(itr);
                    Room currentRoom = GameState.instance().getAdventurersCurrentRoom();
                    currentRoom.add(item);
                    result += (item.getPrimaryName() + " dropped.\n");
                }
            } else {
                result = "You have no items to drop.\n";
            }
            return result;
        } else {
            try {
                Item item = GameState.instance().getItemFromInventoryNamed(this.itemName);
                GameState.instance().removeFromInventory(item);
                Room currentRoom = GameState.instance().getAdventurersCurrentRoom();
                currentRoom.add(item);
                return item.getPrimaryName() + " dropped.\n";
            } catch (NoItemException e) {
                return e.getMessage();
            }
        }
*/
    }
}

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
     * execute() - this method executes command and returns the text that should be printed to the user
     * in response to that command being executed
     * @return text description where the user is going
     */
    String execute() {
        Room room = GameState.instance().getAdventurersCurrentRoom().leaveBy(dir);
        GameState.instance().setAdventurersCurrentRoom(room);
        String execute = GameState.instance().getAdventurersCurrentRoom().describe();
        return execute;
    }
}

class SaveCommand extends Command {

    private String saveFilename;
    SaveCommand(String saveFilename) {
        this.saveFilename = saveFilename;
    }

    String execute() {
        try {
            GameState.instance().store(this.saveFilename);
            return "Game saved successfully.";
        } catch (IllegalSaveFormatException e) {
            return e.getMessage(); 
        }
    }
}

class UnknownCommand extends Command {

    private String bogusCommand;
    UnknownCommand(String bogusCommand) {
        this.bogusCommand = bogusCommand;
    }

    String execute() {
        return null;    //TODO implement
    }
}

class InventoryCommand extends Command {
    InventoryCommand() {
    }

    String execute() {
        ArrayList<Item> inventory = GameState.instance().getInventory();
        if (inventory.isEmpty()) {
            return "You have no items.";
        } else {
            String result = "You are carrying:\n";
            for (Item item : inventory) {
                result += ("  " + item.getPrimaryName() + "\n");
            }
            return result;
        }
    }
}

class ItemSpecificCommand extends Command {

    private String verb;
    private String noun;
    ItemSpecificCommand(String verb, String noun) {
        this.noun = noun;
        this.verb = verb;
    }

    String execute() {

        for (Item i : GameState.instance().getInventory()) {
            if (i.getPrimaryName().equals(this.noun)) {
                return i.getMessageForVerb(this.verb);
            } else {
                return "Couldn't find " + this.noun;
            }
        }

        return "???";
    }
}

class LookCommand extends Command {

    LookCommand() {
    }

    String execute() {
        GameState.instance().getAdventurersCurrentRoom().setRoomDescriptionNeeded();
        String execute = GameState.instance().getAdventurersCurrentRoom().describe();
        return execute;
    }
}
