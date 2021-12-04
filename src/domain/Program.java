package domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Program {
    private final int id;
    private final Venue venues[] = new Venue[4];
    private final boolean canUseNewVenue = true;
    private final List<Event> eventList = new ArrayList<>();

    private boolean venueCanHostEvent(final Venue venue, final Event event) {
        // Check venue capacity
        if (venue.getCapacity() < event.getCapacity()) {
            System.err.println("Venue capacity too low");
            return false;
        }

        for (LocalDate date : event.getDates()) {
            int dateValue = date.getDayOfWeek().getValue();

            // Check venue opening hours
            if (!venue.isOpened(dateValue, event.getTimeSlot())) {
                System.err.println("Venue not opened");
                return false;
            }
                

            // Check venue events
            for (Event e : eventList) {
                for (LocalDate d : e.getDates()) {
                    int dValue = d.getDayOfWeek().getValue();
                    if (e.getVenue().equals(venue) &&  dValue == dateValue && e.getTimeSlot().overlap(event.getTimeSlot())) {
                        System.err.println("Venue not available");
                        return false;
                    }
                }
            }
        }
        
        System.err.println(venue + " can host " + event);
        return true;
    }

    private Venue findVenue(final Event event) {
        for (Venue venue : venues)
            if (!venue.isEmpty() || canUseNewVenue)
                if (venueCanHostEvent(venue, event))
                    return venue;
        return null;
    }

    public Program(final int id) {
        this.id = id;

        for (int i = 0; i < 4; i++) {
            TimeSlot[] slots = new TimeSlot[7];
            for (int day = 0; day < 7; day++)
                slots[i] = new TimeSlot(LocalTime.of(19, 0), LocalTime.of(23, 0));
            venues[i] = new Venue(1500, slots);
        }
    }

    public int getId() {
        return id;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public boolean add(final Event event) {
        System.out.println("- " + event);

        final Venue venue = findVenue(event);
        if (venue == null || !eventList.add(event)) {
            System.err.println("No venue can host…");
            return false;
        }

        event.setVenue(venue);

        if (venue.isEmpty())
            venue.setEmpty(false);

        System.out.println("Hosted in " + venue);
        return true;
    }

    @Override
    public String toString() {
        return eventList.toString();
    }
}
