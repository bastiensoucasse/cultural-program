package domain;

import java.time.LocalDate;

public class Concert extends Event {
    private final String artist;

    public Concert(final String artist, final LocalDate date, final TimeSlot slot, final int capacity) {
        super(slot, capacity);
        this.artist = artist;

        addDate(date);
    }

    public String getArtist() {
        return artist;
    }

    @Override
    public String toString() {
        return "Concert of " + super.toString();
    }
}
