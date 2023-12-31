//package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * Dungeon Class - It holds on to a Hashtable collection of Room objects, and knows which one is the entry point.
 * A Hashtable is a class that makes it easy to look up entries by a "key" rather than by a numbered index,
 * as an ArrayList does.
 * @author Object Oriented Optimists (OOO)
 * @version 2.9
 * 22 July 2020
 */
public class Dungeon {
    private String title = "Simple Dungeon";    //default
    private Room entry;
    private ArrayList<Room> rooms;
    private String fileName;
    private Hashtable<String, Item> items;
    private String intro = "";



    public Dungeon(String fileName) throws IllegalDungeonFormatException, FileNotFoundException, NoRoomException {
        this(fileName, true);
    }
    /**
     * Dungeon constructor
     *
     * @param fileName - file to read Dungeon data from
     * @throws IllegalDungeonFormatException
     * @throws FileNotFoundException
     */
    Dungeon(String fileName, boolean initState) throws IllegalDungeonFormatException, FileNotFoundException, NoRoomException {
        init();
        this.fileName = fileName;
        File file = new File(this.fileName);
        //System.out.println("Dungeon file is " + file.getName() + " File path: " + file.getPath());

        Scanner stdin = new Scanner(file);
        int lineNumber = 0;
        while (stdin.hasNextLine()) {
            String line = stdin.nextLine();
            lineNumber++;
            if (lineNumber == 1) {
                this.title = line;
                continue;
            }
            if (lineNumber == 2) {
                String version = GameState.instance().getVersion();
                if (line.equals(version)) {
                    continue;
                }
                else {
                    throw new IllegalDungeonFormatException("Dungeon file is incompatible with the current version of Zork (" + version + ")");
                }
            }

            if (!line.equals("===")) {
                intro += line + "\n";
                continue;
            }
            else {
                line = stdin.nextLine();
            }

            boolean firstRoom = true;
            if (line.equals("Items:")) {
                while (!line.equals("===")) {
                    Item item;
                    try {
                        item = new Item(stdin);
                        this.add(item);
                    } catch (NoItemException ex) {
                        break;
                    }
                }
            }

            line = stdin.nextLine();
            if (line.equals("Rooms:")) {
                while (!line.equals("===")) {

                    Room room;
                    try {
                        room = new Room(stdin, this, initState);
                        rooms.add(room);
                        if (firstRoom) {
                            this.entry = room;
                            firstRoom = false;
                        }
                    } catch (NoRoomException ex) {
                        break;
                    }
                }
            }

            line = stdin.nextLine();
            if (line.equals("Exits:")) {
                while (!line.equals("===")) {

                    Exit exit;
                    try {
                        exit = new Exit(stdin, this, initState);
                    } catch (Exit.NoExitException ex) {
                        break;
                    }
                }
            }
        }
    }


    /**
     * Dungeon
     * @param entry - room the user is currently in
     * @param title - Dungeon description
     */
    Dungeon(Room entry, String title) {
        init();
        this.title = title;
        this.setEntry(entry);
    }

    private void init() {
        rooms = new ArrayList<>();
        items = new Hashtable<>();
    }

    ArrayList<Room> getRooms() {
        return rooms;
    }

    int getNumRooms() {
        return rooms.size();
    }

    /**
     * getTitle - this method returns title
     *
     * @return title;
     */
    String getTitle() {
        return title;
    }

    /**
     * add - this method adds a room to Dungeon class
     *
     * @param room
     */
    void add(Room room) {
        rooms.add(room);
    }

    /**
     * getRoom - this method returns a Room by roomName
     *
     * @param roomName
     * @return room found
     */
    Room getRoom(String roomName) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getName().equals(roomName)) {
                return rooms.get(i);
            }
        }
        return null;
    }

    /**
     * getFileName - this method returns the filename
     *
     * @return fileName
     */
    private String getFileName() {
        return fileName;
    }

    /**
     * storeState - this method checks if next line is equal to "===". If yes, then write current room
     *
     * @param w - PrintWriter
     */
    void storeState(PrintWriter w) {
        w.write("Dungeon file: " + getFileName() + "\n");
        w.write("Room states:" + "\n");
        for (int i = 0; i < rooms.size(); i++) {
            rooms.get(i).storeState(w);
        }
        w.write("===" + "\n");
    }

    /**
     * restoreState - this method allow the user to resume the game from the state it was saved
     *
     * @param r - Scanner
     */
    void restoreState(Scanner r) {
        r.nextLine(); //Skip "Room states:" line
        String line = r.nextLine(); //room name
        while (!line.equals("===")) {   //loop through all rooms
            String[] currentRoomSplit = line.split(":");    //parse room name by colon
            String currentRoomName = currentRoomSplit[0];
            this.getRoom(currentRoomName).restoreState(r, this);
            line = r.nextLine(); //room name
        }
    }

    /**
     * getEntry - this method returns the room the user is entering
     *
     * @return entry
     */
    Room getEntry() {
        return entry;
    }

    /**
     * setEntry - this method sets the room the user is entering
     * @param entry - room
     */
    void setEntry(Room entry) {
        this.entry = entry;
    }

    Item getItem(String primaryName) {
        return items.get(primaryName);
    }

    void add(Item item) {
        items.put(item.getPrimaryName(), item);
    }
    void removeItem(Item item) {
        items.remove(item);
    }
    void removeItem(String itemName) {
        Item item = getItem(itemName);
        if (item != null) {
            this.removeItem(item);
        }
    }
    public String getIntro() {
        return intro;
    }
    public void setIntro(String intro) {
        this.intro = intro;
    }
}

/**
 * Thrown by any method that parses a .zork dungeon file if the file
 * does not adhere to the correct format.
 * @author John Thomas
 */
class IllegalDungeonFormatException extends Exception {

    /**
     * Constructs a new exception with the given error message.
     * @param errorMsg the detailed message that is returned when this exception is thrown
     */
    IllegalDungeonFormatException(String errorMsg) {
    }
}
