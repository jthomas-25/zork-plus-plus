import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * Represents an in game item with unique attributes.  These attributes are represented as member variables
 * they include {@link #primaryName primary name}, {@link #itemHolder itemHolder} (for interaction) and {@link #aliases aliases}.
 * @author Robert Carroll
 * @author Richard Volynski
 * @version 3.2
 * 14 July 2020
 */
public class Item {
    private String primaryName;
    private int weight;
    private Hashtable<String, ItemHolder> itemHolder = new Hashtable<String, ItemHolder>();
    private ArrayList<String> aliases = new ArrayList<String>();
//    private ArrayList<Item> contents = new ArrayList<>();

    /**
     * Default constructor, takes a Scanner object in order to hydrate the item.
     * Assigning essential values to the member variables.
     */
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

        String lastVerb = null;

        line = s.nextLine();
        while (!line.equals("---")) {
            String[] commandParts = line.split(":");
            String event = "";
            String eventParameter = "";

            String verb = commandParts[0];
            if (commandParts.length == 1) {
                itemHolder.get(lastVerb).setMessage(itemHolder.get(lastVerb).getMessage() + "\n" + verb);
                line = s.nextLine();
                continue;
            }
            String message = commandParts[1];

            if (verb.contains("[") && verb.contains("]")) {

                if (verb.contains("(") && verb.contains(")")) {
                   String[] verbSplit = verb.split("\\[");
                   verb = verbSplit[0];

                   String[] verb2Split = verbSplit[1].split("\\(");
                   event = verb2Split[0];

                   String[] verb3split = verb2Split[1].split("\\)");
                   eventParameter = verb3split[0];

                    ItemHolder itemEvent = new ItemHolder(event, eventParameter, message);
                    itemHolder.put(verb, itemEvent);
                }
                else {
                    String[] verbSplit = verb.split("\\[");
                    verb = verbSplit[0];

                    String[] verb2Split = verbSplit[1].split("\\]");
                    event = verb2Split[0];

                    if (event.contains(",")) {
                        String[] eventToSplit = event.split(",");
                        for (String event0 : eventToSplit) {
                            ItemHolder itemEvent = new ItemHolder(event0, eventParameter, message);
                            itemHolder.put(verb, itemEvent);
                        }
                    }
                    else {
                        ItemHolder itemEvent = new ItemHolder(event, eventParameter, message);
                        itemHolder.put(verb, itemEvent);
                    }
                }
            }
            else {
                ItemHolder itemEvent = new ItemHolder(event, eventParameter, message);
                itemHolder.put(verb, itemEvent);
            }
            lastVerb = verb;
            line = s.nextLine();
        }
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
        return itemHolder.get(verb).getMessage();
    }

    /**
     * Gets the event for an Item that is associated with a given verb. The events indicates what happens
     * to the Item after being acted upon by the given verb.
     *
     * @param verb a verb that has an associated event within the Item's {@link #itemHolder itemHolder}
     * member variable.
     * @return a string, representing the event name for a given verb.
     */
    String getEventForVerb(String verb) {
        return itemHolder.get(verb).getEvent();
    }

    /**
     * Gets the event parameter for an Item that is associated with a given verb.
     *
     * @param verb a verb that has an associated event within the Item's {@link #itemHolder itemHolder}
     * member variable.
     * @return a string, representing the event parameter for a given verb.
     */
    String getEventParamForVerb(String verb) {
        return itemHolder.get(verb).getEventParameter();
    }

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
    private String event;
    private String eventParameter;
    private String message;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventParameter() {
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

    ItemHolder(String event, String eventParameter, String message) {
        this.event = event;
        this.eventParameter = eventParameter;
        this.message = message;
    }
}

