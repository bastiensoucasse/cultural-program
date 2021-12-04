package domain;

import java.time.LocalDate;

public class Concert extends Event {
    private final String artist;
    private final LocalDate date;

    public Concert(final String artist, final LocalDate date, final int capacity) {
        super(capacity);
        this.artist = artist;
        this.date = date;
    }

    public String getArtist() {
        return artist;
    }

    public LocalDate getDate() {
        return date;
    }
}
