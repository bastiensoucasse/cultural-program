package domain;

import java.util.Date;

public class Play extends Event {
    private final Date startingDate;
    private final Date endingDate;
    private final String title;

    public Play(final Date startingDate, final Date endingDate, final String title, final int capacity) {
        super(capacity);
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.title = title;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public String getTitle() {
        return title;
    }
}
