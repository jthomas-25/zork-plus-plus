/**
 * Dungeon Class - It holds on to a Hashtable collection of Room objects, and knows which one is the entry point.
 * A Hashtable is a class that makes it easy to look up entries by a "key" rather than by a numbered index,
 * as an ArrayList does.
 * @author Object Oriented Optimists
 * @version 2.6
 * 25 June 2020
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Dungeon {

    /**
     * getTitle - this method returns title
     *
     * @return title;
     */
    public String getTitle() {
        return title;
    }

    private String title = "Simple Dungeon";    //default
    private Room entry;
    private ArrayList<Room> rooms = new ArrayList<Room>();
    private ArrayList<String> lines = new ArrayList<>();

    private String fileName;

    private ArrayList<Item> items = new ArrayList<>();

    /**
     * Dungeon constructor
     *
     * @param fileName - file to read Dungeon data from
     * @throws IllegalDungeonFormatException
     * @throws FileNotFoundException
     */
    public Dungeon(String fileName, boolean initState) throws IllegalDungeonFormatException, FileNotFoundException, NoRoomException {
        //TODO implement initState

        this.fileName = fileName;
        File file = new File(fileName);


//        System.out.println("Dungeon file is " + fileName + " File path: " + file.getAbsolutePath() +
//                " File size " + file.length());

        Scanner stdin = new Scanner(file);

//        boolean firstLine = true;
//        boolean secondLine = false;
//        boolean thirdLine = false;
//        boolean fourthLine = false;

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

                        lineNumber += 3;
                        this.add(item);
                    }
                }

                line = stdin.nextLine();
                if (line.equals("Rooms:")) {
                    while (!line.equals("===")) {

                        Room room;
                        try {
                            room = new Room(stdin, this,true);  //TODO check initState
                        } catch (NoRoomException | NoItemException ex) {
                            break;
                        }

                        line = stdin.nextLine();
                        lineNumber += 3;
                        rooms.add(room);
                        if (firstRoom) {
                            this.entry = room;
                            firstRoom = false;
                        }
                    }
                }
                line = stdin.nextLine();
                lineNumber++;
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
                        lineNumber += 4;
                    }
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

    private void init() {
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

        w.write("Item states:" + "\n");
        ArrayList<Item> inventory = GameState.instance().getInventory();

        for (int i = 0; i < inventory.size(); i++) {
            inventory.get(i).storeState(w);
        }
        w.write("===" + "\n");

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
    void restoreState(Scanner r) throws NoItemException {  //TODO implement
        String line = r.nextLine();

        if (line.equals("Item states:")) {  //Item states
            while (!line.equals("===")) {   //loop through all item

                try {
                    Item item = new Item(r);
                    GameState.instance().addToInventory(item);
                }
                catch (NoItemException e) {
                    break;
                }
//                line = r.nextLine();   //skip ---
            }
        }

        line = r.nextLine();
        if (line.equals("Room states:")) {  //Room states
            while (!line.equals("===")) {   //loop through all rooms
                line = r.nextLine(); //room name
                if (line.equals("===")) {
                    break;
                }
                String[] currentRoomSplit = line.split(":");    //parse room name by colon
                String currentRoomName = currentRoomSplit[0];   //Room name
                this.getRoom(currentRoomName).restoreState(r);  //Get room and restore it
                line = r.nextLine();   //skip ---
            }
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

    public Item getItem(String primaryName) throws NoItemException {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getPrimaryName().equals(primaryName)) {
                return items.get(i);
            }
        }
        throw new NoItemException(String.format("You're not carrying a(n) %s.", primaryName));
    }

    public void add (Item item) {
        items.add(item);
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

