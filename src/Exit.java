//package com.company;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Exit Class is designed to be a link between rooms, allowing the user to travel from one room
 * to another room in a specific direction.
 * Exit Class contains the user's current room (src, {@link Room} object), direction (e.g. "w", "e"), and
 * room destination (dest, {@link Room} object), where current direction (user input) leads to.
 * An exit can also describe itself, which generates and returns a String message, telling the user
 * which direction they can go and the name of the surrounding room(s). Also, Exit class initializes itself by
 * reading from the .zork file.
 * @author Richard Volynski (OOO)
 * @version 3.1
 * 19 July 2020
 */
public class Exit {
    private String dir;
    private Room src;
    private Room dest;
    private boolean locked;
    private boolean requiresItemToUnlock;
    private String nameOfItemRequiredToUnlock;
    
    /**
     * Thrown when an Exit constructor, given a Scanner object,
     * detects the end of the exits section of a .zork file.
     * @author John Thomas
     */
    class NoExitException extends Exception {}

    /**
     * Thrown when the player attempts to leave the current room through a locked exit.
     * @author John Thomas
     */
    static class ExitLockedException extends Exception {
        
        /**
         * Constructs a new ExitLockedException with the given message.
         * @param message the message to be printed if this exception is thrown
         */
        ExitLockedException(String message) {
            super(message);
        }
    }
    
    /**
     * Exit - this constructor initializes itself, reading lines from .zork file using a scanner, which is provided
     * as a parameter. It also uses Dungeon, provided as a parameter, to find a room by room name.
     * This constructor throws an exception (NoExitException) if a line does
     * contain "===" (indicating end of exits in the .zork file).
     * @param s - Scanner for .zork file
     * @param d - reference to Dungeon class
     * @throws NoExitException when end of exits reached
     */
    Exit(Scanner s, Dungeon d) throws NoExitException {
        this(s, d, false);
    }

    public Exit(Scanner s, Dungeon d, boolean initState) throws NoExitException {
        init();
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

        this.src.addExit(this);

        line = s.nextLine();
        while (!line.equals("---")) {
            String[] splitLine = line.split(": ");
            switch (splitLine[0]) {
                case "locked":
                    if (initState) {
                        this.src.lockExit(this);
                    }
                    break;
                case "Requires":
                    this.nameOfItemRequiredToUnlock = splitLine[1];
                    requiresItemToUnlock = true;
                    break;
            }
            line = s.nextLine();
        }
    }
    
    /**
     * Exit - this currently unused constructor is designed to be used with hard-coded dungeons.
     * It stores current direction, source room, and target room
     * @param dir - direction of the room
     * @param src - {@link Room} object where the user is coming room
     * @param dest - {@link Room} object the user is going into, using input direction
     */
    public Exit(String dir, Room src, Room dest) {
        this(dir, src, dest, false);
    }

    public Exit(String dir, Room src, Room dest, boolean locked) {
        //init();
        this.dir = dir;
        this.src = src;
        this.dest = dest;
        this.locked = locked;
    }

    private void init() {
        locked = false;
        requiresItemToUnlock = false;
    }

    /**
     * getDir - this method returns the direction from the current room (src) to the destination room (dest)
     * @return direction
     */
    public String getDir() {
        return dir;
    }

    /**
     * getSrc - this method returns the current room (src) {@link Room} object
     * @return current room (src) {@link Room} object
     */
    public Room getSrc() {
        return src;
    }

    /**
     * getDest - this method returns the destination room (dest) {@link Room} object based on the direction
     * the user entered
     * @return destination room (dest) {@link Room} object
     */
    public Room getDest() {
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

    void storeState(PrintWriter w) {
        w.write(this.dir);
/*
        if (this.isLocked()) {
            w.write(dir);
        }
*/
    }

    void restoreState() {
        
    }

    boolean requiresItemToUnlock() {
        return requiresItemToUnlock;
    }

    boolean requiresThisItemToUnlock(Item item) {
        return item.goesBy(nameOfItemRequiredToUnlock);
        //return locked && requiresItemToUnlock && item.goesBy(nameOfItemRequiredToUnlock);
    }

    /**
     * Checks to see if the exit cannot be passed through.
     * @return true if and only if the exit is locked, false otherwise
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Prevents this exit from being passed through. Does nothing if this exit is already locked.
     */
    public void lock() {
        locked = true;
    }

    /**
     * Allows this exit to be passed through. Does nothing if this exit is already unlocked.
     */
    public void unlock() {
        locked = false;
    }
}

/**
 * Thrown when an {@link Exit} constructor, given a Scanner object,
 * detects the end of the exits section of a .zork file.
 * @author John Thomas
 */
class NoExitException extends Exception {}
