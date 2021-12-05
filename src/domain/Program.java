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
                slots.put(day, new TimeSlot(LocalTime.of(18, 0), LocalTime.of(23, 0)));
            venueList.add(new Venue(1500, slots));
        }
    }

    private void updateCanUseNewVenues() {
        int counter = (int) venueList.stream().filter(v -> !v.isEmpty()).count();
        if (counter == NUMBER_OF_VENUES - 1) canUseNewVenue = false;
    }

    private Pair<boolean, Event> venueCanHostEvent(final Venue venue, final Event event, final boolean modify) {
        // Check venue capacity
        if (venue.getCapacity() < event.getCapacity()) return false;

        for (LocalDate date : event.getDates()) {
            DayOfWeek day = date.getDayOfWeek();

            // Check venue opening hours
            if (!venue.isOpened(day, event.getTimeSlot())) return false;

            // Check venue events
            for (Event e : eventList)
                for (LocalDate d : e.getDates()) {
                    DayOfWeek dow = d.getDayOfWeek();

                    if (e.getVenue().equals(venue) && dow.equals(day) && e.getTimeSlot().overlap(event.getTimeSlot())) {
                        if (!modify) return false;

                        if (event.getClass() == Concert.class && e.getClass() == Play.class && !e.getVenue().doesHostConcert()) {
                            System.out.println("[WARNING] We have to remove " + e + "!");
                            eventList.remove(e);
                            return true;
                        }
                    }
                }
        }

        return new Pair.which(!modify, null);
    }

    private Pair<boolean, Event> venueCanHostEvent(final Venue venue, final Event event) {
        return venueCanHostEvent(venue, event, false);
    }

    private Venue findVenue(final Event event) {
        // return venueList.stream().filter(venue -> (!venue.isEmpty() || canUseNewVenue) && venueCanHostEvent(venue, event)).findFirst().orElse(null);

        for (Venue venue : venueList)
            if ((!venue.isEmpty() || canUseNewVenue) && venueCanHostEvent(venue, event))
                return venue;

        for (Venue venue : venueList)
            if ((!venue.isEmpty() || canUseNewVenue) && venueCanHostEvent(venue, event, true))
                return venue;

        return null;
    }

    public int getId() {
        return id;
    }

    public boolean add(final Event event) {
        System.out.print("- " + event + ": ");

        final Venue venue = findVenue(event);
        if (venue == null) {
            System.out.println("no venue can host.");
            return false;
        }

        eventList.add(event);
        event.setVenue(venue);
        if (venue.isEmpty()) {
            venue.setEmpty(false);
            if (canUseNewVenue) updateCanUseNewVenues();
        }

        if (event.getClass() == Concert.class && !venue.doesHostConcert())
            venue.setHostConcert(true);

        System.out.println("hosted in " + venue + ".");
        return true;
    }

    @Override
    public String toString() {
        String s = "\n" + name + ": ";
        for (Event e : eventList)
            s += "\n - " + e;
        return s;
    }
}
