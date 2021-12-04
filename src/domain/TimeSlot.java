package domain;

import java.time.LocalTime;

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

    public boolean overlap(final TimeSlot other) {
        if (other.getStartTime().isAfter(startTime) && !other.getStartTime().isAfter(endTime))
            return true;

        if (other.getEndTime().isBefore(endTime) && !other.getEndTime().isBefore(startTime))
            return true;

        return false;
    }

    @Override
    public String toString() {
        return startTime + " to " + endTime;
    }
}
