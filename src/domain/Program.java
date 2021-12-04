package domain;

import java.util.ArrayList;
import java.util.List;

public class Program {
    private final int id;
    private final List<Event> eventList = new ArrayList<>();

    public Program(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public List<Event> getEventList() {
        return eventList;
    }
}
