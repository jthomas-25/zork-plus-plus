import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * Represents an in game item with unique attributes.  These attributes are represented as member variables
 * they include {@link #primaryName primary name}, and {@link #aliases aliases}.
 * @author Robert Carroll
 * @author Richard Volynski
 * @version 3.4
 * 16 July 2020
 */
public class Item {
    private String primaryName;
    private int weight;
    private Hashtable<String, String[]> eventStringListHolder = new Hashtable<>();
    private ArrayList<String> aliases = new ArrayList<String>();
    private Hashtable<String, String> messages = new Hashtable<>();

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

        String verb = "";
        while (!line.equals("---")) {
            String itemCommandString = line;
            if (!hasCorrectSyntax(itemCommandString)) {
                throw new NoItemException("Usage: verb:message | verb[eventName]:message | verb[eventName(eventParameter)]:message");
            } else {
                String[] commandParts = line.split(":");
                String verbString = commandParts[0];
                String message = "";

                if (commandParts.length == 1) {
                    message = messages.get(verb) + "\n" + verbString;
                    messages.put(verb,message);
                }
                else {
                    message = commandParts[1];
                }

                String[] verbSplit = verbString.split("\\[");
                verb = verbSplit[0];
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

    /**
     * Checks to see if the Item goes by a given alias.
     *
     * @param name the alias one wishes to check
     */
    boolean goesBy(String name) {
        return primaryName.equals(name) || aliases.contains(name);
    }

    /**
     * Gets the primary name of this item.
     *
     * @return the item's primary name that the game prints
     */
    String getPrimaryName() {
        return primaryName;
    }

    /**
     * Gets the message for this item that is associated with a given verb. The message indicates what happens
     * to the item after being acted upon by the given verb.
     *
     * @param verb a verb that has an associated message
     * @return the message for the given verb
     */
    String getMessageForVerb(String verb) {
        return messages.get(verb);
    }

    String[] getEventStrings(String verb) {
        return eventStringListHolder.get(verb);
    }

    boolean hasEvents(String verb) {
        return getEventStrings(verb) != null;
    }

    /**
     * Returns a string representation of this item.
     *
     * @return a string representation of this item consisting of its primary name
     */
    public String toString() {
        return primaryName;
    }

    /**
     * Returns the weight of this item.
     *
     * @return an int, representing the {@link #weight weight} of an Item.
     */
    int getWeight() {
        return weight;
    }

    boolean hasItemSpecificCommand (String itemSpecificCommand) {
//        return eventStringListHolder.containsKey(itemSpecificCommand);
        return messages.containsKey(itemSpecificCommand);
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

/**
 * Represents an {@link Item} that can hold other {@link Item}s. This class is useful for items
 * such as "chests" or "swords" which may be comprised of things such as:
 * a blade, hilt, diamond, etc.
 */
class ItemContainer extends Item {
    private ArrayList<Item> contents;
    
    /**
     * Constructs a new ItemContainer, using the given Scanner for hydration.
     * @param s the Scanner object that will hydrate this container's contents
     * @throws NoItemException 
     */
    ItemContainer(Scanner s) throws NoItemException {
        super(s);
        contents = new ArrayList<>();
        //TODO: hydrate item contents
    }
    
    /**
     * Returns the contents of this container.
     * @return an arraylist, representing the items this container holds
     */
    ArrayList<Item> getContents() {
        return this.contents;
    }

    /**
     * Adds given {@link Item} to the {@link #contents contents} of this container.
     * @param item an {@link Item} to be added to {@link #contents contents}.
     */
    void addToContents(Item item) {
        this.contents.add(item);
    }

    /**
     * Removes given {@link Item} from the {@link #contents contents} of this container.
     * @param itemName name of item to remove from {@link #contents contents}
     */
    void removeFromContents(String itemName) {
        for (Item item : this.contents) {
            if (item.goesBy(itemName)) {
                this.contents.remove(item);
                break;
            }
        }

    }
}
