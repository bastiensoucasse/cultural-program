import domain.*;
import infrastructure.ProgramRepositoryInFile;
import infrastructure.ProgramRepositoryInMemory;
import ui.AppUI;
import ui.EventUI;
import ui.VenueUI;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.*;

/**
 * Client of the application.
 *
 * @author Bastien Soucasse
 * @author Iantsa Provost
 */
public class App {
    private static final ProgramRepository MEMORY_REPO = new ProgramRepositoryInMemory();
    private static final ProgramRepository FILE_REPO = new ProgramRepositoryInFile();

    private static final List<Integer> weekList = new ArrayList<>();

    private static List<Venue> defaultVenues() {
        final List<Venue> venues = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            Map<DayOfWeek, TimeSlot> slots = new EnumMap<>(DayOfWeek.class);
            for (DayOfWeek day : DayOfWeek.values())
                slots.put(day, new TimeSlot(LocalTime.of(18, 0), LocalTime.of(23, 0)));
            venues.add(new Venue(1500, slots));
        }

        return venues;
    }

    public static void main(String[] args) {
        // Launch and initialize the venues and events
        AppUI.launch();
        List<Venue> venueList = VenueUI.retrieveAllVenues();
        if (venueList.isEmpty()) venueList = defaultVenues();
        List<Event> eventList = EventUI.retrieveAllEvents();
        List<Event> newEvents = new ArrayList<>();

        // Try to add each event
        while (!eventList.isEmpty()) {
            for (Event event : eventList) {
                // If no venue can host the event, try to change or remove
                boolean canBeHosted = false;
                while (!canBeHosted) {
                    for (final Venue v : venueList) if (v.canHost(event.getCapacity())) canBeHosted = true;
                    if (!canBeHosted) {
                        final int capacity = EventUI.tooLargeCapacity(event);
                        if (capacity < 0) break;
                        else event.setCapacity(capacity);
                    }
                }
                if (!canBeHosted) continue;

                // Split the event into one for each week it is in if necessary
                final Map<Integer, List<LocalDate>> datesPerWeek = new HashMap<>();
                for (final LocalDate d : event.getDates()) {
                    final int week = d.get(WeekFields.SUNDAY_START.weekOfWeekBasedYear());
                    if (!datesPerWeek.containsKey(week)) datesPerWeek.put(week, new ArrayList<>(List.of(d)));
                    else datesPerWeek.get(week).add(d);
                }
                if (datesPerWeek.size() != 1) {
                    // System.out.println("[DEBUG] Splitting " + event + ": ");
                    for (Map.Entry<Integer, List<LocalDate>> entry : datesPerWeek.entrySet()) {
                        Event e;
                        if (event instanceof Concert)
                            e = new Concert(((Concert) event).getArtist(), event.getDates().get(0), event.getSlot(), event.getCapacity());
                        else if (event instanceof Play)
                            e = new Play(((Play) event).getTitle(), event.getDates().get(0), event.getDates().get(event.getDates().size() - 1), event.getSlot(), event.getCapacity());
                        else continue;
                        e.setDates(entry.getValue());
                        // System.out.println("[DEBUG]       - " + e);
                        newEvents.add(e);
                    }
                    continue;
                }

                final int week = event.getDates().get(0).get(WeekFields.SUNDAY_START.weekOfWeekBasedYear());
                final Program program = weekList.contains(week) ? MEMORY_REPO.findProgramById(week) : new Program(week, venueList);
                if (!weekList.contains(week)) weekList.add(week);

                final List<LocalDate> weekDates = event.getDates();
                if (weekDates.removeIf(d -> d.get(WeekFields.SUNDAY_START.weekOfWeekBasedYear()) != week)) {
                    if (weekDates.isEmpty()) continue;
                    event.setDates(weekDates);
                }
                // System.out.println("[DEBUG] " + event);
                if (!program.add(event)) newEvents.addAll(EventUI.reloadEvents(new ArrayList<>(List.of(event))));

                final List<Event> removedEventList = program.getRemovedEvents();
                if (!removedEventList.isEmpty()) {
                    AppUI.displayRemovedEvents(removedEventList);
                    newEvents.addAll(EventUI.reloadEvents(removedEventList));
                    program.clearRemovedEvents();
                }

                MEMORY_REPO.saveProgram(program);
                // FILE_REPO.saveProgram(program);
            }

            eventList.clear();
            eventList.addAll(newEvents);
            newEvents.clear();
        }

        // Display the programs and quit
        for (final int week : weekList) AppUI.displayProgram(MEMORY_REPO.findProgramById(week));
        AppUI.quit();
    }
}
