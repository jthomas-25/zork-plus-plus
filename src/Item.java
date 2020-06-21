/**
 * Item Class - An Item has a name, weight, and a Hashtable of verb/message pairs (called "messages").
 * @author Richard Volynski
 * @version 2.3
 * 21 June 2020
 */


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class Item {
    private String primaryName;
    private int weight;
    private Hashtable messages;
    private ArrayList<String> aliases;

    public Item (Scanner s) {
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
