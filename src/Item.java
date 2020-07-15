import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * Represents an in game item with unique attributes.  These attributes are represented as member variables
 * they include {@link #primaryName primary name}, {@link #itemHolder itemHolder} (for interaction) and {@link #aliases aliases}.
 * @author Robert Carroll
 * @author Richard Volynski
 * @version 3.3
 * 15 July 2020
 */
public class Item {
    private String primaryName;
    private int weight;
    private Hashtable<String, ItemHolder> itemHolder = new Hashtable<>();
    private Hashtable<String, String[]> eventStringListHolder = new Hashtable<>();
    private ArrayList<String> aliases = new ArrayList<String>();
    private Hashtable<String, String> messages = new Hashtable<>();
//    private ArrayList<Item> contents = new ArrayList<>();

    /**
     * Default constructor, takes a Scanner object in order to hydrate the item.
     * Assigning essential values to the member variables.
     */
    Item(Scanner s) throws NoItemException {
        String line = s.nextLine(); //name, aliases
        if (line.equals("===") || line.equals("---")) {
            throw new NoItemException();
        }
        String[] itemNameSplit = line.split(",");
        primaryName = itemNameSplit[0];

        for (int i = 1; i < itemNameSplit.length; i++) {
            aliases.add(itemNameSplit[i]);
        }

        try {
            line = s.nextLine();
            this.weight = Integer.parseInt(line);
        } catch (Exception e) {
            throw new NoItemException("Item weight required.");
        }

        line = s.nextLine();
        while (!line.equals("---")) {
            String itemCommandString = line;
            if (!hasCorrectSyntax(itemCommandString)) {
                throw new NoItemException("Usage: verb:message | verb[eventName]:message | verb[eventName(eventParameter)]:message");
            } else {
                String[] commandParts = line.split(":");
                String verbString = commandParts[0];
                String message = commandParts[1];

                String[] verbSplit = verbString.split("\\[");
                String verb = verbSplit[0];
                //System.out.println("Verb: " + verb);
                messages.put(verb, message);

                //System.out.println(verbString);
                boolean hasEvents = verbSplit.length > 1;
                if (hasEvents) {
                    String eventListString = verbSplit[1].replace("]", "");
                    String[] eventStrings = eventListString.split(",");
/*
                    //Debugging loop
                    for (String eventString : eventStrings) {
                        System.out.println("\tEvent string: " + eventString);
                    }
*/
                    eventStringListHolder.put(verb, eventStrings);
                }
                //System.out.println("Message: " + message);
                line = s.nextLine();
            }
        }
    }

    private static boolean hasCorrectSyntax(String itemCommandString) {
        String verb = itemCommandString.split(":")[0];
        //String message = itemCommandString.split(":")[1];

        if (!containsBrackets(verb)) {
            return true;
        } else {
            int leftBracketPos = verb.indexOf("[");
            int rightBracketPos = verb.indexOf("]");
            boolean containsClosedBrackets = leftBracketPos < rightBracketPos;
            if (containsClosedBrackets) {
                String eventListString = verb.substring(leftBracketPos + 1, rightBracketPos);
                String[] eventStrings = eventListString.split(",");
                for (String eventString : eventStrings) {
                    if (containsParens(eventString)) {
                        if (!containsClosedParens(eventString)) {
                           return false;
                        }
                    }
                }
                return true;
                    
            } else {
                return false;
            }
        }
    }

    static boolean containsBrackets(String s) {
        return s.contains("[") && s.contains("]");
    }
    static boolean containsParens(String s) {
        return s.contains("\\(") && s.contains(")");
    }
    static boolean containsClosedParens(String s) {
        int leftParenPos = s.indexOf("\\(");
        int rightParenPos = s.indexOf(")");
        return leftParenPos < rightParenPos;
    }

//    /**
//     * This method stores ess
//     * @param w a PrintWriter
//     */
//    void storeState(PrintWriter w) {
//        String primaryNameAndAliases = getPrimaryName();
//
//        for (int i = 0; i < aliases.size(); i++) {
//            primaryNameAndAliases+= "," + aliases.get(i);
//        }
//        w.write(primaryNameAndAliases + "\n");
//        w.write(Integer.toString(weight) + "\n");
//        Iterator keys = itemHolder.keySet().iterator();
//        while (keys.hasNext()) {
//            String key = (String) keys.next();
//            String value = (String) itemHolder.get(key);
//            w.write(key + ":" + value + "\n");
//        }
//        w.write("---" + "\n");
//    }


    /**
     * Checks to see if the Item goes by a given alias. It achieves this by checking if the {@link #aliases aliases}
     * ArrayList member variable contains a string equaling the given name.
     *
     * @param name a string, representing the alias one wishes to check.
     */
    boolean goesBy(String name) {
        return primaryName.equals(name) || aliases.contains(name);
    }

    /**
     * Gets the primary name of an Item.  The primary name is a string, member variable, called {@link #primaryName primaryName}.
     *
     * @return {@link #primaryName primaryName} member variable.
     */
    String getPrimaryName() {
        return primaryName;
    }

    /**
     * Gets the message for an Item that is associated with a given verb. The message indicates what happens
     * to the Item after being acted upon by the given verb.
     *
     * @param verb a verb that has an associated message within the Item's {@link #itemHolder itemHolder}
     * member variable.
     * @return a string, representing the message for the given verb.
     */
    String getMessageForVerb(String verb) {
        return messages.get(verb);
/*
        if (itemHolder.get(verb) != null) {
            return itemHolder.get(verb).getMessage();
        } else {
            return null;
        }
*/
    }

    String[] getEventStrings(String verb) {
        return eventStringListHolder.get(verb);
    }

    /**
     * Gets the event for an Item that is associated with a given verb. The events indicates what happens
     * to the Item after being acted upon by the given verb.
     *
     * @param verb a verb that has an associated event within the Item's {@link #itemHolder itemHolder}
     * member variable.
     * @return a string, representing the event name for a given verb.
     */
/*
    String getEventName(String verb) {
        if (itemHolder.get(verb) != null) {
            return itemHolder.get(verb).getEventName();
        } else {
            return null;
        }
    }
*/
    /**
     * Gets the event parameter for an Item that is associated with a given verb.
     *
     * @param verb a verb that has an associated event within the Item's {@link #itemHolder itemHolder}
     * member variable.
     * @return a string, representing the event parameter for a given verb.
     */
/*
    String getEventParam(String verb) {
        if (itemHolder.get(verb) != null) {
            return itemHolder.get(verb).getEventParameter();
        } else {
            return null;
        }
    }
*/
    /**
     * This method replaces the default toString method, returning a string
     * of the item's {@link #primaryName primary name}.
     *
     * @return a string, representing the {@link #primaryName primary name} of an Item.
     */
    public String toString() {
        return primaryName;
    }

    /**
     * This method returns the weight of an Item.  The weight is represented by the
     * {@link #weight weight} member variable of an Item.
     *
     * @return an int, representing the {@link #weight weight} of an Item.
     */
    int getWeight() {
        return weight;
    }

//    /**
//     * This method returns the contents of an Item.  The contents of the Item are other Items.
//     * This can be useful for items such as "chests" or "swords" where they may be comprised of things such as:
//     * a blade, hilt, diamond, etc.
//     *
//     * @return an arraylist, representing the {@link #contents contents} of an Item.
//     */
//    ArrayList<Item> getContents() {
//        return this.contents;
//    }
//    TODO: hydrate item contents

//    /**
//     * Adds given Item to the {@link #contents contents} of this Item.
//     * @param item an Item to be added to {@link #contents contents}.
//     */
//    void addToContents(Item item) {
//        this.contents.add(item);
//    }

//    /**
//     * Removes given Item from the {@link #contents contents} of this Item.
//     * @param itemName name of item to remove from {@link #contents contents}.
//     */
//    void removeFromContents(String itemName) {
//
//        for (Item item : this.contents) {
//            if (item.getPrimaryName().equals(itemName) || item.goesBy(itemName)) {
//                this.contents.remove(item);
//            }
//        }
//
//    }

    boolean hasItemSpecificCommand (String itemSpecificCommand) {
        return itemHolder.containsKey(itemSpecificCommand);
    }
}

/**
 * Thrown when a method attempts to return an {@link Item} where none can be found.
 * @author John Thomas
 * @version 1.0
 */
class NoItemException extends Exception {

    /**
     * Constructs a new NoItemException with no message.
     */
    NoItemException() {
    }

    /**
     * Constructs a new NoItemException with the given message.
     * @param message the message to be printed if this exception is thrown
     */
    NoItemException(String message) {
        super(message);
    }
}

class ItemHolder {
    private String eventName;
    private String eventParameter;
    private String message;
    
    ItemHolder(String eventName, String eventParameter, String message) {
        this.eventName = eventName;
        this.eventParameter = eventParameter;
        this.message = message;
    }
    
    ItemHolder(String message) {
        this.message = message;
    }
    
    public String getEventName(String eventName) {
        return eventName;
    }
    
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    
    public String getEventParameter(String verb) {
        return eventParameter;
    }
    
    public void setEventParameter(String eventParameter) {
        this.eventParameter = eventParameter;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
