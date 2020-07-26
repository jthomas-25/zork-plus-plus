import java.lang.reflect.Array;
import java.util.ArrayList;

public class Pathfinding {


    // Bootleg dijkstra
    public ArrayList<Room> getPath(Room start, Room end) throws InterruptedException {

        if (start == end) {
            ArrayList<Room> path = new ArrayList<>();
            path.add(start);
            return path;
        }

        for (Room room : GameState.instance().getDungeon().getRooms()) {
            Room currRoom = room;
            ArrayList<String> marked = new ArrayList<>();
            ArrayList<String> path = new ArrayList<>();
            // Get starting room
            if (room.getName().equals(start.getName())) {

                path.add(room.getName());

                while (currRoom != end) {

//                    System.out.println("Starting While Loop");
//                    System.out.println("===================");
//                    System.out.println("Current Room: " + currRoom);

//                    Thread.sleep(1000);

                    ArrayList<String> destinations = new ArrayList<>();


                    for (Exit exit : currRoom.getExits().values()) {
                        String exitString = exit.getDest().getName();
//                        System.out.println("if " + path + " doesn't contain " + exitString);
                        if (!path.contains(exitString) && !marked.contains(exitString)) {
                            destinations.add(exitString);
                        }
                    }

//                    System.out.println(("Path: " + path));
//                    System.out.println("Destinations: " + destinations);

                    if (destinations.size() == 0) {
                        marked.add(currRoom.getName());
                        path.clear();
                    }

                    else {

//                        System.out.println("Setting new room to: " + destinations.get(0));
                        currRoom = GameState.instance().getDungeon().getRoom(destinations.get(0));
                        path.add(destinations.get(0));

                    }

//                    System.out.println(".");

                }

            ArrayList<Room> finalPath = new ArrayList<>();
            for (String roomName : path) {
                finalPath.add(GameState.instance().getDungeon().getRoom(roomName));

            }


            return finalPath;


            }
        }

        return null;
    }

}
