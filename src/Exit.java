/**
 * Exit Class - Each Room object holds on to an ArrayList of the Exits from it, and each Exit holds on to the
 * Room that it leads to. An Exit can also describe itself, which generates and returns a String message.
 * @author Richard Volynski
 * @version 1.0
 * 1 June 2020
 */



public class Exit {
    private String dir;

    public String getDir() {
        return dir;
    }

    public Room getSrc() {
        return null; //TODO return Room
    }
    public Room getDest() {
        return null; //TODO return Room
    }

    String describe() {
        return ""; //TODO return String
    }

    public Exit (String dir, Room src, Room dest) {
    }
}
