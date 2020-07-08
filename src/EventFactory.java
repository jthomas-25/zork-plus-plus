class EventFactory {
    private static EventFactory singleInstance = null;

    static synchronized EventFactory instance() {
        if (singleInstance == null)
            singleInstance = new EventFactory();
        return singleInstance;
    }
    
    private EventFactory() {
    }
}
