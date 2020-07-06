import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Room Class - represents every room in the dungeon (name, description), knows whether or not
 * the adventurer has already visited it. Also, the Room Class contains lists of Exits.
 * @author Object Oriented Optimists (OOO)
 * @version 2.5
 * 23 June 2020
 */
public class Room {
    private Hashtable <String, Exit> exits;
    private String name;
    private boolean roomDescriptionNeeded;
    private String desc;
    private boolean beenHere;
    private ArrayList<Item> contents;


    Room(Scanner s, Dungeon d) throws NoRoomException {
        this(s, d, true);
    }
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
     * Room - constructor
     * @param s - Scanner
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
     * Constructor Room - which stores room name in instance variable name
     * @param name
     */
    Room (String name) throws NoExitException {
        init();
        this.name = name;
    }

    private void init() {
        exits = new Hashtable<>();
        roomDescriptionNeeded = false;
        beenHere = false;
        contents = new ArrayList<>();
    }

    /**
     * getName - this method returns name of the room
     * @return room name
     */
    String getName() {
        return name;
    }

    /**
     * setDesc - this method sets room description
     * @param desc
     */
    void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * describe - this method generates room description
     * @return output
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
     * leaveBy - this method returns the room from the direction the user is going
     * @param dir
     * @return room
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
     * addExit - this method stores exit in class Room
     * @param exit - exit
     */
    public void addExit (Exit exit) {
        exits.put(exit.getDir(), exit);
    }

    /**
     * storeState - this method stores the state of the room in the file, whether it was visited or not
     * @param w - PrintWriter to write to file
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
     * @param r - PrintWriter to write to file
     */
    void restoreState(Scanner r) {
        String beenHereLine = r.nextLine();
        beenHere = Boolean.parseBoolean(beenHereLine.split("=")[1]);
    }

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

    void add(Item item) {
        this.contents.add(item);
    }

    void remove(Item item) {
        this.contents.remove(item);
    }
    /**
     * This method will actually let you remove multiple items while iterating
     * over them, thereby avoiding a ConcurrentModificationException.
     * @param itr the iterator which will do the removing.
     */
    void remove(Iterator<Item> itr) {
        itr.remove();
    }

    Item getItemNamed(String name) {
        for (Item item : contents) {
            if (item.goesBy(name)) {
                return item;
            }
        }
        return null;
    }

    ArrayList<Item> getContents() {
        return this.contents;
    }

    /**
     * isBeenHere - this method determines whether or not the user visited the room
     * @return beenHere
     */
    public boolean isBeenHere() {
        return beenHere;
    }

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


