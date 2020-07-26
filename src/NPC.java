import java.util.ArrayList;

public class NPC {

    private String name;
    private Room loc;

    //Since there's only one type of NPC, will just hardcode it in.
    NPC(String name, String spawnLoc) {
        this.name = name;
        this.loc = GameState.instance().getDungeon().getRoom(spawnLoc);
    }

    private ArrayList<Room> findPath(String dest) throws InterruptedException {
        Pathfinding path = new Pathfinding();
        return path.getPath(this.loc, GameState.instance().getDungeon().getRoom(dest));
    }

    void moveTowards(String dest) throws InterruptedException {
        ArrayList<Room> path = this.findPath(dest);
        if (path.size() == 1) {
            assert true;
        } else {
            this.loc = path.get(1);
        }
    }

    boolean inSameRoom() {
        return this.loc == GameState.instance().getAdventurersCurrentRoom();
    }

    String sameRoom() throws InterruptedException {
        System.out.println("You feel the evil presence upon you now.");
        System.out.println("\n");
        System.out.println("You try to fight, to no avail.");
        System.out.println("\n");
        System.out.println("You succumb to the evil presence");
        System.out.println("\n");
        return new DieEvent().trigger("none");
    }

    public void setLoc(String roomName) {
        this.loc = GameState.instance().getDungeon().getRoom(roomName);
    }

    public Room getCurrRoom() {
        return this.loc;
    }

    public String getLogistics(String noun) throws InterruptedException {
        return "There is an " + noun + " " + this.findPath(GameState.instance().getAdventurersCurrentRoom().getName()).size() + " rooms away...";
    }

}
