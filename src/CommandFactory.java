/**
 * CommandFactory Class - A factory class whose purpose is to parse text strings
 * and produce the appropriate Command objects. Also, the CommandFactory is a Singleton class.
 * @author Richard Volynski
 * @version 1.7
 * 13 June 2020
 */
package com.company;

class CommandFactory {
    private static CommandFactory single_instance = null;

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
        switch (commandString.toUpperCase()) {
            case "N":
            case "S":
            case "W":
            case "E":
            case "U":
            case "D":
                return new Command(commandString);
            default:
                return null;
        }
    }
}

