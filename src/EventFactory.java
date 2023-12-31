//package com.company;


/**
 * An EventFactory represents a factory whose purpose is to parse text strings
 * and produce the appropriate {@link ZorkEvent} objects.
 * The EventFactory is a Singleton class, which means there will only ever be
 * one object of this type. Other classes can access this object via the
 * {@link EventFactory#instance()} method.
 * Note that the EventFactory class is the only class that should instantiate
 * {@link ZorkEvent} objects.
 * @author John Thomas (for phase 1)
 * @author Richard Volynski
 * @version 3.4
 * 21 July 2020
 */
public class EventFactory {
    private static EventFactory singleInstance = null;

    /**
     * Ensures the instantiation of a single EventFactory object.
     *
     * @return the new instance of the event factory the first time this method
     * is called, or the same EventFactory object if it has already been instantiated
     */
    static synchronized EventFactory instance() {
        if (singleInstance == null)
            singleInstance = new EventFactory();
        return singleInstance;
    }

    /**
     * Constructs a new instance of an EventFactory.
     */
    private EventFactory() {
    }

    /**
     * Analyzes the given string to obtain information about an event, and produces
     * the corresponding {@link ZorkEvent} object.
     *
     * @param eventString  the event's info (with optional parameters)
     * @return the {@link ZorkEvent} object relevant to this string
     * @throws IllegalArgumentException if this string does not match the proper event syntax
     */
    ZorkEvent parse(String eventString) throws IllegalArgumentException {
        String[] sa = eventString.replace(")","").split("\\(");
        String eventName = sa[0];
        ZorkEvent event = null;
        try {
            if (sa.length == 1) {
                switch (eventName) {
                    case "Die":
                        event = new DieEvent();
                        break;
                    case "Teleport":
                        event = new TeleportEvent();
                        break;
                    case "Win":
                        event = new WinEvent();
                        break;
                }
            } else {
                String[] eventParams = sa[1].split(",");
                String message;
                String itemName;
                int points;
                switch (eventName) {
                    case "Die":     //hardcoded dungeon only
                        message = eventParams[0];
                        event = new DieEvent(message);
                        break;
                    case "Disappear":
                        itemName = eventParams[0];
                        event = new DisappearEvent(itemName);
                        break;
                    case "Drop":
                        itemName = eventParams[0];
                        event = new DropEvent(itemName);
                        break;
                    case "Score":
                        points = Integer.parseInt(eventParams[0]);
                        event = new ScoreEvent(points);
                        break;
                    case "Teleport":    //hardcoded dungeon only
                        String roomName = eventParams[0];
                        event = new TeleportEvent(roomName);
                        break;
                    case "Transform":
                        String newItemName = eventParams[0];
                        event = new TransformEvent(newItemName);
                        break;
                    case "Unlock":
                        String exitDir = eventParams[0];
                        itemName = eventParams[1];
                        event = new UnlockEvent(exitDir, itemName);
                        break;
                    case "Win":     //hardcoded dungeon only
                        message = eventParams[0];
                        event = new WinEvent(message);
                        break;
                    case "Wound":
                        points = Integer.parseInt(eventParams[0]);
                        event = new WoundEvent(points);
                        break;
                }
            }
            return event;
        } catch (Exception e) {
            return null;    //TODO implement
        }
    }
}
