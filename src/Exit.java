/**
 * Exit Class - Each Room object holds on to an ArrayList of the Exits from it, and each Exit holds on to the
 * Room that it leads to. An Exit can also describe itself, which generates and returns a String message.
 * @author Richard Volynski
 * @version 1.8
 * 14 June 2020
 */

package com.company;

import java.util.Scanner;

public class Exit {
    private String dir;

    /**
     * getDir - this method returns the direction of the room that the user moves towards
     * @return dir
     */
    public String getDir() {
        return dir;
    }

    private Room src;
    private Room dest;
    private Scanner s;
    private Dungeon d;

    /**
     * getSrc - this method returns the room source
     * @return src
     */
    public Room getSrc() {
        return src;
    }

    /**
     * getDest - this method returns (takes the user into) the room based on the direction that the user entered
     * @return dest - next room
     */
    public Room getDest() {
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

    /**
     * Exit - this constructor stores current direction, source room, and target room
     * @param dir - direction of the room
     * @param src - room where the user is coming room
     * @param dest - room the user is going into, using input direction
     */
    public Exit (String dir, Room src, Room dest) throws NoExitException {
        this.dir = dir;
        this.src = src;
        this.dest = dest;
    }

    public Exit (Scanner s, Dungeon d) throws NoExitException  {
        String line = s.nextLine();
        this.src = new Room(line);
        line = s.nextLine();
        this.dir = line;
        line = s.nextLine();
        this.dest = new Room(line);
    }
}

class NoExitException extends Exception {

    NoExitException() {

    }
}

