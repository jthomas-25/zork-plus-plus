/**
 * Room Class - represents every room in the dungeon (name, description), knows whether or not
 * the adventurer has already visited it. Also, the Room Class contains lists of Exits.
 * @author Richard Volynski
 * @version 1.1
 * 3 June 2020
 */




public class Room {
    public String getName() {
        return name;
    }

    private String name;
    private boolean firstTimeWhenEnter = true;

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private String desc;
    private boolean beenHere;

    Room (String name) {
        this.name = name;
    }

    String describe() {
        if (this.firstTimeWhenEnter) {
            this.firstTimeWhenEnter = false;
            return desc;
        }
        else {
            return name;
        }
    }

    Room leaveBy(String dir) {
        return null; //TODO return Room
    }

    public void addExit (Exit exit) {
        return;
    }
}

