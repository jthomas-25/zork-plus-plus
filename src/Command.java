/**
 * Command Class - Objects of type Command represent (parsed) commands that the user has typed
 * and wants to invoke
 * @author Richard Volynski
 * @version 2.1
 * 17 June 2020
 * test test
 */



import com.sun.source.tree.BreakTree;

import java.io.IOException;

class Command {

    private String dir;

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

