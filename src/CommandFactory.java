/**
 * CommandFactory Class - A factory class whose purpose is to parse text strings
 * and produce the appropriate Command objects.
 * CommandFactory is a Singleton class.
 * @author Object Oriented Optimists (OOO)
 * @version 2.9
 * 9 July 2020
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
                    case "look":
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
                    default:
                        return new UnknownCommand(words[0]);
                }
            default:
                String itemName;
                switch (words[0]) {
                    case "take":
                        itemName = words[1];
                        return new TakeCommand(itemName);
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

