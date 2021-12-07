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

    // private boolean venueCanHostEvent(final Venue venue, final Event event, final
    // boolean modify) {
    // // Check venue capacity
    // if (venue.getCapacity() < event.getCapacity()) return false;

    // for (LocalDate date : event.getDates()) {
    // DayOfWeek day = date.getDayOfWeek();

    // // Check venue opening hours
    // if (!venue.isOpened(day, event.getTimeSlot())) return false;

    // // Check venue events
    // for (Event e : eventMap.keySet()) {
    // for (LocalDate d : e.getDates()) {
    // DayOfWeek dow = d.getDayOfWeek();

    // if (eventMap.get(e).equals(venue) && dow.equals(day) &&
    // e.getTimeSlot().overlap(event.getTimeSlot())) {
    // if (!modify) return false;

    // if (event.getClass() == Concert.class && e.getClass() == Play.class &&
    // !eventMap.get(e).doesHostConcert()) {
    // removedEvent = e;
    // eventMap.remove(e);
    // return true;
    // }
    // }
    // }

    // if (modify && event.getClass() == Play.class && e.getClass() == Play.class &&
    // event.getDates().size() > e.getDates().size()) {
    // List<LocalDate> overdates = overlapDates(event.getDates(), e.getDates());
    // if (overdates.size() > 0) // s'il y a des dates dates qui dépassent
    // for (LocalDate overdate : overdates) // pour chaque date qui dépasse
    // for (Event ev : eventMap.keySet()) // pour chaque évènement
    // if (ev.getDates().contains(overdate)) // si la date qui dépasse est prise par
    // l'évènement
    // return false;
    // }
    // }
    // }

    // return !modify;
    // }

    // private List<LocalDate> overlapDates(List<LocalDate> newDates,
    // List<LocalDate> oldDates) {
    // List<LocalDate> overdates = new ArrayList<>();

    // if (newDates.get(0).isBefore(oldDates.get(0)))
    // for (LocalDate d : newDates) {
    // if (d.equals(oldDates.get(0)))
    // return overdates;
    // overdates.add(d);
    // }

    // boolean add = false;
    // for (LocalDate d : newDates) {
    // if (add) overdates.add(d);
    // if (d.equals(oldDates.get(oldDates.size()-1))) add = true;
    // }

    // return overdates;
    // }

    // private boolean venueCanHostEvent(final Venue venue, final Event event) {
    // return venueCanHostEvent(venue, event, false);
    // }

    // private Venue findVenue(final Event event) {
    // for (Venue venue : venueList)
    // if ((!venue.isEmpty() || canUseNewVenue) && venueCanHostEvent(venue, event))
    // return venue;

    // for (Venue venue : venueList)
    // if ((!venue.isEmpty() || canUseNewVenue) && venueCanHostEvent(venue, event,
    // true))
    // return venue;

    // return null;
    // }

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

    private void removeEvents() {
        if (removedEvents.size() == 0) return;
        System.out.print("Removed events: ");

        boolean first = true;
        for (Event e : removedEvents) {
            if (first) first = false;
            else System.out.print(", ");
            System.out.print(e);

            eventMap.remove(e);
        }

        System.out.println(".");
        removedEvents.clear();
    }

    public int getId() {
        return id;
    }

    public boolean add(final Event event) {
        System.out.print("- " + event + ": ");

        final Map<LocalDate, Venue> venues = findVenues(event);
        if (venues.isEmpty()) {
            System.out.println("not hosted. ¬");
            removeEvents();
            return false;
        }

        for (Map.Entry<LocalDate, Venue> entry : venues.entrySet()) {
            if (event.getClass() == Play.class)
                numPlays.replace(entry.getValue(), numPlays.get(entry.getValue()) + 1);
            else
                numConcerts.replace(entry.getValue(), numConcerts.get(entry.getValue()) + 1);
        }

        eventMap.put(event, venues);
        System.out.println("hosted. √");

        removeEvents();
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
