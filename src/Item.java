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
    private Hashtable<String, String> messages = new Hashtable<String, String>();
    private ArrayList<String> aliases = new ArrayList<String>();


    Item (Scanner s) throws NoItemException {
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

        line = s.nextLine();
        while (!line.equals("---")) {
            String[] commandParts = line.split(":");
            String verb = commandParts[0];
            String message = commandParts[1];
            messages.put(verb, message);
            line = s.nextLine();
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


    boolean goesBy(String name) {
        return primaryName.equals(name) || aliases.contains(name);
    }

    String getPrimaryName() {
        return primaryName;
    }

    String getMessageForVerb(String verb) {
        return messages.get(verb);
    }

    public String toString() {
        return primaryName;
    }

    int getWeight() {
        return weight;
    }
}

class NoItemException extends Exception {
    /**
     * NoItemException - default constructor
     */
    NoItemException() {
    }

    NoItemException(String message) {
        super(message);
    }
}
