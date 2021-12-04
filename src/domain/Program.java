package domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Program {
    private static final int NUMBER_OF_VENUES = 4;

    private final int id;
    private final String name;
    private final boolean canUseNewVenue = true;
    private final List<Venue> venueList = new ArrayList<>(NUMBER_OF_VENUES);
    private final List<Event> eventList = new ArrayList<>();

    private boolean venueCanHostEvent(final Venue venue, final Event event) {
        // Check venue capacity
        if (venue.getCapacity() < event.getCapacity()) {
            System.err.println(venue + " capacity too low.");
            return false;
        }

        for (LocalDate date : event.getDates()) {
            DayOfWeek day = date.getDayOfWeek();

            // Check venue opening hours
            if (!venue.isOpened(day, event.getTimeSlot())) {
                System.err.println(venue + " not opened.");
                return false;
            }

            // Check venue events
            for (Event e : eventList) {
                for (LocalDate d : e.getDates()) {
                    DayOfWeek dow = d.getDayOfWeek();
                    if (e.getVenue().equals(venue) && dow.equals(day) && e.getTimeSlot().overlap(event.getTimeSlot())) {
                        System.err.println(venue + " not available.");
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private Venue findVenue(final Event event) {
        for (Venue venue : venueList)
            if ((!venue.isEmpty() || canUseNewVenue) && venueCanHostEvent(venue, event))
                return venue;
        return null;
    }

    public Program(final int id) {
        this.id = id;
        this.name = "Program #" + id;

        for (int i = 0; i < NUMBER_OF_VENUES; i++) {
            Map<DayOfWeek, TimeSlot> slots = new HashMap<>();
            for (DayOfWeek day : DayOfWeek.values())
                slots.put(day, new TimeSlot(LocalTime.of(19, 0), LocalTime.of(23, 0)));
            venueList.add(new Venue(1500, slots));
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

        System.out.println("Hosted in " + venue + ".");
        return true;
    }

    @Override
    public String toString() {
        return name + ": " + eventList;
    }
}
