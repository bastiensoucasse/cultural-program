import domain.*;
import infrastructure.ProgramRepositoryInFile;
import infrastructure.ProgramRepositoryInMemory;
import ui.AppUI;
import ui.EventUI;
import ui.VenueUI;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
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
        List<Event> newEventList = new ArrayList<>();

        while (!eventList.isEmpty()) {
            // Try to add each event
            for (final Event event : eventList) {
                // Get all the weeks the event is in
                final List<Integer> eventWeekList = new ArrayList<>();
                for (final LocalDate d : event.getDates()) {
                    final int week = d.getDayOfYear() / 7 + 1;
                    if (!eventWeekList.contains(week)) eventWeekList.add(week);
                }

                // Add the event to all concerned weekly programs
                for (final int week : eventWeekList) {
                    final Program program = weekList.contains(week) ? MEMORY_REPO.findProgramById(week) : new Program(week, venueList);
                    if (!weekList.contains(week)) weekList.add(week);

                    // event.getDates().removeIf(d -> (d.getDayOfYear() / 7) != week);
                    if (!program.add(event)) newEventList.addAll(EventUI.reloadEvents(new ArrayList<>(List.of(event))));

                    final List<Event> removedEventList = program.getRemovedEvents();
                    if (!removedEventList.isEmpty()) {
                        AppUI.displayRemovedEvents(removedEventList);
                        newEventList.addAll(EventUI.reloadEvents(removedEventList));
                        program.clearRemovedEvents();
                    }

                    MEMORY_REPO.saveProgram(program);
                    // FILE_REPO.saveProgram(program);
                }
            }

            eventList = newEventList;
            newEventList.clear();
        }

        // Display the programs and quit
        for (final int week : weekList) AppUI.displayProgram(MEMORY_REPO.findProgramById(week));
        AppUI.quit();
    }
}
