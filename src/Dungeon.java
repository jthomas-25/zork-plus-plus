/**
 * Dungeon Class - It holds on to a Hashtable collection of Room objects, and knows which one is the currentRoom point.
 * A Hashtable is a class that makes it easy to look up entries by a "key" rather than by a numbered index,
 * as an ArrayList does.
 * @author Richard Volynski
 * @version 1.5
 * 6 June 2020
 */


import java.util.ArrayList;

public class Dungeon {

    /**
     * getTitle - this method returns title
     * @return title;
     */
    public String getTitle() {
        return title;
    }

    private String title;
    private Room currentRoom;
    private ArrayList<Room> rooms = new ArrayList<Room>();


    /**
     * Dungeon
     * @param currentRoom - room the user is currently in
     * @param title - Dungeon description
     */
    public Dungeon(Room currentRoom, String title) {
        this.title = title;
        this.currentRoom = currentRoom;
    }

    /**
     * getCurrentRoom - this method returns the room the user is currently in
     */
    public Room getCurrentRoom() {
        return this.currentRoom;
    }

    /**
     * add - this method adds a room to Dungeon class
     * @param room
     */
    public void add (Room room) {
        rooms.add(room);
    }

    /**
     * getRoom - this method returns a Room by roomName
     * @param roomName
     * @return room found
     * */
    public Room getRoom(String roomName) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getName().equals(roomName)) {
                return rooms.get(i);
            }
        }
        return null;
    }
}

