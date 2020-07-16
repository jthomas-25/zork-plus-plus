import java.io.*;
import java.util.*;


/**
 * GameState Class - represents the current state of the game: which dungeon is being played
 * and what room the adventurer is currently in.
 * @author Object Oriented Optimists (OOO)
 * @author John Thomas
 * @author Richard Volynski
 * @version 3.3
 * 16 July 2020
 */
class GameState {
    private final String VERSION = "Zork++";
    private Dungeon dungeon = null;
    private Room currentRoom = null;
    private String dungeonDesc = "Welcome to the Dungeon. Enjoy but you won't come out how you came in!";
    private ArrayList<Item> inventory;
    private final int MAX_INVENTORY_WEIGHT = 500000;
    private int inventoryWeight;
    private int score;
    private int health;
    private Hashtable<Integer, String> ranks;
    private Hashtable<Integer, String> healthMsgs;
    private boolean gameOver;
    private boolean playerDead;


    //Singleton instance of GameState class
    private static GameState single_instance = null;

    /**
     * This method returns single instance of GameState class.
     * @return single instance of GameState class
     * */
    public static synchronized GameState instance() {
        if (single_instance == null)
            single_instance = new GameState();
        return single_instance;
    }

    /**
     * Default Constructor GameState
     */
    private GameState() {
        inventory = new ArrayList<>();
        inventoryWeight = 0;
        score = 0;
        health = 5;   //initial health
        ranks = new Hashtable<>();
        healthMsgs = new Hashtable<>();

        setRank(0, "Amateur Scout");
        setRank(10, "Future Hunter");
        setRank(20, "Treasure Expert");
        setRank(30, "\"How did you get this far\" Adventurer");
        setRank(40, "Dungeon Tour Guide");

        setHealthMsg(5,   "You are perfectly healthy! Keep it up!");
        setHealthMsg(4,   "You are almost at perfect health. Do something!!");
        setHealthMsg(3,  "You are still capable of doing everyday activities.");
        setHealthMsg(2, "You still have chances to survive.");
        setHealthMsg(1,   "You are mortally wounded and need to find medicine.");
        setHealthMsg(0,  "You are dead!");

        gameOver = false;
        playerDead = false;
    }
    
    String getVersion() {
        return VERSION;
    }

    /**
     * initialize - This method initializes the dungeon and name of the starting room.
     * @param dungeon - controls entire game function
     */
    void initialize(Dungeon dungeon) {
//        this.dungeon = new Dungeon(currentRoom, dungeonDesc);
        this.dungeon = dungeon;
        this.currentRoom = dungeon.getEntry();
    }

    /**
     * This method gets the current room of the adventurer by getting GameState's {@link #currentRoom currentRoom} member variable.
     * The {@link #currentRoom currentRoom} is a Room that represents the player's current location.
     *
     * @return currentRoom Room object.
     */
    Room getAdventurersCurrentRoom() {
        return currentRoom;
    }

    /**
     * This method sets the current room of the adventurer by changing GameState's currentRoom member variable.
     * The {@link #currentRoom currentRoom} is a Room that represents the player's current location.
     * @param room Room object, intended to be set as new current room.
     */
    void setAdventurersCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    /**
     * This method gets the {@link #dungeon dungeon} of the GameState.
     * The {@link #dungeon dungeon} is a member variable, representing the dungeon in play.
     *
     * @return dungeon, a Dungeon belonging to GameState.
     */
    Dungeon getDungeon() {
        return dungeon;
    }

    /**
     * This method stores essential GameState member variables into a file, ending in (dot)sav.
     * If the saveName parameter is a legal file name string, this method stores several GameState member variables
     * in the file, line by line.
     * A file generated with this method can be restored using the {@link #restore restore} method.
     *
     * @param saveName String, sets the name of the save file.  Cannot contain the following characters. " / * &lt; &gt; ? | \ . :
     */
    void store(String saveName) throws IllegalSaveFormatException {
        try {
            File saveFile = new File(saveName + ".sav");
            PrintWriter printWriter = new PrintWriter(saveFile);
            printWriter.write("Zork++ save data\n");
            dungeon.storeState(printWriter);
            printWriter.write("Adventurer:\n");
            printWriter.write("Current room: " + currentRoom + "\n");
            if (!inventory.isEmpty()) {
                printWriter.write("Inventory: ");
                for (int i = 0; i < inventory.size(); i++) {
                    Item item = inventory.get(i);
                    printWriter.write(item.getPrimaryName());
                    if (i < inventory.size() - 1) {
                        printWriter.write(",");
                    }
                }
                printWriter.write("\n");
            }
            printWriter.flush();
            printWriter.close();
        }
        catch (Exception ex) {
            throw new IllegalSaveFormatException("Failed to save game state.");
        }
    }

    /**
     * This method restores essential GameState member variables from the requested file.
     * If the file exists, this method will set several GameState member variables to those found within the file.
     * A (dot)sav compatible file can be generated with the {@link #store(String) store} method.
     *
     * @param fileName String, the (dot)sav file to restore.
     */
    void restore(String fileName) throws FileNotFoundException, NoRoomException, IllegalDungeonFormatException, IllegalSaveFormatException {
        File file = new File(fileName);
        Scanner gameScanner = new Scanner(file);
        String version = gameScanner.nextLine().split(" save data")[0];
        if (!version.equals(VERSION)) {
            throw new IllegalSaveFormatException("Save file incompatible with current version of Zork (" + VERSION + ").");
        }
        String secLine = gameScanner.nextLine();  //reads Dungeon file name
        String[] secLineSplit = secLine.split(": ");
        String zorkFileName = secLineSplit[1];
        dungeon = new Dungeon(zorkFileName, false);
        initialize(dungeon);

        dungeon.restoreState(gameScanner);

        gameScanner.nextLine(); //Skip "Adventurer:" line
        String currentRoomLine = gameScanner.nextLine();
        String[] currentRoomSplit = currentRoomLine.split(": ");
        String currentRoomName = currentRoomSplit[1];
        currentRoom = dungeon.getRoom(currentRoomName);
        dungeon.setEntry(currentRoom);
        if (gameScanner.hasNextLine()) {
            String[] splitLine = gameScanner.nextLine().split(": ");
            if (splitLine[0].equals("Inventory")) {
                try {
                    String[] itemNames = splitLine[1].split(",");
                    for (String itemName : itemNames) {
                        Item item = dungeon.getItem(itemName);
                        this.addToInventory(item);
                    }
                } catch (Exception e) {
                }
            }
        }
        gameScanner.close();
    }

    /**
     * This method gets the {@link #inventory inventory} of GameState, a member variable, arraylist of Item(s).
     *
     * @return ArrayList of Item objects.
     */
    ArrayList<Item> getInventory() {
        return this.inventory;
    }

    boolean hasItemSpecificCommand(String command) {
        for (Item item : getInventory()) {
            if (item.hasItemSpecificCommand(command)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method will add the passed Item to the player's inventory.
     * @param item The item to be added to the inventory.
     */
    void addToInventory(Item item) {
        this.inventory.add(item);
        inventoryWeight += item.getWeight();
    }
    /**
     * This method will remove the passed Item from the player's {@link #inventory inventory}.
     * @param item The item to be removed from inventory.
     */
    void removeFromInventory(Item item) {
        this.inventory.remove(item);
        inventoryWeight -= item.getWeight();
    }
    /**
     * Removes multiple items from the player's inventory while iterating over them, thereby
     * avoiding a ConcurrentModificationException.
     * @param itr the iterator which will do the removing
     * @param item the item to be removed from the inventory
     */
    void removeFromInventory(Iterator<Item> itr, Item item) {
        itr.remove();
        inventoryWeight -= item.getWeight();
    }

    /**
     * This method will remove the passed Item from the player's {@link #inventory inventory}.
     * @param itemName The item to be removed from inventory.
     */
    void removeFromInventory(String itemName) throws NoItemException {
        try {
            Item item = getItemFromInventoryNamed(itemName);
            removeFromInventory(item);
        } catch (NoItemException e) {
        }
    }

    /**
     * This method attempts to return a requested Item in the vicinity of the player.
     * The current vicinity is represented by the player's current {@link #inventory inventory} and the player's
     * {@link #currentRoom current room}.
     * @param name a string, containing the name of the desired item in the vicinity.
     */
    Item getItemInVicinityNamed(String name) throws NoItemException {
        Item item = currentRoom.getItemNamed(name);
        if (item == null) {
            try {
                item = getItemFromInventoryNamed(name);
                return item;
            } catch (NoItemException e) {
                throw e;
            }
        } else {
            return item;
        }
    }

    /**
     * This method attempts to return a requested Item within the player's {@link #inventory inventory}.
     * If there is an Item in the player's inventory with the requested name, the Item is returned.
     * If there is no Item in the player's inventory with the requested name, this method throws a
     * NoItemException exception and will return nothing.
     * @param name a string, containing the name of the desired item in player's inventory.
     */
    Item getItemFromInventoryNamed(String name) throws NoItemException {
        for (Item item : inventory) {
            if (item.goesBy(name)) {
                return item;
            }
        }
        throw new NoItemException(String.format("You're not carrying a(n) %s.", name));
    }

    /**
     * This method checks if an item exists in the user's inventory.
     * @param name a string, containing the name of the desired item in player's inventory.
     */
    boolean itemExistsInInventory(String name) {
        Item item = null;
        try {
            item = getItemFromInventoryNamed(name);
        } catch (NoItemException e) {
        }
        return item != null;
    }

    /**
     * This method checks if the player's current inventory, plus the weight of the given object exceeds
     * the {@link #MAX_INVENTORY_WEIGHT MAX_INVENTORY_WEIGHT}.
     * If the Item will cause the player to exceed their {@link #MAX_INVENTORY_WEIGHT MAX_INVENTORY_WEIGHT}, the method
     * returns false.  Otherwise, it will return true.
     * @param item the item to check.
     */
    boolean inventoryWeightLimitReached(Item item) {
        return inventoryWeight + item.getWeight() > MAX_INVENTORY_WEIGHT;
    }

    /**
     * Gets the {@link #score score} of GameState.  A member variable, representing the player's score.
     *
     * @return Int, representing the current score.
     */
    int getScore() {
        return score;
    }

    /**
     * Sets the {@link #score score} of GameState.  A member variable, representing the player's score.
     *
     * @param score integer intended to be new score.
     */
    void setScore(int score) {
        this.score = score;
    }

    /**
     * Gets the {@link #health health} of GameState.  A member variable, representing the player's health.
     *
     * @return integer, representing the player's health.
     */
    int getHealth() {
        return health;
    }

    /**
     * Sets the {@link #health health} of GameState.  A member variable, representing the player's health.
     *
     * @param health integer intended to be the new health of player.
     */
    void setHealth(int health) {
        if (health < 0) {
            health = 0;
        }
        this.health = health;
    }

    /**
     * Gets a String, declaring the rank of the player, based on their current {@link #score score}.
     *
     * @return String, the current rank of player, based on the current {@link #score score}.
     */
    String getRank() {
        if (this.score < 10) {
            return ranks.get(0);
        } else if (this.score < 20) {
            return ranks.get(10);
        } else if (this.score < 30) {
            return ranks.get(20);
        } else if (this.score < 40) {
            return ranks.get(30);
        } else {
            return ranks.get(40);
        }
    }

    /**
     * Checks to see if {@link #getRank() getRank} returns null.
     * If {@link #getRank() getRank} is not null, this method will return true.  If {@link #getRank() getRank} is null,
     * this method returns false.
     *
     * @return boolean.
     */
    boolean rankAssigned() {
        return getRank() != null;
    }

    /**
     * Adds a rank to the {@link #ranks ranks} hashtable.  A rank represents the player's current {@link #score score}
     * as a message, with lower scores corresponding with lower ranks and higher scores with higher ranks.
     *
     * @param score Integer, represents the max score necessary for obtaining a certain rank.
     * @param scoreMsg String, representing a rank to be added.
     */
    private void setRank(int score, String scoreMsg) {
        ranks.put(score, scoreMsg);
    }

    /**
     * Gets an appropriate message, corresponding to the player's current {@link #health health}.
     * Lower health will return more dire messages, while higher or full health will indicate
     * the player is fine.
     *
     * @return String, representing the player's {@link #health health} as a message.
     */
    String getHealthMsg() {

//        Enumeration<Integer>
//        for (Integer healthBound : healthMsgs.keys())
//        }
        return healthMsgs.get(this.health);
    }

    /**
     * Checks to see if {@link #getHealthMsg() getHealthMsg} returns null.
     * If {@link #getHealthMsg() getHealthMsg} is not null, this method will return true.  If {@link #getRank() getRank} is null,
     * this method returns false.
     *
     * @return boolean, will return true if {@link #getHealthMsg() getHealthMsg} returns anything but null.
     */
    boolean healthInfoAvailable() {
        return getHealthMsg() != null;
    }


    /**
     * Adds a health message to the {@link #healthMsgs healthMsgs} hashtable.  A health message
     * represents the player's current {@link #health health} as a message.
     *
     * @param health Integer, represents the max health necessary for obtaining a certain health message.
     * @param healthMsg String, representing the health message to be added.
     */
    void setHealthMsg(int health, String healthMsg) {
        healthMsgs.put(health, healthMsg);
    }

    /**
     * Checks to see if the game has ended.
     * @return true if and only if the game has ended, false otherwise
     */
    boolean gameIsOver() {
        return gameOver == true;
    }

    /**
     * Checks to see if a {@link WinEvent} has been triggered.
     * @return true if and only if the game is over and the player is not dead, false otherwise
     */
    boolean playerHasWon() {
        return gameOver && !playerDead;
    }

    void killPlayer() {
        health = 0;
        playerDead = true;
    }

    /**
     * Causes the game to be over. Does nothing if the game is already over.
     */
    void endGame() {
        if (!gameOver) {
            gameOver = true;
        }
    }
}

/**
 * Thrown by any method that parses a .sav file if the format of
 * the save file is illegal (not properly formatted).
 * @author John Thomas
 */
class IllegalSaveFormatException extends Exception {

    /**
     * Constructs a new exception with the given error message.
     * @param errorMsg the detailed message to be printed when this exception is thrown
     */
    IllegalSaveFormatException(String errorMsg) {
    }
}
