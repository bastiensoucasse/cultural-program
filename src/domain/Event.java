package domain;

public abstract class Event {
    private static int numEvents = 0;

    private final int id;
    private final int capacity;

    public Event(final int capacity) {
        this.id = numEvents;
        this.capacity = capacity;
        numEvents++;
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }
}
