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


    //Singleton instance of GameState class
    private static GameState single_instance = null;

    /**
     * instance - this method returns single instance of GameState class
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
     * getAdventurersCurrentRoom - this method returns the current room the user is in. The room updates as the
     * user moves.
     * @return currentRoom
     */
    Room getAdventurersCurrentRoom() {
        return currentRoom;
    }

    /**
     * setAdventurersCurrentRoom - this method sets the current room the user is in. The room updates as the
     * user moves.
     * @param room - new current room
     */
    void setAdventurersCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    /**
     * getDungeon - this method gets the Dungeon
     */
    Dungeon getDungeon() {
        return dungeon;
    }

    /**
     * store - this method stores the input from the file so that when the user saves the game, the user wll be
     * able to resume playing where they left off.
     * @param saveName
     * @exception IOException
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
     * restore - this method restores the game at the state which it was saved.
     * @param fileName
     * @exception IllegalSaveFormatException
     * @exception NoExitException
     * @exception FileNotFoundException
     * */
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

    ArrayList<Item> getInventory() {
        return this.inventory;
    }

    void addToInventory(Item item) {
        this.inventory.add(item);
        inventoryWeight += item.getWeight();
    }

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

    Item getItemFromInventoryNamed(String name) throws NoItemException {
        for (Item item : inventory) {
            if (item.goesBy(name)) {
                return item;
            }
        }
        throw new NoItemException(String.format("You're not carrying a(n) %s.", name));
    }

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

