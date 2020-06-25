/**
 * Command Class - Objects of type Command represent (parsed) commands that the user has typed
 * and wants to invoke
 * @author Object Oriented Optimists
 * @version 2.6
 * 25 June 2020
 */


import com.sun.source.tree.BreakTree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;


class Command {

    private String dir;

    Command () {
    }

    /**
     * Command - For now, this constructor takes a valid move command entered by the user and stores it
     * as an instance variable
     * @param dir - user inputted direction, including save
     */
    Command (String dir) {
        this.dir = dir;
    }

    /**
     * execute() - this method executes Command and returns the text that should be printed to the user
     * in response to that command being executed
     * @return text description where the user is going
     */
    String execute() throws IllegalSaveFormatException {

        if (dir.toLowerCase().equals("save")) {
            GameState.instance().store("ZorkIII_OOO_state.sav");
            return null;
        }
        else {
            Room room = GameState.instance().getAdventurersCurrentRoom().leaveBy(dir);
            GameState.instance().setAdventurersCurrentRoom(room);
            String execute = GameState.instance().getAdventurersCurrentRoom().describe();
            return execute;
        }
    }
}

class TakeCommand extends Command {
    private String itemName;

    TakeCommand(String itemName) {
        if (itemName.isEmpty()) {
            Scanner stdin = new Scanner(System.in);
            System.out.print("Take what item? ");
            itemName = stdin.nextLine();
//            stdin.close();
        }

        this.itemName = itemName;
    }

    String execute() {
        GameState state = GameState.instance();
        Room currentRoom = state.getAdventurersCurrentRoom();

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
                    return String.format("%s not found in %s", itemName, currentRoom.getName());
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
        GameState state = GameState.instance();
        switch(this.itemName) {
            case "":
                return "Drop what?";
            case "all":
                String result = "";
                ArrayList<Item> inventory = state.getInventory();
                if (!inventory.isEmpty()) {
                    Iterator<Item> itr = inventory.iterator();
                    while (itr.hasNext()) {
                        Item item = itr.next();
                        state.removeFromInventory(itr,item);
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
                }
                catch (NoItemException e) {
                    return e.getMessage();
                }
        }
    }
}

class MovementCommand extends Command {
    private String dir;

    /**
     * MovementCommand - For now, this constructor takes a valid move command entered by the user and stores it
     * as an instance variable
     *
     * @param dir - user inputted direction, including save
     */
    MovementCommand(String dir) {
        this.dir = dir;
    }

    /**
     * execute() - this method executes command and returns the text that should be printed to the user
     * in response to that command being executed
     *
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
        private String saveFileName;

        SaveCommand(String saveFileName) {
            this.saveFileName = saveFileName;
        }

        String execute() {
            try {
                GameState.instance().store(this.saveFileName);
                return "Game saved successfully.";
            }
            catch (IllegalSaveFormatException e) {
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
        return null;
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
                result += ("  " + item + "\n");
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
                return null;
            }
        }
        return null;
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


