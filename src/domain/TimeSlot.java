package domain;

import java.time.LocalTime;

/**
 * Time slot object used to represent opening hours and events duration
 * (Value Object)
 * 
 * @author Bastien Soucasse
 * @author Iantsa Provost
 */
public class TimeSlot {
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
     * Find out if the time slot overlaps with other
     * 
     * @param other Time slot compared with
     * @return True if the time slots overlap
     *         False otherwise.
     */
    public boolean overlap(final TimeSlot other) {
        return !other.getStartTime().isBefore(startTime) && !other.getStartTime().isAfter(endTime)
                || !other.getEndTime().isAfter(endTime) && !other.getEndTime().isBefore(startTime);
    }

    @Override
    public String toString() {
        return startTime + " to " + endTime;
    }
}
