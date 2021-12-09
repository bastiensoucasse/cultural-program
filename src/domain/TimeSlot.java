package domain;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Time slot object used to represent opening hours and event durations.
 * (Value Object)
 *
 * @author Bastien Soucasse
 * @author Iantsa Provost
 */
public class TimeSlot {
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

    private final LocalTime startTime;
    private final LocalTime endTime;

    public TimeSlot(final LocalTime startTime, final LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Checks if the time slot overlaps with another one.
     *
     * @param other The time slot to compare it to.
     * @return <code>true</code> if the time slots overlap; <code>false</code> otherwise.
     */
    public boolean overlap(final TimeSlot other) {
        return !other.getStartTime().isBefore(startTime) && !other.getStartTime().isAfter(endTime) || !other.getEndTime().isAfter(endTime) && !other.getEndTime().isBefore(startTime);
    }

    @Override
    public String toString() {
        return "from " + startTime.format(timeFormatter) + " to " + endTime.format(timeFormatter);
    }
}
