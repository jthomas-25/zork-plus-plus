import java.util.Scanner;

/**
 * Exit Class contains the user's current room (Room object), direction (e.g. "w", "e", "s"), and
 * room destination (Room object), where current direction (user input) leads to.
 * An exit can also describe itself, which generates and returns a String message which tells the user
 * which direction they can go and the name of the surrounding room(s). Also, Exit class implements initialization
 * from Zork file.
 * @author Richard Volynski (OOO)
 * @version 2.6
 * 6 July 2020
 */
class Exit {
    private String dir;
    private Room src;
    private Room dest;
    
    /**
     * Exit - this constructor initializes itself, reading lines from .zork file using a scanner.
     * This constructor throws an exception if a line doesn't contain "===" (indicating end of exits in the .zork file).
     * This constructor also reads Dungeon class by room name.
     * @param s - Scanner
     * @param d - room in Dungeon class
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
     * Exit - this constructor stores current direction, source room, and target room
     * @param dir - direction of the room
     * @param src - room where the user is coming room
     * @param dest - room the user is going into, using input direction
     */
    Exit(String dir, Room src, Room dest) {
        this.dir = dir;
        this.src = src;
        this.dest = dest;
    }


    /**
     * getDir - this method returns the direction from the current room to the destination room
     * @return direction
     */
    String getDir() {
        return dir;
    }

    /**
     * getSrc - this method returns the current room
     * @return current room
     */
    Room getSrc() {
        return src;
    }

    /**
     * getDest - this method returns (takes the user into) the destination room based on the
     * direction that the user entered
     * @return destination room
     */
    Room getDest() {
        return dest;
    }

    /**
     * describe() - this method returns the text (user-friendly) about which rooms the user can enter from
     * the current room and guides the user which direction to go
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

