/**
 * Dungeon Class - It holds on to a Hashtable collection of Room objects, and knows which one is the entry point.
 * A Hashtable is a class that makes it easy to look up entries by a "key" rather than by a numbered index,
 * as an ArrayList does.
 * @author Richard Volynski
 * @version 1.2
 * 4 June 2020
 */


import java.util.ArrayList;

public class Dungeon {
    public String getTitle() {
        return title;
    }

    private String title;
    private Room entry;
    private ArrayList<Room> rooms = new ArrayList<Room>();


    public Dungeon(Room entry, String title) {
        this.title = title;
        this.entry = entry;
    }

    public Room getEntry() {
        return this.entry; //TODO return Room
    }
    public void add (Room room) {
        rooms.add(room);

    }

    public Room getRoom(String roomName) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getName().equals(roomName)) {
                return rooms.get(i);
            }
        }
        return null; //TODO return Room
    }
}

