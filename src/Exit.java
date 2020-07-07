import java.util.Scanner;

/**
 * Exit Class is designed to be a link between rooms, allowing the user to travel from one room
 * to another room in a specific direction.
 * Exit Class contains the user's current room (src, Room object), direction (e.g. "w", "e"), and
 * room destination (dest, Room object), where current direction (user input) leads to.
 * An exit can also describe itself, which generates and returns a String message, telling the user
 * which direction they can go and the name of the surrounding room(s). Also, Exit class initializes itself by
 * reading from the .zork file.
 * @author Richard Volynski (OOO)
 * @version 2.7
 * 7 July 2020
 */
class Exit {
    private String dir;
    private Room src;
    private Room dest;
    
    /**
     * Exit - this constructor initializes itself, reading lines from .zork file using a scanner, which is provided
     * as a parameter. It also uses Dungeon, provided as a parameter, to find a room by room name.
     * This constructor throws an exception (NoExitException) if a line does
     * contain "===" (indicating end of exits in the .zork file).
     * @param s - Scanner for .zork file
     * @param d - reference to Dungeon class
     * @throws NoExitException when end of exits reached
     */
    Exit(Scanner s, Dungeon d) throws NoExitException  {
        String line = s.nextLine();
        if (line.equals("===")) {
            throw new NoExitException();
        }

        String roomName = line;
        this.src = d.getRoom(roomName);

        line = s.nextLine();
        this.dir = line;

        line = s.nextLine();
        roomName = line;
        this.dest = d.getRoom(roomName);
    }
    
    /**
     * Exit - this currently unused constructor is designed to be used with hard-coded dungeons.
     * It stores current direction, source room, and target room
     * @param dir - direction of the room
     * @param src - Room object where the user is coming room
     * @param dest - Room object the user is going into, using input direction
     */
    Exit(String dir, Room src, Room dest) {
        this.dir = dir;
        this.src = src;
        this.dest = dest;
    }


    /**
     * getDir - this method returns the direction from the current room (src) to the destination room (dest)
     * @return direction
     */
    String getDir() {
        return dir;
    }

    /**
     * getSrc - this method returns the current room (src) Room object
     * @return current room (src) Room object
     */
    Room getSrc() {
        return src;
    }

    /**
     * getDest - this method returns the destination room (dest) Room object based on the direction
     * the user entered
     * @return destination room (dest) Room object
     */
    Room getDest() {
        return dest;
    }

    /**
     * describe() - this method returns the text (user-friendly) about which rooms the user can enter from
     * the current room (src) and guides the user which direction to go
     * @return exit description
     */
    String describe() {
        return "You can go " + dir + " to " + dest.getName();
    }
}

/**
 * class NoExitException is a custom exception, which is thrown when there are no more exits available
 */
class NoExitException extends Exception {

    /**
     * NoExitException - default constructor
     */
    NoExitException() {
    }
}

