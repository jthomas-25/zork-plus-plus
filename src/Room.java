/**
 * Room Class - represents every room in the dungeon (name, description), knows whether or not
 * the adventurer has already visited it. Also, the Room Class contains lists of Exits.
 * @author Object Oriented Optimists
 * @version 2.7
 * 25 June 2020
 */



import java.io.PrintWriter;
import java.util.*;

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
    private ArrayList<Item> contents = new ArrayList<>();

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
//    public Room (Scanner s) throws NoRoomException {
//        String line = s.nextLine();
//        if (line.equals("===")) {
//            throw new NoRoomException();
//        }
//        this.name = line;
//        line = s.nextLine();
//        if (line.contains("Contents: ")) {
//            String[] contents = line.split(" ");
//            String itemName = contents[1];
//
//        }
//        this.desc = line;
////        line = s.nextLine();
//    }


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
            output += exit.describe() + "\n";
        }

        for (Item i : contents) {
            output = output + "\n" + String.format("There is a(n) %s here.", i.getPrimaryName());
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

    /**
     * Room - constructor to initialize room content
     * @param s - Scanner
     * @param d - Dungeon
     * @param initState - initialized from .sav file or not
     */
    public Room(Scanner s, Dungeon d, boolean initState) throws NoRoomException, NoItemException {
        String line = s.nextLine();
        if (line.equals("===")) {
            throw new NoRoomException();
        }
        this.name = line;
        line = s.nextLine();
        if (line.contains("Contents: ")) {
            String[] contents = line.split(" ");
            String itemName = contents[1];
            String[] afterSplitItemName = itemName.split(",");
            for (int i = 0; i < afterSplitItemName.length; i++) {
                Item item = d.getItem(afterSplitItemName[i]);
                add(item);
            }
            line = s.nextLine();
        }
        this.desc = line;
//        line = s.nextLine();
    }


    /**
     * restoreState - restores game as it was saved
     * @param s - Scanner
     * @param d - Dungeon
     * @throws NoItemException
     */
    void restoreState(Scanner s, Dungeon d) throws NoItemException {
        restoreState(s);
        String line = s.nextLine();
        String[] splitLine = line.split(": ");
        if (splitLine[0].equals("Contents")) {
            String[] itemNames = splitLine[1].split(",");
            for (String itemName : itemNames) {
                Item item = d.getItem(itemName);
                this.add(item);
            }
            s.nextLine();   //Skip "---" delimiter
        }
    }


    /**
     * add - this method adds item to ArrayList contents
     * @param item
     */
    void add(Item item) {
        this.contents.add(item);
    }

    /**
     * This method will actually let you remove multiple items while iterating
     * over them, thereby avoiding a ConcurrentModificationException.
     * @param itr the iterator which will do the removing.
     */
    void remove(Iterator<Item> itr) {
        itr.remove();
    }

    /**
     * remove - this method removes items from ArrayList contents
     * @param item
     */
    void remove(Item item) {
        this.contents.remove(item);
    }


    /**
     * getItemNamed - this method checks if item matches item name
     * @param name - name of the item
     * @return item found
     */
    Item getItemNamed(String name) {
        for (Item content : contents) {
            if (content.goesBy(name)) {
                return content;
            }
        }
        return null;
    }

    /**
     * getContents - this method allows user to get contents in the Room
     * @return contents
     */
    ArrayList<Item> getContents() {
        return this.contents;
    }

    /**
     * setRoomDescriptionNeeded - this method sets a flag if room description is needed
     */
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

