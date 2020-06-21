/**
 * CommandFactory Class - A factory class whose purpose is to parse text strings
 * and produce the appropriate Command objects. Also, the CommandFactory is a Singleton class.
 * @author Richard Volynski
 * @version 2.3
 * 21 June 2020
 */


class CommandFactory {
    private static CommandFactory single_instance = null;
    private String itemName;

    /**
     * instance() - this method is represented by the Singleton CommandFactory Class
     * @return single_instance
     */
    public static CommandFactory instance() {
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
        switch (commandString.toLowerCase()) {
            case "n":
            case "s":
            case "w":
            case "e":
            case "u":
            case "d":
                return new MovementCommand(commandString);
            case "look":
                return new LookCommand();
            case "save":
                return new SaveCommand();
            case "take":
                return new TakeCommand(commandString);  //TODO implement item to take
            case "drop":
                return new DropCommand(commandString);  //TODO implement item to drop
            case "i":
                return new InventoryCommand(commandString);
            default:
                return new UnknownCommand(commandString);
        }
    }
}

