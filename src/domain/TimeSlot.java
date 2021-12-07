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
        return !other.getStartTime().isBefore(startTime) && !other.getStartTime().isAfter(endTime)
                || !other.getEndTime().isAfter(endTime) && !other.getEndTime().isBefore(startTime);
    }

    @Override
    public String toString() {
        return startTime + " to " + endTime;
    }
}
