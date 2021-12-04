package domain;

import java.time.LocalDate;

public class Play extends Event {
    private final String title;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Play(final String title, final LocalDate startDate, final LocalDate endDate, final int capacity) {
        super(capacity);
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
