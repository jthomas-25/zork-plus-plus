/**
 * CommandFactory Class - A factory class whose purpose is to parse text strings
 * and produce the appropriate Command objects. Also, the CommandFactory is a Singleton class.
 * @author Richard Volynski
 * @version 1.3
 * 4 June 2020
 */


class CommandFactory {
    private static CommandFactory single_instance = null;
    public static CommandFactory instance() {
        if (single_instance == null)
            single_instance = new CommandFactory();

        return single_instance;
    }

    private CommandFactory() {
    }

    Command parse (String commandString) {
        return null; //TODO return Command
    }
}

