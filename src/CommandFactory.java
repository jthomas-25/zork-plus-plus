/**
 * CommandFactory Class - A factory class whose purpose is to parse text strings
 * and produce the appropriate Command objects. Also, the CommandFactory is a Singleton class.
 * @author Richard Volynski
 * @version 2.5
 * 23 June 2020
 */


class CommandFactory {
    private static CommandFactory single_instance = null;

    /**
     * instance() - this method is represented by the Singleton CommandFactory Class
     *
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
     *
     * @param commandString - user input
     * @return - Command objects
     */
    Command parse(String commandString) {
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
                    case "i":
                    case "inventory":
                        return new InventoryCommand();
                    default:
                        return new UnknownCommand(words[0]);
                }
            default:
                switch (words[0]) {
                    case "take":
                        return new TakeCommand(words[1]);
                    case "drop":
                        return new DropCommand(words[1]);
                    case "save":
                        return new SaveCommand(words[1]);
                    default:
                        return new ItemSpecificCommand(words[0], words[1]);
                }
        }
    }
}
