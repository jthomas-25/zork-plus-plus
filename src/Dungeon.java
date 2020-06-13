/**
 * Dungeon Class - It holds on to a Hashtable collection of Room objects, and knows which one is the entry point.
 * A Hashtable is a class that makes it easy to look up entries by a "key" rather than by a numbered index,
 * as an ArrayList does.
 * @author Richard Volynski
 * @version 1.7
 * 13 June 2020
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
    private ArrayList<String> lines = new ArrayList<>();

    private String fileName;

    public Dungeon (String fileName) {  //TODO implement
        Scanner stdin = new Scanner(fileName);

//        boolean firstLine = true;
//        boolean secondLine = false;
//        boolean thirdLine = false;
//        boolean fourthLine = false;

        int lineNumber = 0;

        while (stdin.hasNext()) {
            String line = stdin.nextLine();
            if (lineNumber == 0) {
                this.title = line;
                lineNumber++;
                continue;
            }
            if (lineNumber == 1) {
                if (line.equals("Zork II")) {
                    lineNumber++;
                    continue;
                }
                else {
                    System.out.println("Dungeon file is incompatible with the current version of Zork");
                    break;  //TODO exit gracefully
                }
            }

            if (lineNumber == 2) {
                if (line.equals("===")) {
                    lineNumber++;
                    continue;
                }
                else {
                    System.out.println("Third line is wrong in the Dungeon file");
                    break;  //TODO exit gracefully
                }
            }

            if (lineNumber == 3) {
                if (line.equals("Rooms:")) {    //TODO implement
                }
            }

            lines.add(line);
        }
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

