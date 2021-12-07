package domain;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Map;
import java.util.Objects;

public class Venue {
    private static int numVenues = 0;

    private final int id;
    private final String name;
    private final int capacity;
    private final Map<DayOfWeek, TimeSlot> slots;

    public Venue(final int capacity, final Map<DayOfWeek, TimeSlot> slots) {
        this.id = ++numVenues;
        this.name = "Venue " + numVenues;
        this.capacity = capacity;
        this.slots = slots;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isOpened(final DayOfWeek day, final TimeSlot slot) {
        final LocalTime open = slots.get(day).getStartTime(), close = slots.get(day).getEndTime(),
                start = slot.getStartTime(), end = slot.getEndTime();
        return (start.equals(open) || start.isAfter(open)) && (end.isBefore(close) || end.equals(close));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Venue venue = (Venue) o;
        return id == venue.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name;
    }
}
