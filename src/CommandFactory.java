/**
 * CommandFactory Class - A factory class whose purpose is to parse text strings
 * and produce the appropriate Command objects. Also, the CommandFactory is a Singleton class.
 * @author Richard Volynski
 * @version 1.0
 * 1 June 2020
 */


class CommandFactory {

    public CommandFactory instance() {
        return this;
    }

    private CommandFactory() {
    }

    Command parse (String commandString) {
        return null; //TODO return Command
    }
}
