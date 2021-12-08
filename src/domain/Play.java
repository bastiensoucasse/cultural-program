package domain;

import java.time.LocalDate;

public class Play extends Event {
    private final String title;

    public Play(final String title, final LocalDate startDate, final LocalDate endDate, final TimeSlot slot, final int capacity) {
        super(slot, capacity);
        this.title = title;

        for (LocalDate date = startDate; date.isBefore(endDate) || date.equals(endDate); date = date.plusDays(1))
            super.addDate(date);
    }


    public String getTitle() {
        return title;
    }


    @Override
    public String toString() {
        return "Play " + super.toString();
    }
}
