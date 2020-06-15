/**
 * Room Class - represents every room in the dungeon (name, description), knows whether or not
 * the adventurer has already visited it. Also, the Room Class contains lists of Exits.
 * @author Richard Volynski
 * @version 1.9
 * 15 June 2020
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
    private Scanner s;

    /**
     * setDesc - this method sets room description
     * @param desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    private String desc;
    private boolean beenHere;

    public Room (Scanner s) throws NoRoomException {
        String line = s.nextLine();
        this.name = line;
        line = s.nextLine();
        this.desc = line;
        line = s.nextLine();
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
        if (exits.get(dir.toUpperCase()) == null) {
            return this;
        }
        else {
            return exits.get(dir.toUpperCase()).getDest();
        }
    }

    /**
     * addExit - this method stores exit in class Room
     * @param exit - exit
     */

    public void addExit (Exit exit) {
        exits.put(exit.getDir(), exit);
    }

    void storeState(PrintWriter w) {    //TODO implement
    }

    void restoreState(Scanner r) {     //TODO implement
    }
}

class NoRoomException extends Exception {

    NoRoomException() {

    }
}

