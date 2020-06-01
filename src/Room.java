/**
 * Room Class - represents every room in the dungeon (name, description), knows whether or not
 * the adventurer has already visited it. Also, the Room Class contains lists of Exits.
 * @author Richard Volynski
 * @version 1.0
 * 1 June 2020
 */




public class Room {
    public String getName() {
        return name;
    }

    private String name;

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private String desc;
    private boolean beenHere;

    Room (String name) {

    }
    String describe() {
        return ""; //TODO implement
    }
    Room leaveBy(String dir) {
        return null; //TODO return Room
    }

    public void addExit (Exit exit) {
        return;
    }
}
