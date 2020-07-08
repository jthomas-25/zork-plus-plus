import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Room Class - represents every room in the dungeon.
 * Room has name and description and stores whether or not the adventurer has already visited it.
 * Also, the Room Class contains lists of Exits.
 * @author Object Oriented Optimists (OOO)
 * @author Richard Volynski
 * @version 2.7
 * 8 July 2020
 */
public class Room {
    private Hashtable <String, Exit> exits;
    private String name;
    private boolean roomDescriptionNeeded;
    private String desc;
    private boolean beenHere;
    private ArrayList<Item> contents;

    /**
     * Currently not used
     * Room - this constructor initializes itself, reading lines from .zork file using a scanner, which is provided
     * as a parameter. It also uses Dungeon, provided as a parameter, to find an item by item name, and calls another
     * Room constructor with three parameters, providing third parameter (initState) as true.
     * This constructor throws an exception (NoRoomException) if a line does
     * contain "===" (indicating end of rooms in the .zork file).
     * @param s - Scanner for .zork file
     * @param d - reference to Dungeon class
     * @throws NoRoomException - if a line contains "===" (indicating end of rooms in the zork file).
     */
    Room(Scanner s, Dungeon d) throws NoRoomException {
        this(s, d, true);
    }


    /**
     * Room - this constructor initializes itself, reading lines from .zork file using a scanner, which is provided
     * as a parameter. It also uses Dungeon, provided as a parameter, to find an item by item name. The third
     * parameter (initState) identifies whether to initialize Room content
     * This constructor throws an exception (NoRoomException) if a line does
     * contain "===" (indicating end of rooms in the .zork file).
     * @param s - Scanner for .zork file
     * @param d - reference to Dungeon class
     * @param initState - flag to initalize room contents
     * @throws NoRoomException - if a line contains "===" (indicating end of rooms in the zork file).
     */
    public Room(Scanner s, Dungeon d, boolean initState) throws NoRoomException {
        init();
        String line = s.nextLine();
        if (line.equals("===")) {
            throw new NoRoomException();
        }
        this.name = line;

        line = s.nextLine();
        String[] splitLine = line.split(": ");
        if (splitLine[0].equals("Contents")) {
            //Decide whether to reset room state (i.e. contents)
            if (initState) {
                String[] itemNames = splitLine[1].split(",");
                for (String itemName : itemNames) {
                    Item item = d.getItem(itemName);
                    this.add(item);
                }
            }
            this.desc = s.nextLine();
        } else {
            this.desc = line;
        }

        line = s.nextLine();
        while (!line.equals("---")) {
            this.desc += ("\n" + line);
            line = s.nextLine();
        }
    }

    /**
     * Currently obsolete
     * Room - this constructor initializes itself, reading lines from .zork file using a scanner, which is provided
     * as a parameter.
     * This constructor throws an exception (NoRoomException) if a line
     * contains "===" (indicating end of rooms in the .zork file).
     * @param s - Scanner for .zork file
     * @throws NoRoomException - if a line contains "===" (indicating end of rooms in the zork file).
     */
    public Room (Scanner s) throws NoRoomException {
        init();
        String line = s.nextLine();
        if (line.equals("===")) {
            throw new NoRoomException();
        }
        this.name = "\t" + line;
        line = s.nextLine();
        this.desc = "\t" + line;
    }

    /**
     * Currently obsolete
     * Constructor Room - which stores room name in instance variable name
     * @param name - Room name
     */
    Room (String name) throws NoExitException {
        init();
        this.name = name;
    }

    /**
     * init - private method to initalize instance variables for the room, including exits, roomDescriptionNeeded
     * flag, beenHere flag, and list of contents (items)
     */
    private void init() {
        exits = new Hashtable<>();
        roomDescriptionNeeded = false;
        beenHere = false;
        contents = new ArrayList<>();
    }

    /**
     * getName - this method returns name of the room
     * @return name - room name
     */
    String getName() {
        return name;
    }

    /**
     * Currently not used
     * setDesc - this method sets room description
     * @param desc - room description
     */
    void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * describe - this method generates user-friendly room description,
     items in the current room and gives all exits of the room object. This
     method also tells the user if they visited the room. If this method is 	 called again, only the room name and exits will be returned.
     * @return user-friendly room name, description (if user's first time in 	 Room), items (if applicable), and nearest exits
     */
    String describe() {
        String output = "";
        if (!beenHere) {
            beenHere = true;
            output = name + "\n" + this.desc + "\n" + "\n";
        }
        else if (this.roomDescriptionNeeded) {
            this.roomDescriptionNeeded = false;
            output = name + "\n" + this.desc + "\n" + "\n";
        }
        else {
            output = name + "\n";
        }

        // Show all balances in hash table
        Enumeration directions = exits.keys();

        while (directions.hasMoreElements()) {
            String dir = (String)directions.nextElement();
            Exit exit = exits.get(dir);
            output += exit.describe() + "\n";
        }

        for (Item i : contents) {
            output = output + "\n" + String.format("There is a(n) %s here.", i);
        }
        return output;
    }

    /**
     * leaveBy - this method returns the Room object from the direction PATH     String the user is going from the current room to the destination room
     * @param dir - direction ("n", "s", "w", "e", "u/up", "d/down")
     * @return room the user is going to
     */
    Room leaveBy(String dir) {
        if (exits.get(dir.toLowerCase()) == null) {
            return this;
        }
        else {
            Room newRoom = exits.get(dir.toLowerCase()).getDest();
            return newRoom;
        }
    }

    /**
     * addExit - this method adds an exit to the hashtable of the exits in the room
     * @param exit - exit
     */
    public void addExit (Exit exit) {
        exits.put(exit.getDir(), exit);
    }

    /**
     * storeState - this method stores the state of the room in the .sav file, whether the room was visited or not
     * as well as room contents (items)
     * @param w - PrintWriter to write to .sav file
     */
    void storeState(PrintWriter w) {
        w.write(getName() + ":\n");
        w.write("beenHere=" + beenHere + "\n");
        if (!contents.isEmpty()) {
            w.write("Contents: ");
            for (int i = 0; i < contents.size(); i++) {
                Item item = contents.get(i);
                w.write(item.getPrimaryName());
                if (i < contents.size() - 1) {
                    w.write(",");
                }
            }
            w.write("\n");
        }
        w.write("---" + "\n");
    }

    /**
     * restoreState - this method restores the state of the room from the file, whether it was visited or not
     * @param r - Scanner to read from .sav file
     */
    void restoreState(Scanner r) {
        String beenHereLine = r.nextLine();
        beenHere = Boolean.parseBoolean(beenHereLine.split("=")[1]);
    }

    /**
     * restoreState - this method first calls restoreState with Scanner parameter to restore
     * room's beenHere flag. Then, this method restores room contents (items) and adds them to the list of items
     * in the room.
     * @param s - Scanner to read from .sav file
     * @param d - current Dungeon
     */
    void restoreState(Scanner s, Dungeon d) {
        restoreState(s);
        String line = s.nextLine();
        String[] splitLine = line.split(": ");
        if (splitLine[0].equals("Contents")) {
            String[] itemNames = splitLine[1].split(",");
            for (String itemName : itemNames) {
                Item item = d.getItem(itemName);
                this.add(item);
            }
            s.nextLine();   //Skip "---" delimiter
        }
    }

    /**
     * add - this method adds an item to the Room's list of items
     * @param item - item to add
     */
    void add(Item item) {
        this.contents.add(item);
    }

    /**
     * remove - this method removes an item from the Room's list of items
     * @param item - item to remove
     */
    void remove(Item item) {
        this.contents.remove(item);
    }

    /**
     * remove - This method removes multiple items while iterating over them.
     * @param itr - the iterator which will do the removing
     */
    void remove(Iterator<Item> itr) {
        itr.remove();
    }

    /**
     * getItemNamed - this method checks if user inputted item matches the name
     * of an item in the user's current room and returns the item if found.
     * Otherwise, return null.
     * @param name - item name (user input)
     * @return item found in the room
     */
    Item getItemNamed(String name) {
        for (Item item : contents) {
            if (item.goesBy(name)) {
                return item;
            }
        }
        return null;
    }

    /**
     * getContents - this method returns a list of contents (items) from a room
     * @return room contents (items)
     */
    ArrayList<Item> getContents() {
        return this.contents;
    }

    /**
     * isBeenHere - this method determines whether or not the user visited a room
     * @return beenHere flag
     */
    public boolean isBeenHere() {
        return beenHere;
    }

    /**
     * setRoomDescriptionNeeded - this method sets description needed flag for a room
     */
    public void setRoomDescriptionNeeded() {
        this.roomDescriptionNeeded = true;
    }
}

/**
 * class NoRoomException is a custom exception
 */
class NoRoomException extends Exception {

    /**
     * NoRoomException - default constructor
     */
    NoRoomException() {
    }
}


