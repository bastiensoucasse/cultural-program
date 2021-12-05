package domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Program {
    private static final int NUMBER_OF_VENUES = 4;

    private final int id;
    private final String name;
    private final List<Venue> venueList = new ArrayList<>(NUMBER_OF_VENUES);
    private boolean canUseNewVenue = true;
    private final List<Event> eventList = new ArrayList<>();

    public Program(final int id) {
        this.id = id;
        this.name = "Program #" + id;

        for (int i = 0; i < NUMBER_OF_VENUES; i++) {
            Map<DayOfWeek, TimeSlot> slots = new EnumMap<>(DayOfWeek.class);
            for (DayOfWeek day : DayOfWeek.values())
                slots.put(day, new TimeSlot(LocalTime.of(19, 0), LocalTime.of(23, 0)));
            venueList.add(new Venue(1500, slots));
        }
    }

    private void updateCanUseNewVenues() {
        int counter = (int) venueList.stream().filter(v -> !v.isEmpty()).count();
        if (counter == NUMBER_OF_VENUES - 1) canUseNewVenue = false;
    }

    private boolean venueCanHostEvent(final Venue venue, final Event event) {
        // Check venue capacity
        if (venue.getCapacity() < event.getCapacity()) {
            System.out.println(venue + " capacity too low.");
            return false;
        }

        for (LocalDate date : event.getDates()) {
            DayOfWeek day = date.getDayOfWeek();

            // Check venue opening hours
            if (!venue.isOpened(day, event.getTimeSlot())) {
                System.out.println(venue + " not opened.");
                return false;
            }

            // Check venue events
            for (Event e : eventList)
                for (LocalDate d : e.getDates()) {
                    DayOfWeek dow = d.getDayOfWeek();
                    if (e.getVenue().equals(venue) && dow.equals(day) && e.getTimeSlot().overlap(event.getTimeSlot())) {
                        System.out.println(venue + " not available.");
                        return false;
                    }
                }
        }

        return true;
    }

    private Venue findVenue(final Event event) {
        return venueList.stream().filter(venue -> (!venue.isEmpty() || canUseNewVenue) && venueCanHostEvent(venue, event)).findFirst().orElse(null);
    }

    public int getId() {
        return id;
    }

    public boolean add(final Event event) {
        System.out.println("- " + event);

        final Venue venue = findVenue(event);
        if (venue == null) {
            System.out.println("No venue can host…");
            return false;
        }

        eventList.add(event);
        event.setVenue(venue);
        if (venue.isEmpty()) {
            venue.setEmpty(false);
            if (canUseNewVenue) updateCanUseNewVenues();
        }

        System.out.println("Hosted in " + venue + ".");
        return true;
    }

    @Override
    public String toString() {
        return name + ": " + eventList;
    }
}
