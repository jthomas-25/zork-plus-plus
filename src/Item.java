/**
 * Item Class - An Item has a name, weight, and a Hashtable of verb/message pairs (called "messages").
 * @author Richard Volynski
 * @version 2.5
 * 23 June 2020
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



    public Item (Scanner s) throws NoItemException {
        String line = s.nextLine(); //name, aliases
        if (line.equals("===") || line.equals("---")) {
            throw new NoItemException();
        }
        String[] itemNameSplit = line.split(",");
        primaryName = itemNameSplit[0];

        for (int i = 1; i < itemNameSplit.length; i++) {
            aliases.add(itemNameSplit[i]);
        }

        line = s.nextLine();
        this.weight = Integer.parseInt(line);

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


    public boolean goesBy(String name) {
        return false;   //TODO implement
    }

    public String getPrimaryName() {
        return primaryName;
    }

    public String getMessageForVerb(String verb) {
        return "";  //TODO implement
    }

    public String toString() {
        return null;    //TODO implement
    }

    public int getWeight() {
        return weight;
    }
}

class NoItemException extends Exception {
    /**
     * NoItemException - default constructor
     */
    NoItemException() {
    }
}
