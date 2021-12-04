package domain;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Map;

public class Venue {
    private static int numVenues = 0;

    private final int id;
    private final String name;
    private final int capacity;
    private final Map<DayOfWeek, TimeSlot> slots;
    private boolean empty = true;

    public Venue(final int capacity, final Map<DayOfWeek, TimeSlot> slots) {
        this.id = numVenues;
        this.name = "Venue " + numVenues;
        this.capacity = capacity;
        this.slots = slots;
        numVenues++;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isOpened(final DayOfWeek day, final TimeSlot slot) {
        final LocalTime open = slots.get(day).getStartTime(), close = slots.get(day).getEndTime(), start = slot.getStartTime(), end = slot.getEndTime();
        return (start.equals(open) || start.isAfter(open)) && (end.isBefore(close) || end.equals(close));
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Venue other = (Venue) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
}
