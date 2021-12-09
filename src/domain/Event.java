package domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.time.format.DateTimeFormatter;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;


/**
 * Event (can't be instantiated) representing either a Concert or a Play.
 * (Entity)
 *
 * @author Bastien Soucasse
 * @author Iantsa Provost
 */
@JsonTypeInfo(use = NAME, include = PROPERTY)
@JsonSubTypes({@JsonSubTypes.Type(value = Concert.class, name = "Concert"), @JsonSubTypes.Type(value = Play.class, name = "Play")})
public abstract class Event {
    private static transient int numEvents = 0;

    private final int id;
    private final List<LocalDate> dates = new ArrayList<>();
    private final TimeSlot slot;
    private final int capacity;

    protected Event(final TimeSlot slot, final int capacity) {
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

    public void addDate(final LocalDate date) {
        dates.add(date);
    }

    public void removeDate(final LocalDate date) {
        dates.remove(date);
    }

    public TimeSlot getTimeSlot() {
        return slot;
    }

    public int getCapacity() {
        return capacity;
    }

    /**
     * Checks if the event has a date on SATURDAY.
     */
    public boolean isOnSaturday() {
        for (final LocalDate date : dates)
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY) return true;
        return false;
    }

    /**
     * Checks if the event has a date on SUNDAY.
     */
    public boolean isOnSunday() {
        for (final LocalDate date : dates)
            if (date.getDayOfWeek() == DayOfWeek.SUNDAY) return true;
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("(" + capacity + " attenders), " + slot + ", on ");
        boolean first = true;
        for (final LocalDate d : dates) {
            if (first) first = false;
            else s.append(", ");
            s.append(d.format(DateTimeFormatter.ofPattern("EEEE M/d/yyyy")));
        }
        s.append(".");
        return s.toString();
    }

    public String toStringWithoutDates() {
        return "(" + capacity + " attenders), " + slot;
    }
}
