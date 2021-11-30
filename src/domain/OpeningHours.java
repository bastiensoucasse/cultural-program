package domain;

import java.time.LocalTime;

public class OpeningHours {
    private final LocalTime opening;
    private final LocalTime closing;

    public OpeningHours(final LocalTime opening, final LocalTime closing) {
        this.opening = opening;
        this.closing = closing;
    }

    public LocalTime getOpening() {
        return opening;
    }

    public LocalTime getClosing() {
        return closing;
    }
}
