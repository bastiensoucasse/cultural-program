package domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Program {
    private static final int NUMBER_OF_VENUES = 4;
    private static final List<Venue> venueList = new ArrayList<>(NUMBER_OF_VENUES);

    private final int id; // Week number
    private final String name;
    private final Map<Event, Map<LocalDate, Venue>> eventMap = new HashMap<>();
    
    private Map<Venue, Integer> numConcerts = new HashMap<>(NUMBER_OF_VENUES);
    private Map<Venue, Integer> numPlays = new HashMap<>(NUMBER_OF_VENUES);
    private List<Event> removedEvents = new ArrayList<>();

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

    private boolean canUseNewVenue() {
        int emptyVenues = 0;

        for (Venue venue : venueList)
            if (numConcerts.get(venue) == 0 && numPlays.get(venue) == 0)
                emptyVenues++;

        return emptyVenues > 1;
    }

    // private boolean venueCanHostEvent(final Venue venue, final Event event, final boolean modify) {
    //     // Check venue capacity
    //     if (venue.getCapacity() < event.getCapacity()) return false;

    //     for (LocalDate date : event.getDates()) {
    //         DayOfWeek day = date.getDayOfWeek();

    //         // Check venue opening hours
    //         if (!venue.isOpened(day, event.getTimeSlot())) return false;

    //         // Check venue events
    //         for (Event e : eventMap.keySet()) {
    //             for (LocalDate d : e.getDates()) {
    //                 DayOfWeek dow = d.getDayOfWeek();

    //                 if (eventMap.get(e).equals(venue) && dow.equals(day) && e.getTimeSlot().overlap(event.getTimeSlot())) {
    //                     if (!modify) return false;

    //                     if (event.getClass() == Concert.class && e.getClass() == Play.class && !eventMap.get(e).doesHostConcert()) {
    //                         removedEvent = e;
    //                         eventMap.remove(e);
    //                         return true;
    //                     }
    //                 }
    //             }

    //             if (modify && event.getClass() == Play.class && e.getClass() == Play.class && event.getDates().size() > e.getDates().size()) {
    //                 List<LocalDate> overdates = overlapDates(event.getDates(), e.getDates());
    //                 if (overdates.size() > 0) // s'il y a des dates dates qui dépassent
    //                     for (LocalDate overdate : overdates) // pour chaque date qui dépasse
    //                         for (Event ev : eventMap.keySet()) // pour chaque évènement
    //                             if (ev.getDates().contains(overdate)) // si la date qui dépasse est prise par l'évènement 
    //                                 return false;
    //             }
    //         }
    //     }

    //     return !modify;
    // }

    // private List<LocalDate> overlapDates(List<LocalDate> newDates, List<LocalDate> oldDates) {
    //     List<LocalDate> overdates = new ArrayList<>();

    //     if (newDates.get(0).isBefore(oldDates.get(0)))
    //         for (LocalDate d : newDates) {
    //             if (d.equals(oldDates.get(0)))
    //                 return overdates;
    //             overdates.add(d);
    //         }
        
    //     boolean add = false;
    //     for (LocalDate d : newDates) {
    //         if (add) overdates.add(d);
    //         if (d.equals(oldDates.get(oldDates.size()-1))) add = true;
    //     }
        
    //     return overdates;
    // }

    // private boolean venueCanHostEvent(final Venue venue, final Event event) {
    //     return venueCanHostEvent(venue, event, false);
    // }

    // private Venue findVenue(final Event event) {
    //     for (Venue venue : venueList)
    //         if ((!venue.isEmpty() || canUseNewVenue) && venueCanHostEvent(venue, event))
    //             return venue;

    //     for (Venue venue : venueList)
    //         if ((!venue.isEmpty() || canUseNewVenue) && venueCanHostEvent(venue, event, true))
    //             return venue;

    //     return null;
    // }

    private void removeEvents(List<Event> toRemove) {
        for (Event e : toRemove)
            eventMap.remove(e);
    }

    private Map<LocalDate,Venue> findVenues(final Event event) {
        Map<LocalDate, Venue> dateVenue = new HashMap<>();
        // List<Event> removedEvents = new ArrayList<>(); // Set as attribute

        for (LocalDate date : event.getDates()) {
            for (Venue venue : venueList) {
                // Check if venue is available
                boolean available = true;
                for (Event e : eventMap.keySet())
                    if (eventMap.get(e).get(date).equals(venue))
                        available = false;
                if (available)
                    dateVenue.put(date, venue);
            }


            if (!dateVenue.containsKey(date)) {
                for (Venue venue : venueList) {
                    for (Event e : eventMap.keySet()) {
                        // if (event.getClass() == Concert.class) Nothing to
                        if (event.getClass() == Play.class
                        && ((e.getClass() == Concert.class && numConcertsInVenues.get(venue) > 1)
                        || (e.getClass() == Play.class && event.getDates().size() > e.getDates().size()))) {
                            dateVenue.put(date, venue);
                            removedEvents.add(e);
                            break;
                        }
                    }                        
                }
            }
        }
    
        removeEvents(removedEvents);
        return dateVenue;
    }

    public int getId() {
        return id;
    }

    public boolean add(final Event event) {
        System.out.print("- " + event + ": ");

        final Map<LocalDate, Venue> venues = findVenues(event);
        if (venues.isEmpty()) {
            System.out.println("No venue can host");
            return false;
        }

        // TODO: Removed events
    
        return true;
    }

    @Override
    public String toString() {
        String s = "\n" + name + ": ";
        for (Event e : eventMap.keySet())
            s += "\n - " + e + " at " + eventMap.get(e);
        return s;
    }
}
