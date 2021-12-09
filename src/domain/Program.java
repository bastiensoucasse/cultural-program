package domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Program for a week.
 * (Aggregate)
 *
 * @author Bastien Soucasse
 * @author Iantsa Provost
 */
public class Program {
    private final int id;
    private final String name;
    private final List<Venue> venueList;
    private final Map<Event, Map<LocalDate, Venue>> eventMap = new HashMap<>();

    private transient List<Event> removedEvents = new ArrayList<>();
    private final transient Map<Venue, Integer> numConcerts;
    private final transient Map<Venue, Integer> numPlays;

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

    /**
     * Checks if a venue is available at a given date.
     *
     * @param venue …
     * @param date  …
     * @return …
     */
    private boolean venueIsAvailable(final Venue venue, final LocalDate date) {
        // If (at least) one event has (at least) one date at venue, it's not available
        for (final Entry<Event, Map<LocalDate, Venue>> entry : eventMap.entrySet()) {
            final Map<LocalDate, Venue> venues = entry.getValue();
            if (venues.containsKey(date) && venues.get(date).equals(venue)) return false;
        }

        return true;
    }

    /**
     * Checks if the venue is empty.
     *
     * @param venue …
     * @return …
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
            if (numConcerts.get(venue) == 0 && numPlays.get(venue) == 0) emptyVenues++;

        return emptyVenues > 1;
    }

    /**
     * Finds a free venue at a given date and time slot for a given capacity.
     *
     * @param date …
     * @param slot …
     * @param capacity …
     * @return …
     */
    private Venue findFreeVenue(final LocalDate date, final TimeSlot slot, final int capacity) {
        for (final Venue venue : venueList) {
            // If the venue is closed, can't host for such capacity, or not available, check the next one
            if (!venue.isOpened(date.getDayOfWeek(), slot)  || !venue.canHost(capacity) || !venueIsAvailable(venue, date)) continue;

            // If the venue is not empty, we can use this one
            if (!venueIsEmpty(venue)) return venue;

            // The venue is empty, if it's possible to use a new one, use this one
            if (canUseNewVenue()) return venue;
        }

        return null;
    }

    /**
     * Finds a free venue for each date of an event.
     *
     * @param event …
     * @return …
     */
    private Map<LocalDate, Venue> findFreeVenues(final Event event) {
        final Map<LocalDate, Venue> venues = new HashMap<>();

        // Adds the venue found for each date
        for (final LocalDate date : event.getDates()) {
            final Venue venue = findFreeVenue(date, event.getSlot(), event.getCapacity());
            if (venue != null) venues.put(date, venue);
        }

        return venues;
    }

    /**
     * Gets the list of events that overlap with a given event, at a given venue.
     *
     * @param event …
     * @param venue …
     * @return …
     */
    private List<Event> getOverlapEvents(final Event event, final Venue venue) {
        final List<Event> overlapEvents = new ArrayList<>();

        // Adds the event if it shares (at least) one date at the same venue.
        for (final Entry<Event, Map<LocalDate, Venue>> entry : eventMap.entrySet())
            for (final LocalDate date : event.getDates()) {
                final Map<LocalDate, Venue> venues = entry.getValue();
                if (venues.containsKey(date) && venues.get(date).equals(venue)) overlapEvents.add(entry.getKey());
            }

        return overlapEvents;
    }

    /**
     * Checks if an event list contains an instance of a class.
     *
     * @param events …
     * @param clazz  …
     * @return …
     */
    private boolean eventsContainsInstanceOf(final List<Event> events, Class<?> clazz) {
        for (final Event e : events) if (e.getClass() == clazz) return true;
        return false;
    }

    /**
     * Checks if a venue can host a complete event if it modifies some of its own events.
     *
     * @param venue …
     * @param event …
     * @return …
     */
    private boolean canVenueHostEventIfModify(final Venue venue, final Event event) {
        final List<Event> overlapEvents = getOverlapEvents(event, venue);

        // If the venue can't host for such capacity or is closed, NO
        if (!venue.canHost(event.getCapacity())) return false;
        for (final LocalDate d : event.getDates()) if (!venue.isOpened(d.getDayOfWeek(), event.getSlot())) return false;

        // We want to add a concert
        if (event instanceof Concert) {
            final Event e = overlapEvents.get(0);

            // If concert, NO
            if (e instanceof Concert) return false;

            // If play
            if (e instanceof Play) {
                // If both SATURDAY and SUNDAY, NO
                if (e.isOnSaturday() && e.isOnSunday()) return false;

                // Not SATURDAY nor SUNDAY or only one
                // If first concert, YES
                return numConcerts.get(venue) == 0;

                // Otherwise, NO
            }
        }

        // We want to add a play
        if (event instanceof Play) {
            int sum = 0, concerts = 0;
            boolean sat = false, sun = false;
            for (final Event e : overlapEvents) {
                if (e instanceof Concert) concerts++;
                if (e instanceof Play) sum += e.getDates().size();
                if (e.isOnSaturday()) sat = true;
                if (e.isOnSunday()) sun = true;
            }

            // If both sat and sun
            if (event.isOnSaturday() && event.isOnSunday()) {
                // If only play(s)
                if (eventsContainsInstanceOf(overlapEvents, Play.class) && !eventsContainsInstanceOf(overlapEvents, Concert.class)) {
                    // If no SATURDAY or no SUNDAY, YES
                    if (!sat || !sun) return true;

                    // If longer then sum, YES
                    return event.getDates().size() > sum;

                    // Otherwise, NO
                }

                // If both concert(s) and play(s) or only concert(s)
                if (eventsContainsInstanceOf(overlapEvents, Concert.class)) {
                    // If both SATURDAY and SUNDAY, NO
                    return !sat || !sun || concerts != numConcerts.get(venue);

                    // Otherwise, YES
                }
            }

            // Not sat nor sun or only one
            // If both concert(s) and play(s) or only concert(s), NO
            if (eventsContainsInstanceOf(overlapEvents, Concert.class)) return false;

            // If only play(s)
            if (eventsContainsInstanceOf(overlapEvents, Play.class)) {
                // If longer, YES
                return event.getDates().size() > sum;

                // Otherwise, NO
            }
        }

        return false;
    }

    /**
     * Finds a venue for an event by modifying other events schedule.
     *
     * @param event …
     * @return …
     */
    private Venue findVenueByModifying(final Event event) {
        for (final Venue venue : venueList)
            if (canVenueHostEventIfModify(venue, event)) {
                removedEvents = new ArrayList<>(getOverlapEvents(event, venue));
                return venue;
            }

        return null;
    }

    /**
     * Finds a venue for each event date.
     *
     * @param event …
     * @return …
     */
    private Map<LocalDate, Venue> findVenues(final Event event) {
        // Try to get free venues
        Map<LocalDate, Venue> venues = findFreeVenues(event);
        if (venues.size() == event.getDates().size()) return venues;

        // No free venues for all dates could be found
        venues.clear();
        for (final LocalDate date : event.getDates()) {
            final Venue venue = findVenueByModifying(event);
            if (venue != null) venues.put(date, venue);
        }
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

    public void clearRemovedEvents() {
        if (removedEvents.size() == 0) return;
        for (Event e : removedEvents)
            eventMap.remove(e);
        removedEvents.clear();
    }

    public boolean add(final Event event) {
        final Map<LocalDate, Venue> venues = findVenues(event);
        if (venues.isEmpty()) return false;

        for (Map.Entry<LocalDate, Venue> entry : venues.entrySet()) {
            if (event.getClass() == Concert.class) numConcerts.replace(entry.getValue(), numConcerts.get(entry.getValue()) + 1);
            if (event.getClass() == Play.class) numPlays.replace(entry.getValue(), numPlays.get(entry.getValue()) + 1);
        }

        eventMap.put(event, venues);
        return true;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("\n" + name + ": ");
        for (final Entry<Event, Map<LocalDate, Venue>> entry : eventMap.entrySet()) {
            final Event event = entry.getKey();
            final Map<LocalDate, Venue> dateVenueMap = entry.getValue();
            s.append("\n - ").append(event).append(", at respectively ");
            boolean first = true;
            for (final Entry<LocalDate, Venue> entry1 : dateVenueMap.entrySet()) {
                if (first) first = false;
                else s.append(", ");
                final Venue venue = entry1.getValue();
                s.append(venue);
            }
            s.append(".");
        }
        return s.toString();
    }
}
