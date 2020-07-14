/**
 * CommandFactory Class - A factory class whose purpose is to parse text strings
 * and produce the appropriate Command objects.
 * CommandFactory is a Singleton class.
 * @author Object Oriented Optimists (OOO)
 * @version 3.2
 * 14 July 2020
 */
class CommandFactory {
    private static CommandFactory single_instance = null;

    /**
     * instance() - this method is represented by the Singleton CommandFactory Class
     * @return single static instance of CommandFactory class
     */
    static synchronized CommandFactory instance() {
        if (single_instance == null)
            single_instance = new CommandFactory();
        return single_instance;
    }

    /**
     * CommandFactory - default constructor
     */
    private CommandFactory() {
    }

    /**
     * parse - this method parses entered commands and produces Command objects
     * @param commandString - user input
     * @return - Command objects
     */
    Command parse (String commandString) {
        String[] words = commandString.split(" ");
        switch (words.length) {
            case 1:
                switch (words[0]) {
                    case "n":
                    case "s":
                    case "w":
                    case "e":
                    case "u":
                    case "d":
                        return new MovementCommand(words[0]);
                    case "look": case "l":
                        return new LookCommand();
                    case "take":
                        return new TakeCommand("");
                    case "drop":
                        return new DropCommand("");
                    case "i": case "inventory":
                        return new InventoryCommand();
                    case "q": case "quit":
                        return new QuitCommand();
                    case "score":
                        return new ScoreCommand();
                    case "health":
                        return new HealthCommand();
                    case "swap":
                        return new SwapCommand("", "");
                    default:
                        if (GameState.instance().hasItemSpecificCommand(words[0])) {
                            return new ItemSpecificCommand(words[0], "");
                        }
                        else {
                            return new UnknownCommand(words[0]);
                        }
                }
            default:
                String itemName;
                switch (words[0]) {
                    case "take":
                        itemName = words[1];
                        return new TakeCommand(itemName);
                    case "swap":
                        String userItem = "";
                        String itemInRoom = "";
                        if (words.length == 4) {
                            userItem = words[1];
                            itemInRoom = words[3];
                        }
                        else if (words.length == 3) {
                            userItem = words[1];
                            itemInRoom = words[2];
                        }
                        else if (words.length == 2) {
                            userItem = words[1];
                        }
                        return new SwapCommand(userItem,itemInRoom);
                    case "drop":
                        itemName = words[1];
                        return new DropCommand(itemName);
                    case "save":
                        String saveFilename = words[1];
                        return new SaveCommand(saveFilename);
                    default:
                        String verb = words[0];
                        String noun = words[1];
                        return new ItemSpecificCommand(verb, noun);
                }
        }
    }
}


