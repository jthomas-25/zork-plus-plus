/**
 * Dungeon Class -  It holds on to a Hashtable collection of Room objects, and knows which one is the entry point.
 * A Hashtable is a class that makes it easy to look up entries by a "key" rather than by a numbered index,
 * as an ArrayList does.
 * @author Richard Volynski
 * @version 1.0
 * 1 June 2020
 */


public class Dungeon {
    public String getTitle() {
        return title;
    }

    private String title;
    public Dungeon(Room entry, String title) {
    }
    public Room getEntry() {
        return null; //TODO return Room
    }
    public void add (Room room) {

    }
    public Room getRoom(String roomName) {
        return null; //TODO return Room
    }
}
