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
 * @version 3.0
 * 15 July 2020
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

                        case "Disappear":
                            event = new DisappearEvent(""); //TODO
                            break;
    /*                    case "Drop":
                            event = new DropEvent();
                            break;
    */
                        case "Teleport":
                            event = new TeleportEvent();
                            break;
                        case "Win":
                            event = new WinEvent();
                            break;
                    }
                    break;

                default:
                    int points;
                    String message;
                    switch (eventName) {
                        case "Die":
                            message = sa[1];
                            event = new DieEvent(message);
                            break;
                        case "Score":
                            points = Integer.parseInt(sa[1]);
                            event = new ScoreEvent(points);
                            break;

    /*
                        case "Teleport":
                            String roomName = sa[1];
                            event = new TeleportEvent(roomName);
                            break;
    */                    case "Transform":
                            event = new TransformEvent(sa[1]);
                            break;

                        case "Unlock":
                            String exitDir = sa[1];
                            event = new UnlockEvent(exitDir);
                            break;
                        case "Win":
                            message = sa[1];
                            event = new WinEvent(message);
                            break;
                        case "Wound":
                            points = Integer.parseInt(sa[1]);
                            event = new WoundEvent(points);
                            break;
                        case "Drop":
                            event = new DropEvent(sa[1]);
                            break;

                    }
            }
            return event;
        }
        catch (Exception e) {
            return null;    //TODO implement
        }
/*
        //Reflection technique
        try {
            Class clazz = Class.forName(eventName + "Event");
            event = (ZorkEvent) clazz.newInstance();
        } catch (Exception e) {
        }
*/
    }

    String triggerEvent(String eventName, String eventParam) throws IllegalArgumentException, NoItemException, NoRoomException {
        ZorkEvent event;
        switch (eventName.toLowerCase()) {
            case "score":
                int points = Integer.parseInt(eventParam);
                event = new ScoreEvent(points);
                break;
            case "wound":
                int hp = Integer.parseInt(eventParam);
                event = new WoundEvent(hp);
                break;
            case "die":
                event = new DieEvent(eventParam);
                break;
            case "win":
                event = new WinEvent(eventParam);
                break;
            case "drop":
                event = new DropEvent(eventParam);
                break;
            case "disappear":
                event = new DisappearEvent(eventParam);
                break;
            case "transform":
                event = new TransformEvent(eventParam);
                break;
/*
            case "teleport":
                event = new TeleportEvent(eventParam);
                break;
*/
            default:
                throw new IllegalArgumentException("Event not found: " + eventName);
        }
        return event.trigger("");
    }
}
