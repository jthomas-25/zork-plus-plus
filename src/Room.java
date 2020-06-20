/**
 * Room Class - represents every room in the dungeon (name, description), knows whether or not
 * the adventurer has already visited it. Also, the Room Class contains lists of Exits.
 * @author Richard Volynski
 * @version 2.2
 * 20 June 2020
 */



import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;

public class Room{

    /**
     * getName - this method returns name of the room
     * @return room name
     */
    public String getName() {
        return name;
    }

    private Hashtable <String, Exit> exits = new Hashtable<>();

    private String name;
    private boolean firstTimeWhenEnter = true;
    private boolean roomDescriptionNeeded = false;
    private Scanner s;

    /**
     * setDesc - this method sets room description
     * @param desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    private String desc;
    private boolean beenHere = false;

    /**
     * Room - constructor
     * @param s - Scanner
     */
    public Room (Scanner s) throws NoRoomException {
        String line = s.nextLine();
        if (line.equals("===")) {
            throw new NoRoomException();
        }
        this.name = line;
        line = s.nextLine();
        this.desc = line;
//        line = s.nextLine();
    }


    /**
     * Constructor Room - which stores room name in instance variable name
     * @param name
     */
    Room (String name) throws NoExitException {
        this.name = name;
    }

    /**
     * describe - this method generates room description
     * @return output
     */
    String describe() {
        String output = "";
        if (this.firstTimeWhenEnter) {
            this.firstTimeWhenEnter = false;
            output = name + "\n" + desc + "\n";
        }
        else if (this.roomDescriptionNeeded) {
            this.roomDescriptionNeeded = false;
            output = name + "\n" + desc + "\n";
        }
        else {
            output = name + "\n";
        }

        // Show all balances in hash table
        Enumeration directions = exits.keys();

        while (directions.hasMoreElements()) {
            String dir = (String)directions.nextElement();
            Exit exit = exits.get(dir);
            output+= exit.describe() + "\n";

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
            newRoom.beenHere = true;
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
        w.write("beenHere=true" + "\n");
        w.write("---" + "\n");
    }

    /**
     * restoreState - this method restores the state of the room from the file, whether it was visited or not
     * @param r - PrintWriter to write to file
     */
    void restoreState(Scanner r) {
        String beenHereLine = r.nextLine(); //beenHere = true
        String[] beenHereSplit = beenHereLine.split("=");   //parse by =
        String newBeenHere = beenHereSplit[1];  //beenHere flag
        if (newBeenHere.equals("true")) {
            beenHere = true;
        }
        else {
            beenHere = false;
        }
    }

    /**
     * isBeenHere - this method determines whether or not the user visited the room
     * @return beenHere
     */
    public boolean isBeenHere() {
        return beenHere;
    }

    public Room(Scanner s, Dungeon d, boolean initState) {
    }

    void restoreState(Scanner s, Dungeon d) {
    }

    void add(Item item) {
    }

    void remove(Item item) {
    }

    Room getItemNamed(String name) {
        return null;    //TODO return item;
    }

    Room getContents() {
        return null;    //TODO return ArrayList<item>
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

