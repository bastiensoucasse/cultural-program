package domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private final transient Map<Venue, Integer> numConcerts;
    private final transient Map<Venue, Integer> numPlays;
    private transient List<Event> removedEventList = new ArrayList<>();

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
        return removedEventList;
    }

    /**
     * Checks if a venue is available at a given date.
     *
     * @param venue The venue to check.
     * @param date  The date to check.
     * @return <code>true</code> if the venue is available; <code>false</code> otherwise.
     */
    private boolean venueIsAvailable(final Venue venue, final LocalDate date) {
        for (final Entry<Event, Map<LocalDate, Venue>> entry : eventMap.entrySet()) {
            final Map<LocalDate, Venue> venues = entry.getValue();
            if (venues.containsKey(date) && venues.get(date).equals(venue)) return false;
        }

        return true;
    }

    /**
     * Checks if the venue is empty.
     *
     * @param venue The venue to check.
     * @return <code>true</code> if the venue is empty; <code>false</code> otherwise.
     */
    private boolean venueIsEmpty(final Venue venue) {
        return numConcerts.get(venue) == 0 && numPlays.get(venue) == 0;
    }

    /**
     * Checks if it's possible to use a new (empty) venue.
     *
     * @return <code>true</code> if there will be at least one venue available after; <code>false</code> otherwise.
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
     * @param date     The date to consider.
     * @param slot     The time slot to consider.
     * @param capacity The capacity asked by the event.
     * @return The venue found if so; <code>null</code> otherwise.
     */
    private Venue findFreeVenue(final LocalDate date, final TimeSlot slot, final int capacity) {
        for (final Venue venue : venueList) {
            if (!venue.isOpened(date.getDayOfWeek(), slot) || !venue.canHost(capacity) || !venueIsAvailable(venue, date))
                continue;

            if (!venueIsEmpty(venue)) return venue;
            if (canUseNewVenue()) return venue;
        }

        return null;
    }

    /**
     * Finds a free venue for each date of an event.
     *
     * @param event The event to consider.
     * @return The map of venue found per date of the event.
     */
    private Map<LocalDate, Venue> findFreeVenues(final Event event) {
        final Map<LocalDate, Venue> venues = new HashMap<>();

        for (final LocalDate date : event.getDates()) {
            final Venue venue = findFreeVenue(date, event.getSlot(), event.getCapacity());
            if (venue != null) venues.put(date, venue);
        }

        return venues;
    }

    /**
     * Gets the list of events that overlap with a given event, in a given venue.
     *
     * @param event The event to consider.
     * @param venue The venue to look into.
     * @return The list of events that overlap with the event in the venue.
     */
    private List<Event> getOverlapEvents(final Event event, final Venue venue) {
        final List<Event> overlapEvents = new ArrayList<>();

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
     * @param events The event list to look into.
     * @param clazz  The class wanted.
     * @return <code>true</code> if an instance of the class was found; <code>false</code> otherwise.
     */
    private boolean eventsContainsInstanceOf(final List<Event> events, Class<?> clazz) {
        for (final Event e : events) if (e.getClass() == clazz) return true;
        return false;
    }

    private boolean canVenueHostEventIfModifyAlt(final Venue venue, final Event event) {
        // If the venue can't host for such capacity or is closed, NO
        if (!venue.canHost(event.getCapacity())) return false;
        for (final LocalDate d : event.getDates()) if (!venue.isOpened(d.getDayOfWeek(), event.getSlot())) return false;

        final List<Event> overlapEvents = getOverlapEvents(event, venue);

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
     * Checks if a venue can host a complete event if it modifies some of its own events.
     * The modification will consist on rescheduling or removing the events that overlap with this event.
     *
     * @param venue The venue to check.
     * @param event The event to consider.
     * @return <code>true</code> if the venue can host the event after modification; <code>false</code> otherwise.
     */
    private boolean canVenueHostEventIfModify(final Venue venue, final Event event) {
        // If the venue is the empty one, can't host for such capacity, or is closed, NO
        if (venueIsEmpty(venue)) return false; // If we are here, and we find the empty one, it means it's the reserved one.
        if (!venue.canHost(event.getCapacity())) return false;
        for (final LocalDate d : event.getDates()) if (!venue.isOpened(d.getDayOfWeek(), event.getSlot())) return false;

        final List<Event> overlapEvents = getOverlapEvents(event, venue);
        int sum = 0, concerts = 0;
        boolean sat = false, sun = false;
        for (Event e : overlapEvents) {
            sum += e.getDates().size();
            if (e instanceof Concert) concerts++;
            if (e.isOnSaturday()) sat = true;
            if (e.isOnSunday()) sun = true;
        }

        // The new event is not on the whole weekend
        if (!event.isOnSaturday() || !event.isOnSunday()) {
            // The overlap events are not on the whole weekend
            if (!sat || !sun) {
                // The new event is a concert, and it's the first one in the week in this venue, YES
                if (event instanceof Concert && numConcerts.get(venue) == 0) return true;

                // The new event is longer than all the events that overlap
                if (event.getDates().size() > sum) {
                    // There are still concerts in the week in the venue, YES; otherwise, NO
                    return concerts != numConcerts.get(venue);
                }

                // Otherwise, NO
                return false;
            }

            // The overlap events are on the whole weekend, NO
            return false;
        }

        // The new event is on the whole weekend

        // The overlap events are not on the whole weekend, YES
        if (!sat || !sun) return true;

        // The overlap events are on the whole weekend

        // All the concerts of the venue in the week overlap, NO
        if (concerts == numConcerts.get(venue)) return false;

        // The new event is longer than all the events that overlap, YES; otherwise NO
        return event.getDates().size() > sum;
    }

    /**
     * Finds a venue for an event by modifying other events schedule.
     *
     * @param event The event to consider.
     * @return The venue found if so; <code>null</code> otherwise.
     */
    private Venue findVenueByModifying(final Event event) {
        for (final Venue venue : venueList)
            if (canVenueHostEventIfModify(venue, event)) {
                removedEventList = new ArrayList<>(getOverlapEvents(event, venue));
                return venue;
            }

        return null;
    }

    /**
     * Finds a venue for each event date.
     *
     * @param event The event to consider.
     * @return The map of venue found per date of event.
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

    /**
     * Clear the removed event list.
     */
    public void clearRemovedEventList() {
        if (removedEventList.size() == 0) return;
        for (Event e : removedEventList) eventMap.remove(e);
        removedEventList.clear();
    }

    /**
     * Adds an event to the program of the week if possible.
     *
     * @param event The event to add.
     * @return <code>true</code> if the event has been successfully added; <code>false</code> otherwise.
     */
    public boolean add(final Event event) {
        final Map<LocalDate, Venue> venues = findVenues(event);
        if (venues.isEmpty()) return false;

        for (Map.Entry<LocalDate, Venue> entry : venues.entrySet()) {
            if (event.getClass() == Concert.class)
                numConcerts.replace(entry.getValue(), numConcerts.get(entry.getValue()) + 1);
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
            s.append("\n - ").append(event).append(" (");
            boolean first = true;
            for (final Entry<LocalDate, Venue> entry1 : dateVenueMap.entrySet()) {
                if (first) first = false;
                else s.append(", ");
                final LocalDate date = entry1.getKey();
                final Venue venue = entry1.getValue();
                s.append(date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d"))).append(" at ").append(venue);
            }
            s.append(").");
        }
        return s.toString();
    }
}
