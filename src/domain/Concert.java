package domain;

import java.util.Date;

public class Concert extends Event {
    private final Date date;
    private final String artist;

    public Concert(final Date date, final String artist, final int capacity) {
        super(capacity);
        this.date = date;
        this.artist = artist;
    }

    public Date getDate() {
        return date;
    }

    public String getArtist() {
        return artist;
    }
}
