import java.util.Scanner;

/**
 * Exit Class - Each Room object holds on to an ArrayList of the Exits from it, and each Exit holds on to the
 * Room that it leads to. An Exit can also describe itself, which generates and returns a String message.
 * @author Object Oriented Optimists (OOO)
 * @version 2.5
 * 23 June 2020
 */
class Exit {
    private String dir;
    private Room src;
    private Room dest;
    
    /**
     * Exit - this method throws an exception if a line doesn't contain "===" (indicating the current room)
     * @param s - Scanner
     * @param d - room in Dungeon class
     * @throws NoExitException
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
    Exit(String dir, Room src, Room dest) throws NoExitException {
        this.dir = dir;
        this.src = src;
        this.dest = dest;
    }


    /**
     * getDir - this method returns the direction of the room that the user moves towards
     * @return dir
     */
    String getDir() {
        return dir;
    }

    /**
     * getSrc - this method returns the room source
     * @return src
     */
    Room getSrc() {
        return src;
    }

    /**
     * getDest - this method returns (takes the user into) the room based on the direction that the user entered
     * @return dest - next room
     */
    Room getDest() {
        return dest;
    }

    /**
     * describe() - this method returns the text about which rooms the user can enter from the current room
     * and guides the user which direction to go
     * @return Room description
     */
    String describe() {
        return "You can go " + dir + " to " + dest.getName();
    }
}

/**
 * class NoExitException is a custom exception
 */
class NoExitException extends Exception {

    /**
     * NoExitException - default constructor
     */
    NoExitException() {
    }
}

