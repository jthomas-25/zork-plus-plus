/**
 * Dungeon Class - It holds on to a Hashtable collection of Room objects, and knows which one is the entry point.
 * A Hashtable is a class that makes it easy to look up entries by a "key" rather than by a numbered index,
 * as an ArrayList does.
 * @author Richard Volynski
 * @version 1.6
 * 12 June 2020
 */

package com.company;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Dungeon {

    /**
     * getTitle - this method returns title
     * @return title;
     */
    public String getTitle() {
        return title;
    }

    private String title;
    private Room entry;
    private ArrayList<Room> rooms = new ArrayList<Room>();

    private String fileName;

    public Dungeon (String fileName) {  //TODO implement
    }


    /**
     * Dungeon
     * @param entry - room the user is currently in
     * @param title - Dungeon description
     */
     Dungeon(Room entry, String title) {
        this.title = title;
        this.setEntry(entry);
     }

     private void init() {  //TODO implement
     }

    /**
     * add - this method adds a room to Dungeon class
     * @param room
     */
    public void add (Room room) {
        rooms.add(room);
    }

    /**
     * getRoom - this method returns a Room by roomName
     * @param roomName
     * @return room found
     * */
    public Room getRoom(String roomName) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getName().equals(roomName)) {
                return rooms.get(i);
            }
        }
        return null;
    }

    public String getFileName() {
        return fileName;
    }

    void storeState(PrintWriter w) {   //TODO implement

    }

    void restoreState(Scanner r) {  //TODO implement

    }

    public Room getEntry() {
        return entry;
    }

    public void setEntry(Room entry) {
        this.entry = entry;
    }
}

