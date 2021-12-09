package domain;

import java.time.LocalDate;

/**
 * Concert (cultural event).
 *
 * @author Bastien Soucasse
 * @author Iantsa Provost
 */
public class Concert extends Event {
    private static final int PRIORITY = 1;

    private final String artist;

    public Concert(final String artist, final LocalDate date, final TimeSlot slot, final int capacity) {
        super(PRIORITY, capacity, slot);
        this.artist = artist;

        addDate(date);
    }

    public String getArtist() {
        return artist;
    }

    @Override
    public String toString() {
        return "Concert of " + artist + ", " + super.toString();
    }
}
