/**
 * Item Class - An Item has a name, weight, and a Hashtable of verb/message pairs (called "messages").
 * @author Object Oriented Optimists
 * @version 2.7
 * 25 June 2020
 */


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;

public class Item {
    private String primaryName;
    private int weight;
    private Hashtable messages = new Hashtable();
    private ArrayList<String> aliases = new ArrayList<>();


<<<<<<< HEAD
    Item (Scanner s) throws NoItemException {
=======
    /**
     * Item - Constructor to read from file, parse it, and restore items
     * @param s - file to read from
     * @throws NoItemException
     */
    public Item (Scanner s) throws NoItemException {
>>>>>>> b8a429491614a1e9cdef1c7bf93a55d14f8c40e3
        String line = s.nextLine(); //name, aliases
        if (line.equals("===") || line.equals("---")) {
            throw new NoItemException("");
        }
        String[] itemNameSplit = line.split(",");
        primaryName = itemNameSplit[0];

        for (int i = 1; i < itemNameSplit.length; i++) {
            aliases.add(itemNameSplit[i]);
        }

        line = s.nextLine();
<<<<<<< HEAD
        this.weight = Integer.parseInt(line);
=======

        try {
            this.weight = Integer.parseInt(line);
        }
        catch (Exception e) {
            e = e;
        }
>>>>>>> b8a429491614a1e9cdef1c7bf93a55d14f8c40e3


        while (!line.equals("---")) {
            line = s.nextLine();

            if (line.equals("---")) {
                break;
            }
            String[] keyToSplit = line.split(":");
            messages.put(keyToSplit[0],keyToSplit[1]);
        }
    }

    /**
     * storeState - this method stores the state of the room in the file, whether it was visited or not
     * @param w - PrintWriter to write to file
     */
    void storeState(PrintWriter w) {
        String primaryNameAndAliases = getPrimaryName();

        for (int i = 0; i < aliases.size(); i++) {
            primaryNameAndAliases+= "," + aliases.get(i);
        }
        w.write(primaryNameAndAliases + "\n");
        w.write(Integer.toString(weight) + "\n");
        Iterator keys = messages.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = (String) messages.get(key);
            w.write(key + ":" + value + "\n");
        }
        w.write("---" + "\n");
    }


<<<<<<< HEAD
    boolean goesBy(String name) {
        return primaryName.equals(name) || aliases.contains(name);
    }

    String getPrimaryName() {
        return primaryName;
    }

    String getMessageForVerb(String verb) {
        return messages.get(verb);
=======
    /**
     * goesBy - Returns true if the Item is known by a certain name
     * @param name - item name
     * @return boolean
     */
    public boolean goesBy(String name) {
        if (name.equals(primaryName)) {
            return true;
        }
        else {
            for (int i = 0; i < aliases.size(); i++) {
                if (aliases.get(i).equals(name)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * getPrimaryName -this method returns name of an item
     * @return primaryName
     */
    public String getPrimaryName() {
        return primaryName;
    }

    /**
     * getMessageForVerb - this method returns a certain message for user command
     * @param verb
     * @return verb
     */
    public String getMessageForVerb(String verb) {
        Iterator keys = messages.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (key.equals(verb)) {
                return (String) messages.get(key);
            }
        }
        return null;
>>>>>>> b8a429491614a1e9cdef1c7bf93a55d14f8c40e3
    }

    /**
     * toString
     * @return primaryName - item name
     */
    public String toString() {
        return primaryName;
    }

<<<<<<< HEAD
    int getWeight() {
=======
    /**
     * getWeight - this message returns weight of user's inventory
     * @return weight
     */
    public int getWeight() {
>>>>>>> b8a429491614a1e9cdef1c7bf93a55d14f8c40e3
        return weight;
    }
}

class NoItemException extends Exception {
    /**
     * NoItemException - default constructor
     * @param format
     */
    NoItemException(String format) {
    }
}
