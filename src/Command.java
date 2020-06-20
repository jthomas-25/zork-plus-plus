/**
 * Command Class - Objects of type Command represent (parsed) commands that the user has typed
 * and wants to invoke
 * @author Richard Volynski
 * @version 2.2
 * 20 June 2020
 */



import com.sun.source.tree.BreakTree;

import java.io.IOException;

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
    TakeCommand() {
        //TODO implement
    }
    String execute() {
        return null;    //TODO implement
    }
}

class DropCommand extends Command {

    private String itemName;
    DropCommand() {
        //TODO implement
    }
    String execute() {
        return null;    //TODO implement
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
        //TODO implement
    }
    String execute() {
        return null;    //TODO implement
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
    InventoryCommand(String dir) {
        super(dir);
    }
    String execute() {
        return null;    //TODO implement
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

