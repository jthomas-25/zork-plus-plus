import java.util.ArrayList;

public class Pathfinding {


    // Bootleg dijkstra
    public ArrayList<Room> getPath(Room start, Room end) throws InterruptedException {
//        System.out.println("Start: " + start + "End: " + end);

        if (start == end) {
            ArrayList<Room> path = new ArrayList<>();
            path.add(start);
            return path;
        }

        for (Room room : GameState.instance().getDungeon().getRooms()) {

            // Get starting room
            if (room.getName().equals(start.getName())) {
                Room currRoom = room;
                ArrayList<String> visited = new ArrayList<>();
                visited.add(room.getName());
                ArrayList<Room> path = new ArrayList<>();
                ArrayList<Room> exits = new ArrayList<>();

                // Just loop till a path is found.  Not ideal for large dungeons, or complex path finding.
                // Adds a bit of randomness to the mix too.
                while (currRoom != end) {
//                    System.out.println("---");
//                    System.out.println(visited);
//                    System.out.println("---");
                    visited.add(currRoom.getName());
                    path.add(currRoom);
                    exits.clear();
                    for (Exit exit : currRoom.getExits().values()) {
                        exits.add(exit.getDest());
                    }
                    for (Room exit : exits) {
//                        System.out.println(exit);
                        if (exits.size() == 1 && visited.contains(exits.get(0).getName())) {
//                            System.out.println("dead end");
                            path.clear();
                            currRoom = start;
                        }

                        if (!visited.contains(exit.getName())) {
                            currRoom = exit;
                        }
                    }
                }
                return path;
            }
        }

        return null;
    }

}
