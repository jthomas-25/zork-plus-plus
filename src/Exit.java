/**
 * Exit Class - Each Room object holds on to an ArrayList of the Exits from it, and each Exit holds on to the
 * Room that it leads to. An Exit can also describe itself, which generates and returns a String message.
 * @author Richard Volynski
 * @version 1.4
 * 5 June 2020
 */



public class Exit {
    private String dir;

    public String getDir() {
        return dir;
    }

    private Room src;
    private Room dest;

    public Room getSrc() {
        return src;
    }
    public Room getDest() {
        return dest;
    }

    String describe() {
        return "You can go " + dest.getName();
    }

    public Exit (String dir, Room src, Room dest) {
        this.dir = dir;
        this.src = src;
        this.dest = dest;
    }
}

