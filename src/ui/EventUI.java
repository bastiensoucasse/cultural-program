package ui;

import domain.Concert;
import domain.Event;
import domain.Play;
import domain.TimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Event UI.
 *
 * @author Bastien Soucasse
 * @author Iantsa Provost
 */
public class EventUI extends AbstractUI {
    protected static int year = 2022;

    /**
     * Asks the user for information needed to create an event.
     * User should enter valid input, according to what is asked.
     *
     * @return The created event.
     */
    public static Event retrieveEvent() {
        System.out.println("\n*** Schedule an event *** ");

        System.out.println("What type of event do you want to schedule ?");
        System.out.println("0. Concert");
        System.out.println("1. Play");
        System.out.print("Enter an option: ");
        int eventType = Integer.parseInt(retrieveInfo());

        if (eventType == 0) // should not be -1
            System.out.print("Enter artist/group name: ");
        else System.out.print("Enter title: ");
        String artist_title = retrieveInfo();

        if (eventType == 0) // should not be -1
            System.out.print("Enter date (MM-dd): ");
        else System.out.print("Enter first date of representation (MM-dd): ");

        LocalDate date = LocalDate.parse(year + "-" + retrieveInfo());

        LocalDate endDate = null;
        if (eventType == 1) {
            System.out.print("Enter last date of representation (MM-dd): ");
            endDate = LocalDate.parse(year + "-" + retrieveInfo());
        }

        System.out.print("Enter start time (HH[:mm]): ");
        LocalTime startTime = LocalTime.parse(retrieveInfo(), tf);

        System.out.print("Enter end time (HH[:mm]): ");
        LocalTime endTime = LocalTime.parse(retrieveInfo(), tf);

        TimeSlot slot = new TimeSlot(startTime, endTime);

        System.out.print("Enter the audience capacity expected: ");
        int capacity = Integer.parseInt(retrieveInfo());

        Event event;
        if (eventType == 0) event = new Concert(artist_title, date, slot, capacity);
        else event = new Play(artist_title, date, endDate, slot, capacity);

        System.out.println(event + " successfully added!");
        return event;
    }

    /**
     * Allows the user to create as many events as they want/need.
     * User should enter valid input, according to what is asked.
     *
     * @return The list of created events.
     */
    public static List<Event> retrieveAllEvents() {
        System.out.println("\n*** EVENT ADDER ***");
        System.out.print("\nWhat year do you want to schedule ? ");
        year = Integer.parseInt(AbstractUI.retrieveInfo());

        List<Event> eventList = new ArrayList<>();
        String c = "y";
        while (c.charAt(0) == 'y') {
            eventList.add(retrieveEvent());

            System.out.print("Do you want to add another event ? ('y' or 'n'): ");
            c = retrieveInfo();
        }

        return eventList;
    }

    /**
     * Asks the user to reschedule or cancel events that had to be removed.
     * User should enter valid input, according to what is asked.
     *
     * @param eventList This list of event to reschedule.
     * @return The list of rescheduled events.
     */
    public static List<Event> reloadEvents(final List<Event> eventList) {
        System.out.println("\n*** RESCHEDULE EVENTS ***");
        List<Event> newList = new ArrayList<>();

        for (Event e : eventList) {
            System.out.println(e);
            System.out.println("Do you want to reschedule or cancel this event ?");
            System.out.println("0. Reschedule");
            System.out.println("1. Cancel");
            System.out.print("Enter an option: ");

            if (Integer.parseInt(retrieveInfo()) == 1) continue;

            System.out.println("\nRescheduling " + e + "...");

            if (e.getClass() == Concert.class) System.out.print("Enter date (MM-dd): ");
            else System.out.print("Enter first date of representation (MM-dd): ");

            LocalDate date = LocalDate.parse(year + "-" + retrieveInfo());

            LocalDate endDate = null;
            if (e.getClass() == Play.class) {
                System.out.print("Enter last date of representation (MM-dd): ");
                endDate = LocalDate.parse(year + "-" + retrieveInfo());
            }

            System.out.print("Enter start time (HH[:mm]): ");
            LocalTime startTime = LocalTime.parse(retrieveInfo(), tf);

            System.out.print("Enter end time (HH[:mm]): ");
            LocalTime endTime = LocalTime.parse(retrieveInfo(), tf);

            TimeSlot slot = new TimeSlot(startTime, endTime);

            Event event;
            if (e.getClass() == Concert.class)
                event = new Concert(((Concert) e).getArtist(), date, slot, e.getCapacity());
            else event = new Play(((Play) e).getTitle(), date, endDate, slot, e.getCapacity());
            newList.add(event);
        }

        return newList;
    }


    /**
     * Asks the user to choose a new capacity for a given event or cancel it.
     *
     * @param event The event to edit.
     * @return The new capacity if given; -1 otherwise.
     */
    public static int tooLargeCapacity(final Event event) {
        System.out.println("\n*** TOO LARGE CAPACITY EVENT ***");
        System.out.println(event + " has a too large capacity, no venue can host it. Do you want to change capacity ? ('y' or 'n')");
        System.out.println("WARNING: If you do not change it, the event will be cancelled!");

        int capacity;
        if (retrieveInfo().charAt(0) == 'y') {
            System.out.print("Enter new capacity: ");
            capacity = Integer.parseInt(retrieveInfo());
        } else capacity = -1;

        return capacity;
    }
}
