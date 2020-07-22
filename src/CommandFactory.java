//package com.company;

/**
 * CommandFactory Class - A factory class whose purpose is to parse text strings
 * and produce the appropriate {@link Command} objects.
 * CommandFactory is a Singleton class.
 * @author Object Oriented Optimists (OOO)
 * @version 3.7
 * 19 July 2020
 */
class CommandFactory {
    private static CommandFactory single_instance = null;

    /**
     * instance() - this method is represented by the Singleton CommandFactory Class
     * @return single static instance of CommandFactory class
     */
    public static synchronized CommandFactory instance() {
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
     * parse - this method parses entered commands and produces {@link Command} objects
     * @param commandString - user input
     * @return - {@link Command} objects
     */
    Command parse(String commandString) {
        String[] words = commandString.split(" ");
        switch (words.length) {
            case 1:
                switch (words[0].toLowerCase()) {
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
                    case "kill":
                        return new KillCommand("");
                    case "unlock":
                        return new UnlockCommand("", "", "", "");
                    default:
                        if (GameState.instance().hasItemSpecificCommand(words[0])) {
                            return new ItemSpecificCommand(words[0], "");
                        }
                        else {
                            return new UnknownCommand(words[0]);
                        }
                }
            default:
                String itemName = "";
                switch (words[0].toLowerCase()) {
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
                    case "kill":
                        String noun = words[1];
                        return new KillCommand(noun);
                    case "unlock":
                        String exitDir = "";
                        String objectToUnlock = "";
                        String prep = "";
                        itemName = "";
                        if (words.length == 2) {
                            //unlock exit
                            objectToUnlock = words[1];
                        } else if (words.length == 3) {
                            if (words[2].equals("with")) {
                                //unlock exit with
                                objectToUnlock = words[1];
                                prep = words[2];
                            } else {
                                //unlock e exit
                                exitDir = words[1];
                                objectToUnlock = words[2];
                            }
                        } else if (words.length == 4) {
                            //unlock exit with <itemName>
                            exitDir = words[1];
                            objectToUnlock = words[2];
                            prep = words[3];
                        } else if (words.length == 5) {
                            //unlock e exit with <itemName>
                            exitDir = words[1];
                            objectToUnlock = words[2];
                            prep = words[3];
                            itemName = words[4];
                        } else {
                            return new UnknownCommand(commandString);
                        }
                        return new UnlockCommand(exitDir, objectToUnlock, prep, itemName);
                    default:
                        String verb = words[0];
                        String word2 = words[1];
                        return new ItemSpecificCommand(verb, word2);
                }
        }
    }
}
