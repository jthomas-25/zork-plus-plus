/**
 * Command Class - Objects of type Command represent (parsed) commands that the user has typed
 * and wants to invoke
 * @author Richard Volynski
 * @version 1.3
 * 4 June 2020
 */



class Command {

    private String dir;


    Command (String dir) {
        this.dir = dir;
    }




    String execute() {
        Room room = GameState.instance().getAdventurersCurrentRoom().leaveBy(dir);
        GameState.instance().setAdventurersCurrentRoom(room);
        String executeFeet = GameState.instance().getAdventurersCurrentRoom().describe();
        return executeFeet;
    }
}

