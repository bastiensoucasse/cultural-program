package domain;

import java.time.LocalDate;

/**
 * Play (cultural event) during several days.
 * Has a representation each and every day between startDate and endDate included.
 * Also, the time slot is the same for every representations.
 *
 * @author Bastien Soucasse
 * @author Iantsa Provost
 */
public class Play extends Event {
    private static final int PRIORITY = 2;

    private final String title;

    public Play(final String title, final LocalDate startDate, final LocalDate endDate, final TimeSlot slot, final int capacity) {
        super(PRIORITY, capacity, slot);
        this.title = title;

        for (LocalDate date = startDate; date.isBefore(endDate) || date.equals(endDate); date = date.plusDays(1))
            super.addDate(date);
    }


    public String getTitle() {
        return title;
    }


    @Override
    public String toString() {
        return "Play " + title + ", " + super.toString();
    }
}
