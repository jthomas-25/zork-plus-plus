/**
 * Command Class - Objects of type Command represent (parsed) commands that the user has typed
 * and wants to invoke
 * @author Richard Volynski
 * @version 2.3
 * 21 June 2020
 */



import com.sun.source.tree.BreakTree;

import java.io.IOException;
import java.util.ArrayList;



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
            GameState.instance().store("Richard_state.sav");
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
    TakeCommand(String itemName) { this.itemName = itemName;}
    String execute() {
        Room currentRoom = GameState.instance().getAdventurersCurrentRoom();
        if (currentRoom.getItemNamed(itemName) != null) {
            Item item = currentRoom.getItemNamed(itemName);
            GameState.instance().addToInventory(item);
            currentRoom.remove(item);
            return String.format("Took %s from %s", itemName, currentRoom.getName());
        } else {
            return String.format("%s not found in %s", itemName, currentRoom.getName());
        }
    }
}

class DropCommand extends Command {
    private String itemName;
    
    DropCommand(String itemName) {
        this.itemName = itemName;
    }

    String execute() {
        if (this.itemName.equals("")) {
            return "Drop what?";
        } else if (this.itemName.equals("all")) {
            String result = "";
            ArrayList<Item> inventory = GameState.instance().getInventory();
            for (Item item : inventory) {
                GameState.instance().removeFromInventory(item);
                Room currentRoom = GameState.instance().getAdventurersCurrentRoom();
                currentRoom.add(item);
                result += (item.getPrimaryName() + " dropped.\n");
            }
            return result;
        } else {
//            try {
                Item item = GameState.instance().getItemFromInventoryNamed(this.itemName);
                GameState.instance().removeFromInventory(item);
                Room currentRoom = GameState.instance().getAdventurersCurrentRoom();
                currentRoom.add(item);
                return this.itemName + " dropped.";
//            }
//             catch (NoItemException e) {
//                return e.getMessage();
//            }
        }
/*
    //Alternate version of the if-else code if you prefer switch statements instead
        String result = "";
        switch (this.itemName) {
            case "":
                result = "Drop what?";
                break;
            case "all":
                ArrayList<Item> inventory = GameState.instance().getInventory();
                for (Item item : inventory) {
                    GameState.instance().removeFromInventory(item);
                    Room currentRoom = GameState.instance().getAdventurersCurrentRoom();
                    currentRoom.add(item);
                    result += (item.getPrimaryName() + " dropped.\n";
                }
                break;
            default:
                try {
                    Item item = GameState.instance().getItemFromInventoryNamed(this.itemName);
                    GameState.instance().removeFromInventory(item);
                    Room currentRoom = GameState.instance().getAdventurersCurrentRoom();
                    currentRoom.add(item);
                    result = this.itemName + " dropped.";
                } catch (NoItemException e) {
                    result = e.getMessage();
                }
                break;
        }
        return result;
*/
    }
}

class MovementCommand extends Command {

    private String dir;
    MovementCommand(String dir) {
        super(dir); //TODO implement
    }
    String execute() throws IllegalSaveFormatException {
        return super.execute();    //TODO implement
    }
}

class SaveCommand extends Command {

    private String saveFileName;
    SaveCommand() {

    }

    String execute() throws IllegalSaveFormatException {
        GameState.instance().store("Richard_state.sav");
        return null;
    }
}

class UnknownCommand extends Command {

    private String bogusCommand;
    UnknownCommand(String dir) {
        super(dir); //TODO implement
    }
    String execute() {
        return null;    //TODO implement
    }
}

class InventoryCommand extends Command {
    InventoryCommand(String inventory) {
        //TODO implement inventory
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
    ItemSpecificCommand(String dir) {
        super(dir); //TODO implement
    }
    String execute() {
        return null;    //TODO implement
    }
}

class LookCommand extends Command {

    LookCommand() {
        //TODO implement
    }
    String execute() {
        GameState.instance().getAdventurersCurrentRoom().setRoomDescriptionNeeded();
        String execute = GameState.instance().getAdventurersCurrentRoom().describe();
        return execute;
    }
}


