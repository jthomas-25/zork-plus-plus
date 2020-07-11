/**
 * An EventFactory represents a factory whose purpose is to parse text strings
 * and produce the appropriate {@link ZorkEvent} objects.
 * The EventFactory is a Singleton class, which means there will only ever be
 * one object of this type. Other classes can access this object via the
 * {@link EventFactory#instance()} method.
 * Note that the EventFactory class is the only class that should instantiate
 * {@link ZorkEvent} objects.
 * @author John Thomas
 * @version 2.8
 * 10 July 2020
 */
class EventFactory {
    private static EventFactory singleInstance = null;

    /**
     * Ensures the instantiation of a single EventFactory object.
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
     * @param eventString the event's info (with optional parameters)
     * @return the {@link ZorkEvent} object relevant to this string
     * @throws IllegalArgumentException if this string does not match the proper event syntax
     */
     ZorkEvent parse(String eventString) throws IllegalArgumentException {
        return null;    //TODO implement
    }
}
