/**
 * Dungeon Class - It holds on to a Hashtable collection of Room objects, and knows which one is the entry point.
 * A Hashtable is a class that makes it easy to look up entries by a "key" rather than by a numbered index,
 * as an ArrayList does.
 * @author Richard Volynski
 * @version 1.9
 * 15 June 2020
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Scanner;

public class Dungeon {

    /**
     * getTitle - this method returns title
     * @return title;
     */
    public String getTitle() {
        return title;
    }

    private String title;
    private Room entry;
    private ArrayList<Room> rooms = new ArrayList<Room>();
    private ArrayList<String> lines = new ArrayList<>();

    private String fileName;

    public Dungeon (String fileName) throws NoRoomException, NoExitException, FileNotFoundException {  //TODO implement
        File file = new File(fileName);
        
	System.out.println("Dungeon file is " + fileName + " File path: " + file.getAbsolutePath() +
                " File size " + file.length());
	
	Scanner stdin = new Scanner(file);

//        boolean firstLine = true;
//        boolean secondLine = false;
//        boolean thirdLine = false;
//        boolean fourthLine = false;

        int lineNumber = 0;

        while (stdin.hasNextLine()) {
            String line = stdin.nextLine();
            lineNumber++;
            if (lineNumber == 1) {
                this.title = line;
                continue;
            }
            if (lineNumber == 2) {
                if (line.equals("Zork II")) {
                    continue;
                }
                else {
                    System.out.println("Dungeon file is incompatible with the current version of Zork");
                    break;  //TODO exit gracefully
                }
            }

            if (lineNumber == 3) {
                if (line.equals("===")) {
                    continue;
                }
                else {
                    System.out.println("Third line is wrong in the Dungeon file");
                    break;  //TODO exit gracefully
                }
            }

            boolean firstRoom = true;

            if (lineNumber == 4) {
                if (line.equals("Rooms:")) {    //TODO implement
                    while (!line.equals("===")) {
                        Room room = new Room(stdin);
                        line = stdin.nextLine();
                        lineNumber +=3;
                        rooms.add(room);
                        if (firstRoom) {
                            this.entry = room;
                            firstRoom = false;
                        }
                    }
                }

                if (line.equals("===")) {
                    line = stdin.nextLine();
                    lineNumber++;
                    if (line.equals("Exits:")) {
                        while (!line.equals("===")) {

                            Exit exit = null;
                            try {
                                exit = new Exit(stdin,this);
                            }
                            catch (NoExitException ex) {
                                break;
                            }
                            line = stdin.nextLine();
                            Room exitSrc = exit.getSrc();
                            String exitSrcRoomName = exitSrc.getName();

                            for (int i = 0; i < rooms.size(); i++) {
                                Room currentRoom = rooms.get(i);
                                if (currentRoom.getName().equals( exitSrcRoomName)) {
                                    currentRoom.addExit(exit);
                                    break;
                                }
                            }

                            lineNumber+=4;
                        }
                    }
                }
            }
            lines.add(line);
        }
    }


    /**
     * Dungeon
     * @param entry - room the user is currently in
     * @param title - Dungeon description
     */
     Dungeon(Room entry, String title) {
        this.title = title;
        this.setEntry(entry);
     }

     private void init() {  //TODO implement
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

    public String getFileName() {
        return fileName;
    }

    void storeState(PrintWriter w) {   //TODO implement

    }

    void restoreState(Scanner r) {  //TODO implement

    }

    public Room getEntry() {
        return entry;
    }

    public void setEntry(Room entry) {
        this.entry = entry;
    }
}

