package domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Program for a week
 * (Aggregate)
 * 
 * @author Bastien Soucasse
 * @author Iantsa Provost
 */
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

    /**
     * Checks if a venue is available at a given date.
     * 
     * @param venue…
     * @param date…
     * @return…
     */
    private boolean venueIsAvailable(final Venue venue, final LocalDate date) {
        // If (at least) one event has (at least) one date at venue, it's not available
        for (final Entry<Event, Map<LocalDate, Venue>> entry : eventMap.entrySet())
            if (((Map<LocalDate, Venue>) entry.getValue()).containsValue(venue))
                return false;

        return true;
    }

    /**
     * Checks if the venue is empty.
     *
     * @param venue…
     * @return…
     */
    private boolean venueIsEmpty(final Venue venue) {
        // If there are no concerts not plays, it's empty
        return numConcerts.get(venue) == 0 && numPlays.get(venue) == 0;
    }

    /**
     * Checks if it's possible to use a new (empty) venue.
     * 
     * @return true if there will be at least one venue available after; false otherwise.
     */
    private boolean canUseNewVenue() {
        int emptyVenues = 0;

        for (final Venue venue : venueList)
            if (numConcerts.get(venue) == 0 && numPlays.get(venue) == 0)
                emptyVenues++;

        return emptyVenues > 1;
    }

    /**
     * Finds a free venue at a given date and time slot.
     * 
     * @param date…
     * @param slot…
     * @return…
     */
    private Venue findFreeVenue(final LocalDate date, final TimeSlot slot) {
        for (final Venue venue : venueList) {
            // If the venue is closed or not available, check the next one
            if (!venue.isOpened(date.getDayOfWeek(), slot) || !venueIsAvailable(venue, date))
                continue;

            // If the venue is not empty, we can use this one
            if (!venueIsEmpty(venue))
                return venue;
            
            // The venue is empty, if it's possible to use a new one, use this one
            if (canUseNewVenue())
                return venue;
        }

        return null;
    }

    /**
     * Finds a free venue for each date of an event.
     * 
     * @param event…
     * @return…
     */
    private Map<LocalDate, Venue> findFreeVenues(final Event event) {
        final Map<LocalDate, Venue> venues = new HashMap<>();

        // Adds the venue found for each date
        for (final LocalDate date : event.getDates()) {
            final Venue venue = findFreeVenue(date, event.getTimeSlot());
            if (venue != null) venues.put(date, venue);
        }

        return venues;
    }

    /**
     * Gets the list of events that overlap with a given event, at a given venue.
     * 
     * @param event…
     * @param venue…
     * @return…
     */
    private List<Event> getOverlapEvents(final Event event, final Venue venue) {
        final List<Event> overlapEvents = new ArrayList<>();

        // Adds the event if it shares (at least) one date at the same venue.
        for (final Entry<Event, Map<LocalDate, Venue>> entry : eventMap.entrySet())
            for (final LocalDate date : event.getDates()) {
                final Map<LocalDate, Venue> venues = (Map<LocalDate, Venue>) entry.getValue();
                if (venues.containsKey(date) && venues.get(date).equals(venue))
                    overlapEvents.add((Event) entry.getKey());
            }

        return overlapEvents;
    }

    /**
     * Checks if an event list contains an instance of a class.
     * 
     * @param events…
     * @param class…
     * @return…
     */
    private boolean eventsContainsInstanceOf(final List<Event> events, Class<?> clazz) {
        // If there is at least one of such class, it's true
        for (final Event e : events)
            if (e.getClass() == clazz)
                return true;

        return false;
    }

    /**
     * Checks if a venue can host a complete event if it modifies some of its own events.
     * 
     * @param venue…
     * @param event…
     * @param overlapEvents…
     * @return…
     */
    private boolean canVenueHostEventIfModify(final Venue venue, final Event event) {
        final List<Event> overlapEvents = getOverlapEvents(event, venue);
    
        // We want to add a concert
        if (event instanceof Concert) {
            final Event e = overlapEvents.get(0);

            // If concert, NO
            if (e instanceof Concert)
                return false;
            
            // If play
            if (e instanceof Play) {
                // If both SATURDAY and SUNDAY, NO
                if (e.isFullyWE())
                    return false;

                // Not SATURDAY nor SUNDAY or only one
                // If first concert, YES
                if (numConcerts.get(venue) == 0)
                    return true;

                // Otherwise, NO
                return false;
            }
        }

        // We want to add a play
        if (event instanceof Play) {
            int sum = 0, concerts = 0;
            boolean sat = false, sun = false;
            for (final Event e : overlapEvents) {
                if (e instanceof Concert)
                    concerts++;
                if (e instanceof Play)
                    sum += e.getDates().size();
                if (e.isWESat())
                    sat = true;
                if (e.isWESun())
                    sun = true;
            }

            // If both sat and sun
            if (event.isWESat() && event.isWESun()) {
                // If only play(s)
                if (eventsContainsInstanceOf(overlapEvents, Play.class) && !eventsContainsInstanceOf(overlapEvents, Concert.class)) {
                    // If no SATURDAY or no SUNDAY, YES
                    if (!sat || !sun)
                        return true;
                    
                    // If longer then sum, YES
                    if (event.getDates().size() > sum)
                        return true;
                    
                    // Otherwise, NO
                    return false;
                }

                // If both concert(s) and play(s) or only concert(s)
                if (eventsContainsInstanceOf(overlapEvents, Concert.class)) {
                    // If both SATURDAY and SUNDAY, NO
                    if (sat && sun && concerts == numConcerts.get(venue))
                        return false;

                    // Otherwise, YES
                    return true;
                }
            }

            // Not sat nor sun or only one
            // If both concert(s) and play(s) or only concert(s), NO
            if (eventsContainsInstanceOf(overlapEvents, Concert.class))
                return false;

            // If only play(s)
            if (eventsContainsInstanceOf(overlapEvents, Play.class)) {
                // If longer, YES
                if (event.getDates().size() > sum)
                    return true;
                
                // Otherwise, NO
                return false;
            }
        }

        return false;
    }

    /**
     * Finds a venue for an event by modifying other events schedule.
     * 
     * @param event…
     * @return…
     */
    private Venue findVenueByModifying(final Event event) {
        for (final Venue venue : venueList)
            if (canVenueHostEventIfModify(venue, event))
                return venue;

        return null;
    }

    /**
     * Finds a venue for each event date.
     * 
     * @param event…
     * @return…
     */
    private Map<LocalDate, Venue> findVenues(final Event event) {
        // Try to get free venues
        Map<LocalDate, Venue> venues = findFreeVenues(event);
        if (venues.size() == event.getDates().size())
            return venues;
        
        // No free venues for all dates could be found
        venues.clear();
        for (final LocalDate date : event.getDates())
            venues.put(date, findVenueByModifying(event));
        return venues;
    }

    // private Venue findVenue(final LocalDate date, final Event newEvent) {
    // // Find an available venue
    // for (final Venue venue : venueList) {
    // // If venue is closed, we don't care
    // if (!venue.isOpened(date.getDayOfWeek(), newEvent.getTimeSlot())) continue;

    // boolean venueAvailable = true;

    // for (final Event existingEvent : eventMap.keySet()) {
    // final Map<LocalDate, Venue> v = eventMap.get(existingEvent);
    // if (v.keySet().contains(date) && v.get(date).equals(venue))
    // venueAvailable = false;
    // }

    // if (venueAvailable) {
    // if (numConcerts.get(venue) == 0 && numPlays.get(venue) == 0) {
    // if (canUseNewVenue())
    // return venue;
    // } else
    // return venue;
    // }
    // }

    // // Find a venue that can remove an existing event
    // for (final Venue venue : venueList) {
    // // If venue is closed, we don't care
    // if (!venue.isOpened(date.getDayOfWeek(), newEvent.getTimeSlot())) continue;

    // for (final Event existingEvent : eventMap.keySet()) {
    // if ()

    // for (LocalDate existingEventDate : existingEvent.getDates()) {
    // // If the existing event is not at this date, or not at this venue, we don't
    // care
    // if (!date.isEqual(existingEventDate) ||
    // !eventMap.get(existingEvent).get(existingEventDate).equals(venue)) continue;

    // boolean ok = false;

    // // If we have a concert
    // if (newEvent.getClass() == Concert.class) {
    // // If the existing event is a play
    // if (existingEvent.getClass() == Play.class) {
    // // If there are no concerts here yet, ok
    // if (numConcerts.get(venue) == 0)
    // if (eventHasWE(newEvent)) ok = true;
    // }
    // }

    // // If we have a play
    // if (newEvent.getClass() == Play.class) {
    // // If the play is (at least) the whole weekend, ok
    // final List<LocalDate> eventDates = newEvent.getDates();
    // boolean sat = false, sun = false;
    // for (LocalDate d : eventDates) {
    // if (d.getDayOfWeek() == DayOfWeek.SATURDAY) sat = true;
    // if (d.getDayOfWeek() == DayOfWeek.SUNDAY) sun = true;
    // }
    // if (sat && sun) ok = true;

    // // If the existing event is a concert
    // if (!ok && existingEvent.getClass() == Concert.class) {
    // // If there is another concert, ok
    // if (numConcerts.get(venue) > 1)
    // ok = true;
    // }

    // // If the existing event is a play
    // if (!ok && existingEvent.getClass() == Play.class) {
    // // If the new play has more dates, ok
    // if (newEvent.getDates().size() > existingEvent.getDates().size())
    // ok = true;
    // }
    // }

    // // If ok, try to relocate, or remove event
    // if (ok) {
    // final Venue relocation = findVenue(date, existingEvent);
    // if (relocation != null) eventMap.get(existingEvent).replace(date,
    // relocation);
    // else removedEvents.add(existingEvent);
    // return venue;
    // }
    // }
    // }
    // }

    // return null;
    // }

    // private Map<LocalDate, Venue> findVenues(final Event event) {
    // Map<LocalDate, Venue> dateVenue = new HashMap<>();
    // for (final LocalDate date : event.getDates()) {
    // final Venue venue = findVenue(date, event);
    // if (venue != null) dateVenue.put(date, venue);
    // }
    // return dateVenue;
    // }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Venue> getVenueList() {
        return venueList;
    }

    public Map<Event, Map<LocalDate, Venue>> getEventMap() {
        return eventMap;
    }

    public List<Event> getRemovedEvents() {
        return removedEvents;
    }

    public Map<Venue, Integer> getNumConcerts() {
        return numConcerts;
    }

    public Map<Venue, Integer> getNumPlays() {
        return numPlays;
    }

    public void clearRemovedEvents() {
        if (removedEvents.size() == 0)
            return;
        for (Event e : removedEvents)
            eventMap.remove(e);
        removedEvents.clear();
    }

    public boolean add(final Event event) {
        final Map<LocalDate, Venue> venues = findVenues(event);
        if (venues.isEmpty())
            return false;

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
            s += "\n - " + e.toStringWithoutDates() + " on ";
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
