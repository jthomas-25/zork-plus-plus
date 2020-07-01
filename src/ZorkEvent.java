/**
 * ZorkEvent class
 * @author Object Oriented Optimists (OOO)
 * @version 1.0
 * 1 July 2020
 */


abstract class ZorkEvent {

    ZorkEvent() {
    }

    abstract String trigger() throws Exception;

    abstract String trigger(int score) throws Exception;

    abstract String trigger(String item) throws Exception;

    abstract String trigger(String item1, String item2) throws Exception;
}

class ScoreEvent extends ZorkEvent {

    @Override
    String trigger() throws Exception {
       throw new Exception("Score event wrong call");
    }

    @Override
    String trigger(int score) {
        return "Score";    //TODO implement
    }

    @Override
    String trigger(String string) throws Exception {
        throw new Exception("Score event wrong call");
    }

    @Override
    String trigger(String item1, String item2) throws Exception {
        throw new Exception("Score event wrong call");
    }
}

class WoundEvent extends ZorkEvent {

    @Override
    String trigger() throws Exception {
        throw new Exception("Wound event wrong call");
    }

    @Override
    String trigger(int healthPoints) {
        return null;
    }

    @Override
    String trigger(String string) throws Exception {
        throw new Exception("Wound event wrong call");
    }

    @Override
    String trigger(String item1, String item2) throws Exception {
        throw new Exception("Wound event wrong call");
    }
}

class DieEvent extends ZorkEvent {

    @Override
    String trigger() throws Exception {
        return "Die";
    }

    @Override
    String trigger(int score) throws Exception {
        throw new Exception("Die event wrong call");
    }

    @Override
    String trigger(String string) throws Exception {
        throw new Exception("Die event wrong call");
    }

    @Override
    String trigger(String item1, String item2) throws Exception {
        return null;
    }
}

class WinEvent extends ZorkEvent {

    @Override
    String trigger() throws Exception {
        return "Win";
    }

    @Override
    String trigger(int score) throws Exception {
        throw new Exception("Win event wrong call");
    }

    @Override
    String trigger(String string) throws Exception {
        throw new Exception("Win event wrong call");
    }

    @Override
    String trigger(String item1, String item2) throws Exception {
        throw new Exception("Win event wrong call");
    }
}

class DropEvent extends ZorkEvent {

    @Override
    String trigger() throws Exception {
        throw new Exception("Drop event wrong call");
    }

    @Override
    String trigger(int score) throws Exception {
        throw new Exception("Drop event wrong call");
    }

    @Override
    String trigger(String string) {
        return "Item dropped";
    }

    @Override
    String trigger(String item1, String item2) throws Exception {
        throw new Exception("Drop event wrong call");
    }
}

class DisappearEvent extends ZorkEvent {

    @Override
    String trigger() throws Exception {
        throw new Exception("Disappear event wrong call");
    }

    @Override
    String trigger(int score) throws Exception {
        throw new Exception("Disappear event wrong call");
    }

    @Override
    String trigger(String string) {
        return "Item disappeared";
    }

    @Override
    String trigger(String item1, String item2) throws Exception {
        throw new Exception("Disappear event wrong call");
    }
}

class TransformEvent extends ZorkEvent {

    @Override
    String trigger() throws Exception {
        throw new Exception("Transform event wrong call");
    }

    @Override
    String trigger(int score) throws Exception {
        throw new Exception("Transform event wrong call");
    }

    @Override
    String trigger(String string) throws Exception {
        throw new Exception("Transform event wrong call");
    }

    @Override
    String trigger(String item1, String item2) throws Exception {
        return "Transform"; //TODO implement
    }
}

class TeleportEvent extends ZorkEvent {

    @Override
    String trigger() {
        return null;
    }

    @Override
    String trigger(int score) throws Exception {
        throw new Exception("Teleport event wrong call");
    }

    @Override
    String trigger(String string) throws Exception {
        throw new Exception("Teleport event wrong call");
    }

    @Override
    String trigger(String item1, String item2) throws Exception {
        throw new Exception("Teleport event wrong call");
    }
}


