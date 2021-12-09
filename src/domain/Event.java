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
    private final int priority;
    private int capacity;
    private TimeSlot slot;
    private List<LocalDate> dates = new ArrayList<>();

    protected Event(final int priority, final int capacity, final TimeSlot slot) {
        this.id = ++numEvents;
        this.priority = priority;
        this.capacity = capacity;
        this.slot = slot;
    }

    public int getId() {
        return id;
    }

    public int getPriority() {
        return priority;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public TimeSlot getSlot() {
        return slot;
    }

    public void setSlot(TimeSlot slot) {
        this.slot = slot;
    }

    public List<LocalDate> getDates() {
        return dates;
    }

    public void setDates(final List<LocalDate> dates) {
        this.dates = dates;
    }

    public void addDate(final LocalDate date) {
        dates.add(date);
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
    public String toString() {
        StringBuilder s = new StringBuilder("(" + capacity + " attenders), " + slot + ", on ");
        boolean first = true;
        for (final LocalDate d : dates) {
            if (first) first = false;
            else s.append(", ");
            s.append(d.format(DateTimeFormatter.ofPattern("EEEE M/d/yyyy")));
        }
        return s.toString();
    }
}
