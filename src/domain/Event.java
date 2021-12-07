package domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Event {
    private static int numEvents = 0;

    private final int id;
    private final List<LocalDate> dates = new ArrayList<>();
    private final TimeSlot slot;
    private final int capacity;

    protected Event(final TimeSlot slot, final int capacity) {
        this.id = ++numEvents;
        this.slot = slot;
        this.capacity = capacity;
    }

    protected void addDate(final LocalDate date) {
        dates.add(date);
    }

    public List<LocalDate> getDates() {
        return dates;
    }

    public TimeSlot getTimeSlot() {
        return slot;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Event event = (Event) o;
        return id == event.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "(" + capacity + " attenders): " + slot;
    }
}
