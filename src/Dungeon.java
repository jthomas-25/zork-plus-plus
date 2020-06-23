/**
 * Dungeon Class - It holds on to a Hashtable collection of Room objects, and knows which one is the entry point.
 * A Hashtable is a class that makes it easy to look up entries by a "key" rather than by a numbered index,
 * as an ArrayList does.
 * @author Richard Volynski
 * @version 2.5
 * 23 June 2020
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class Dungeon {
    private String title = "Simple Dungeon";    //default
    private Room entry;
    private ArrayList<Room> rooms = new ArrayList<Room>();
    private String fileName;
    private Hashtable<String, Item> items = new Hashtable<String, Item>();


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
    public Dungeon(String fileName, boolean initState) throws IllegalDungeonFormatException, FileNotFoundException, NoRoomException {

        this.fileName = fileName;
        File file = new File(fileName);


//        System.out.println("Dungeon file is " + fileName + " File path: " + file.getAbsolutePath() +
//                " File size " + file.length());

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
                if (line.equals("Zork III")) {
                    continue;
                } else {
                    throw new IllegalDungeonFormatException("Dungeon file is incompatible with the current version of Zork");
//                    System.out.println("Dungeon file is incompatible with the current version of Zork");
                }
            }

            if (lineNumber == 3) {
                if (line.equals("===")) {
                    continue;
                } else {
//                    System.out.println("Third line is wrong in the Dungeon file");
                    break;
                }
            }

            boolean firstRoom = true;

            if (lineNumber == 4) {
                if (line.equals("Items:")) {
                    while (!line.equals("===")) {
                        Item item;
                        try {
                            item = new Item(stdin);
                        } catch (NoItemException ex) {
                            break;
                        }

                        this.add(item);
                    }
                }

                line = stdin.nextLine();
                if (line.equals("Rooms:")) {
                    while (!line.equals("===")) {

                        Room room;
                        try {
                            room = new Room(stdin, this, initState);
                        } catch (NoRoomException ex) {
                            break;
                        }

                        rooms.add(room);
                        if (firstRoom) {
                            this.entry = room;
                            firstRoom = false;
                        }
                    }
                }

                line = stdin.nextLine();
                if (line.equals("Exits:")) {
                    while (!line.equals("===")) {

                        Exit exit;
                        try {
                            exit = new Exit(stdin, this);
                        } catch (NoExitException ex) {
                            break;
                        }
                        line = stdin.nextLine();
                        Room exitSrc = exit.getSrc();   //exit src = null
                        String exitSrcRoomName = exitSrc.getName();

                        for (int i = 0; i < rooms.size(); i++) {
                            Room currentRoom = rooms.get(i);
                            if (currentRoom.getName().equals(exitSrcRoomName)) {
                                currentRoom.addExit(exit);
                                break;
                            }
                        }
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
        this.title = title;
        this.setEntry(entry);
    }

    private void init() {
    }

    /**
     * getTitle - this method returns title
     *
     * @return title;
     */
    public String getTitle() {
        return title;
    }

    /**
     * add - this method adds a room to Dungeon class
     *
     * @param room
     */
    public void add(Room room) {
        rooms.add(room);
    }

    /**
     * getRoom - this method returns a Room by roomName
     *
     * @param roomName
     * @return room found
     */
    public Room getRoom(String roomName) {
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
    public String getFileName() {
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
            if (rooms.get(i).isBeenHere()) {
                rooms.get(i).storeState(w);
            }
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
    public Room getEntry() {
        return entry;
    }

    /**
     * setEntry - this method sets the room the user is entering
     * @param entry - room
     */
    public void setEntry(Room entry) {
        this.entry = entry;
    }

    public Item getItem(String primaryName) {
        return items.get(primaryName);
    }

    public void add(Item item) {
        items.put(item.getPrimaryName(), item);
    }
}

/**
 * class IllegalDungeonFormatException is a custom exception
 */
class IllegalDungeonFormatException extends Exception {

    /**
     * IllegalDungeonFormatException - default constructor
     */
    public IllegalDungeonFormatException(String errorMsg) {
    }
}
