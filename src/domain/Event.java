package domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Event {
    private static int numEvents = 0;

    private final int id;
    private final List<LocalDate> dates = new ArrayList<>();
    private final TimeSlot slot;
    private final int capacity;
    private Venue venue = null;

    public Event(final TimeSlot slot, final int capacity) {
        this.id = ++numEvents;
        this.slot = slot;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public List<LocalDate> getDates() {
        return dates;
    }

    void addDate(final LocalDate date) {
        dates.add(date);
    }

    public TimeSlot getTimeSlot() {
        return slot;
    }

    public int getCapacity() {
        return capacity;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(final Venue venue) {
        this.venue = venue;
    }

    @Override
    public String toString() {
        if (venue == null)
            return dates + " " + slot + " with " + capacity + " attenders";
        return dates + " " + slot + " with " + capacity + " attenders" + " at " + venue;
    }
}
