/**
 * EventFactory Class - A factory class whose purpose is to parse text strings
 * and produce the appropriate Event objects.
 * EventFactory is a Singleton class.
 * @author Object Oriented Optimists (OOO)
 * @author John Thomas
 * @author Richard Volynski
 * @version 2.8
 * 8 July 2020
 */
class EventFactory {
    private static EventFactory singleInstance = null;

    /**
     * instance() - this method is represented by the Singleton EventFactory Class
     * @return single static instance of EventFactory class
     */
    static synchronized EventFactory instance() {
        if (singleInstance == null)
            singleInstance = new EventFactory();
        return singleInstance;
    }

    /**
     * EventFactory - default constructor
     */
    private EventFactory() {
    }

    /**
     * parse - this method parses entered events (and parameters) and produces ZorkEvent objects
     * @param eventString - event with parameters read from .zork file
     * @return - ZorkEvent object
     */
     ZorkEvent parse (String eventString) {
        return null;    //TODO implement
    }
}

