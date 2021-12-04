package domain;

import java.time.LocalDate;

public class Concert extends Event {
    private final String artist;

    public Concert(final String artist, final LocalDate date, final TimeSlot slot, final int capacity) {
        super(slot, capacity);
        this.artist = artist;
        super.addDate(date);
    }

    @Override
    public String toString() {
        return "Concert of " + artist + " " + super.toString();
    }
}
