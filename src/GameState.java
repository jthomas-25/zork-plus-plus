import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Iterator;


/**
 * GameState Class - represents the current state of the game: which dungeon is being played
 * and what room the adventurer is currently in.
 * @author Object Oriented Optimists (OOO)
 * @version 2.6
 * 1 July 2020
 */
class GameState {
    private Dungeon dungeon = null;
    private Room currentRoom = null;
    private String dungeonDesc = "Welcome to the Dungeon. Enjoy but you won't come out how you came in!";
    private ArrayList<Item> inventory;
    private final int MAX_INVENTORY_WEIGHT = 40;
    private int inventoryWeight;
    private int score;
    private int health;
    private Hashtable<Integer, String> ranks;
    private Hashtable<Integer, String> healthMsgs;
    private boolean gameOver;
    private boolean playerWon;
    //private boolean playerLost;


    //Singleton instance of GameState class
    private static GameState single_instance = null;

    /**
     * This method returns single instance of GameState class.
     * @return single instance of GameState class
     * */
    static synchronized GameState instance() {
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
        health = 0;
        ranks = new Hashtable<>();
        healthMsgs = new Hashtable<>();

        setRank(0, "Rank 1");
        setRank(25, "Rank 2");
        setRank(50, "Rank 3");
        setRank(75, "Rank 4");
        setRank(100, "Rank 5");

        setHealthMsg(0, "Message 1");
        setHealthMsg(1, "Message 2");
        setHealthMsg(2, "Message 3");
        setHealthMsg(3, "Message 4");
        setHealthMsg(4, "Message 5");

        gameOver = false;
        playerWon = false;
        //playerLost = false;
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
     * This method gets the current room of the adventurer by getting GameState's currentRoom member variable.
     *
     * @return currentRoom Room object.
     */
    Room getAdventurersCurrentRoom() {
        return currentRoom;
    }

    /**
     * This method sets the current room of the adventurer by changing GameState's currentRoom member variable.
     *
     * @param room Room object, intended to be set as new current room.
     */
    void setAdventurersCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    /**
     * This method gets the dungeon of the GameState's currentRoom member variable.
     *
     * @return Dungeon
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
     * @param saveName String, sets the name of the save file.  Cannot contain the following characters. " / * < > ? | \ . :
     */
    void store(String saveName) throws IllegalSaveFormatException {
        try {
            File saveFile = new File(saveName + ".sav");
            PrintWriter printWriter = new PrintWriter(saveFile);
            printWriter.write("Zork III save data\n");
            dungeon.storeState(printWriter);
            printWriter.write("Adventurer:\n");
            printWriter.write("Current room: " + currentRoom.getName() + "\n");
            printWriter.write("Inventory: ");
            if (!inventory.isEmpty()) {
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
        if (!version.equals("Zork III")) {
            throw new IllegalSaveFormatException("Save file incompatible with current version of Zork (Zork III).");
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
        gameScanner.close();
    }

    /**
     * This method will add the passed Item to the player's inventory.
     */
    ArrayList<Item> getInventory() {
        return this.inventory;
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
     * This method will actually let you remove multiple items while iterating
     * over them, thereby avoiding a ConcurrentModificationException.
     * @param itr the iterator which will do the removing.
     */
    void removeFromInventory(Iterator<Item> itr, Item item) {
        itr.remove();
        inventoryWeight -= item.getWeight();
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
     * This method checks if the player's current inventory, plus the weight of the given object exceeds
     * the {@link #MAX_INVENTORY_WEIGHT MAX_INVENTORY_WEIGHT}.
     * If the Item will cause the player to exceed their {@link #MAX_INVENTORY_WEIGHT MAX_INVENTORY_WEIGHT}, the method
     * returns false.  Otherwise, it will return true.
     * @param item the item to check.
     */
    boolean inventoryWeightLimitReached(Item item) {
        return inventoryWeight + item.getWeight() > MAX_INVENTORY_WEIGHT;
    }

    int getInventoryWeight() {
        return inventoryWeight;
    }

    int getScore() {
        return score;
    }

    void setScore(int score) {
        this.score = score;
    }

    int getHealth() {
        return health;
    }

    void setHealth(int health) {
        this.health = health;
    }

    String getRank() {
        return ranks.get(this.score);
    }

    boolean rankAssigned() {
        return getRank() != null;
    }

    void setRank(int score, String scoreMsg) {
        ranks.put(score, scoreMsg);
    }

    String getHealthMsg() {
        return healthMsgs.get(this.health);
    }

    boolean healthInfoAvailable() {
        return getHealthMsg() != null;
    }

    void setHealthMsg(int health, String healthMsg) {
        healthMsgs.put(health, healthMsg);
    }

    boolean gameisOver() {
        return gameOver == true;
    }

    void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    boolean playerHasWon() {
        return playerWon == true;
    }

    void setPlayerWon(boolean playerWon) {
        this.playerWon = playerWon;
        //playerLost = !this.playerWon;
    }

/*
    boolean playerHasLost() {
        return playerLost == true;
    }

    void setPlayerLost(boolean playerLost) {
        this.playerLost = playerLost;
        playerWon = !this.playerLost;
    }
*/
}

/**
 * class IllegalDungeonFormatException is a custom exception
 */
class IllegalSaveFormatException extends Exception {

    /**
     * IllegalDungeonFormatException - default constructor
     */
    public IllegalSaveFormatException(String errorMsg) {
    }
}

