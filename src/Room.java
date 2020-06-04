/**
 * Room Class - represents every room in the dungeon (name, description), knows whether or not
 * the adventurer has already visited it. Also, the Room Class contains lists of Exits.
 * @author Richard Volynski
 * @version 1.2
 * 4 June 2020
 */



import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class Room {
    public String getName() {
        return name;
    }
    private Hashtable <String, Exit> exits = new Hashtable<>();

    private String name;
    private boolean firstTimeWhenEnter = true;

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private String desc;
    private boolean beenHere;

    Room (String name) {
        this.name = name;
    }

    String describe() {
        String output = "";
        if (this.firstTimeWhenEnter) {
            this.firstTimeWhenEnter = false;
            output = desc + "\n";
        }
        else {
            output = name + "\n";
        }

        // Show all balances in hash table.
        Enumeration directions = exits.keys();

        while (directions.hasMoreElements()) {
            String dir = (String)directions.nextElement();
            Exit exit = (Exit)exits.get(dir);
            output+= exit.describe() + "\n";

        }
        return output;
    }

    Room leaveBy(String dir) {
        return null; //TODO return Room
    }

    public void addExit (Exit exit) {
        exits.put(exit.getDir(), exit);
    }
}

