import com.sun.jdi.event.BreakpointEvent;

/**
 * An EventFactory represents a factory whose purpose is to parse text strings
 * and produce the appropriate {@link ZorkEvent} objects.
 * The EventFactory is a Singleton class, which means there will only ever be
 * one object of this type. Other classes can access this object via the
 * {@link EventFactory#instance()} method.
 * Note that the EventFactory class is the only class that should instantiate
 * {@link ZorkEvent} objects.
 * @author John Thomas
 * @author Richard Volynski
 * @version 3.1
 * 16 July 2020
 */
class EventFactory {
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
            switch (sa.length) {
                case 1:
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
                break;
                default:
                    //now use reflection?
                    String message;
                    String itemName;
                    int points;
                    switch (eventName) {
                        case "Die":     //hardcoded dungeon only
                            message = sa[1];
                            event = new DieEvent(message);
                            break;
                        case "Disappear":
                            itemName = sa[1];
                            event = new DisappearEvent(itemName);
                            break;
                        case "Drop":
                            itemName = sa[1];
                            event = new DropEvent(itemName);
                            break;
                        case "Score":
                            points = Integer.parseInt(sa[1]);
                            event = new ScoreEvent(points);
                            break;
                        case "Teleport":    //hardcoded dungeon only
                            String roomName = sa[1];
                            event = new TeleportEvent(roomName);
                            break;
                        case "Transform":
                            String newItemName = sa[1];
                            event = new TransformEvent(newItemName);
                            break;
                        case "Unlock":
                            String exitDir = sa[1];
                            event = new UnlockEvent(exitDir);
                            break;
                        case "Win":     //hardcoded dungeon only
                            message = sa[1];
                            event = new WinEvent(message);
                            break;
                        case "Wound":
                            points = Integer.parseInt(sa[1]);
                            event = new WoundEvent(points);
                            break;
                    }
                break;
            }
            return event;
        }
        catch (Exception e) {
            return null;    //TODO implement
        }
    }
}
