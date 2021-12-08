package domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Program {
    private final int id; // Week number
    private final String name;
    private final List<Venue> venueList;
    private final Map<Event, Map<LocalDate, Venue>> eventMap = new HashMap<>();

    private final List<Event> removedEvents = new ArrayList<>();
    private final Map<Venue, Integer> numConcerts;
    private final Map<Venue, Integer> numPlays;

    public Program(final int id, final List<Venue> venueList) {
        this.id = id;
        this.name = "Program #" + id;
        this.venueList = venueList;

        numConcerts = new HashMap<>(venueList.size());
        numPlays = new HashMap<>(venueList.size());

        for (Venue venue : venueList) {
            numConcerts.put(venue, 0);
            numPlays.put(venue, 0);
        }
    }

    private boolean canUseNewVenue() {
        int emptyVenues = 0;

        for (Venue venue : venueList)
            if (numConcerts.get(venue) == 0 && numPlays.get(venue) == 0)
                emptyVenues++;

        return emptyVenues > 1;
    }

    private Map<LocalDate, Venue> findVenues(final Event event) {
        Map<LocalDate, Venue> dateVenue = new HashMap<>();

        for (LocalDate date : event.getDates()) {
            // Find an available venue
            for (Venue venue : venueList) {
                boolean venueAvailable = true;
                for (Event e : eventMap.keySet()) {
                    final Map<LocalDate, Venue> v = eventMap.get(e);
                    if (v.keySet().contains(date) && v.get(date).equals(venue))
                        venueAvailable = false;
                }
                if (venueAvailable && venue.isOpened(date.getDayOfWeek(), event.getTimeSlot())) {
                    if (numConcerts.get(venue) == 0 && numPlays.get(venue) == 0) {
                        if (canUseNewVenue())
                            dateVenue.put(date, venue);
                    } else
                        dateVenue.put(date, venue);
                }
                if (dateVenue.containsKey(date))
                    break;
            }

            // Find a venue that can remove an existing event
            if (!dateVenue.containsKey(date)) {
                boolean ok = false;

                for (Venue venue : venueList) {
                    for (Event e : eventMap.keySet()) {
                        // if (event.getClass() == Concert.class)
                        // Nothing to do

                        if (event.getClass() == Play.class) {
                            // Put a play over a concert?
                            if (e.getClass() == Concert.class) {
                                // If there is another concert, ok
                                if (numConcerts.get(venue) > 1)
                                    ok = true;

                                // If the play is the whole weekend, ok
                                final List<LocalDate> eventDates = event.getDates();
                                if (date.getDayOfWeek() == DayOfWeek.SATURDAY
                                        || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                                    for (LocalDate d : eventDates)
                                        if (date.getDayOfWeek() == DayOfWeek.SATURDAY
                                                && d.getDayOfWeek() == DayOfWeek.SUNDAY
                                                || date.getDayOfWeek() == DayOfWeek.SUNDAY
                                                        && d.getDayOfWeek() == DayOfWeek.SATURDAY)
                                            ok = true;
                                }
                            }

                            // Put a play over a play?
                            if (e.getClass() == Play.class) {
                                // If the new play has more dates, ok
                                if (event.getDates().size() > e.getDates().size())
                                    ok = true;
                            }
                        }

                        // Ok, remove existing event and add new one
                        if (ok) {
                            removedEvents.add(e);
                            dateVenue.put(date, venue);
                            break;
                        }
                    }

                    if (ok)
                        break;
                }
            }
        }

        return dateVenue;
    }

    public int getId() {
        return id;
    }

    public List<Event> getRemovedEvents() {
        return removedEvents;
    }

    public void clearRemovedEvents() {
        if (removedEvents.size() == 0) return;
        for (Event e : removedEvents) eventMap.remove(e);
        removedEvents.clear();
    }

    public boolean add(final Event event) {
        final Map<LocalDate, Venue> venues = findVenues(event);
        if (venues.isEmpty()) return false;

        for (Map.Entry<LocalDate, Venue> entry : venues.entrySet()) {
            if (event.getClass() == Play.class)
                numPlays.replace(entry.getValue(), numPlays.get(entry.getValue()) + 1);
            else
                numConcerts.replace(entry.getValue(), numConcerts.get(entry.getValue()) + 1);
        }

        eventMap.put(event, venues);

        return true;
    }
    

    @Override
    public String toString() {
        String s = "\n" + name + ": ";
        for (Event e : eventMap.keySet()) {
            s += "\n - " + e + " on ";
            boolean first = true;
            for (LocalDate d : e.getDates()) {
                if (first)
                    first = false;
                else
                    s += ", ";
                s += d.getDayOfWeek().name().toLowerCase() + " at " + eventMap.get(e).get(d);
            }
            s += ".";
        }
        return s;
    }
}
