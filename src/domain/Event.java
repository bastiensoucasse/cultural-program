package domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;


/**
 * ???
 * (Entity)
 * 
 * @author Bastien Soucasse
 * @author Iantsa Provost 
 */

@JsonTypeInfo(use = NAME, include = PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Concert.class, name = "Concert"),
        @JsonSubTypes.Type(value = Play.class, name = "Play")
})
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

    public int getId() {
        return id;
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

    /**
     * Checks if the event has no date on SATURDAY and SUNDAY.
     */
    public boolean isNotWE() {
        for (final LocalDate date : dates)
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)
                return false;

        return true;
    }

    /**
     * Checks if the event has a date on SATURDAY.
     */
    public boolean isWESat() {
        for (final LocalDate date : dates)
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY)
                return true;
        return false;
    }

    /**
     * Checks if the event has a date on SUNDAY.
     */
    public boolean isWESun() {
        for (final LocalDate date : dates)
            if (date.getDayOfWeek() == DayOfWeek.SUNDAY)
                return true;
        return false;
    }

    /**
     * Checks if the event has a date on SATURDAY or SUNDAY (but not both).
     */
    public boolean isPartlyWE() {
        boolean sat = false, sun = false;

        for (final LocalDate date : dates) {
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY)
                sat = true;
            if (date.getDayOfWeek() == DayOfWeek.SUNDAY)
                sun = true;
        }

        if (sat && sun) return false;
        return sat || sun;
    }

    /**
     * Checks if the event has a date on both SATURDAY and SUNDAY.
     */
    public boolean isFullyWE() {
        boolean sat = false, sun = false;

        for (final LocalDate date : dates) {
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY)
                sat = true;
            if (date.getDayOfWeek() == DayOfWeek.SUNDAY)
                sun = true;
        }

        return sat && sun;
    }

    /**
     * Checks if the event has a date on only SATURDAY or SUNDAY.
     */
    public boolean isOnlyWE() {
        for (final LocalDate date : dates)
            if (date.getDayOfWeek() == DayOfWeek.MONDAY || date.getDayOfWeek() == DayOfWeek.TUESDAY || date.getDayOfWeek() == DayOfWeek.WEDNESDAY || date.getDayOfWeek() == DayOfWeek.THURSDAY || date.getDayOfWeek() == DayOfWeek.FRIDAY)
                return false;

        return true;
    }

    /**
     * Checks if the event has a date on both SATURDAY and SUNDAY and only them.
     */
    public boolean isFullAndOnlyWE() {
        boolean sat = false, sun = false;

        for (final LocalDate date : dates) {
            if (date.getDayOfWeek() == DayOfWeek.MONDAY || date.getDayOfWeek() == DayOfWeek.TUESDAY || date.getDayOfWeek() == DayOfWeek.WEDNESDAY || date.getDayOfWeek() == DayOfWeek.THURSDAY || date.getDayOfWeek() == DayOfWeek.FRIDAY)
                return false;
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY)
                sat = true;
            if (date.getDayOfWeek() == DayOfWeek.SUNDAY)
                sun = true;
        }

        return sat && sun;
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
        String s = "from " + slot + "(" + capacity + " attenders), on ";
        boolean first = true;
        for (final LocalDate d : dates) {
            if (first) first = false;
            else s += ", ";
            s += d.getDayOfWeek().name().toLowerCase();
        }
        return s + ".";
    }

    public String toStringWithoutDates() {
        return "from " + slot + "(" + capacity + " attenders)";
    }
}
