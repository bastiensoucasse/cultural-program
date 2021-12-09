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

    /**
     * 1. NO
     * 2. OK
     * 3. NON
     * 4. OK
     * <p>
     * 5. NON
     * <p>
     * 6. OK
     * <p>
     * 7. NON
     * 8. OK
     * 9. NON
     *
     * @param index
     * @return
     */
    private static List<Event> defaultEvents(int index) {
        TimeSlot slot = new TimeSlot(LocalTime.of(20, 0), LocalTime.of(22, 0));
        int capacity = 1000;
        List<List<Event>> list = new ArrayList<>(List.of(new ArrayList<>(List.of( // OK
                        new Concert("1Da", LocalDate.of(2022, 1, 4), slot, capacity), new Concert("1Db", LocalDate.of(2022, 1, 6), slot, capacity), new Play("1D The Musical", LocalDate.of(2022, 1, 3), LocalDate.of(2022, 1, 6), slot, capacity))), new ArrayList<>(List.of( // OK
                        new Concert("1Da", LocalDate.of(2022, 1, 4), slot, capacity), new Concert("1Db", LocalDate.of(2022, 1, 6), slot, capacity), new Concert("1Dc", LocalDate.of(2022, 1, 7), slot, capacity), new Play("1D The Musical", LocalDate.of(2022, 1, 3), LocalDate.of(2022, 1, 6), slot, capacity))), new ArrayList<>(List.of( // OK
                        new Concert("1Da", LocalDate.of(2022, 1, 4), slot, capacity), new Play("Eminem The Musical", LocalDate.of(2022, 1, 6), LocalDate.of(2022, 1, 7), slot, capacity), new Concert("1Db", LocalDate.of(2022, 1, 8), slot, capacity), new Play("1D The Musical", LocalDate.of(2022, 1, 4), LocalDate.of(2022, 1, 6), slot, capacity))), new ArrayList<>(List.of( // OK
                        new Play("1D The Musical", LocalDate.of(2022, 1, 3), LocalDate.of(2022, 1, 5), slot, capacity), new Concert("1D", LocalDate.of(2022, 1, 4), slot, capacity))),


                new ArrayList<>(List.of( // OK
                        // Pre scheduled
                        new Play("Eminem The Musical", LocalDate.of(2022, 1, 8), LocalDate.of(2022, 1, 9), slot, capacity),

                        // Trying to add that (just one of them)
                        // new Concert("1D", LocalDate.of(2022, 1, 8), slot, capacity)
                        // new Play("1D The Musical", LocalDate.of(2022, 1, 7), LocalDate.of(2022, 1, 8), slot, capacity)
                        new Play("1D The Musical", LocalDate.of(2022, 1, 6), LocalDate.of(2022, 1, 8), slot, capacity))),


                new ArrayList<>(List.of( // OK
                        // Pre scheduled (just one of them)
                        // new Concert("1D", LocalDate.of(2022, 1, 8), slot, capacity),
                        // new Concert("1D", LocalDate.of(2022, 1, 9), slot, capacity),
                        // new Play("Eminem The Musical", LocalDate.of(2022, 1, 7), LocalDate.of(2022, 1, 8), slot, capacity),
                        new Play("Eminem The Musical", LocalDate.of(2022, 1, 6), LocalDate.of(2022, 1, 8), slot, capacity),

                        // Trying to add that (just one of them)
                        new Play("1D The Musical", LocalDate.of(2022, 1, 7), LocalDate.of(2022, 1, 9), slot, capacity)
                        // new Play("1D The Musical", LocalDate.of(2022, 1, 8), LocalDate.of(2022, 1, 9), slot, capacity)
                )),


                new ArrayList<>(List.of( // OK
                        // Pre scheduled
                        new Concert("1Da", LocalDate.of(2022, 1, 8), slot, capacity), new Concert("1Db", LocalDate.of(2022, 1, 9), slot, capacity),

                        // Trying to add that
                        // could start sooner, should be the same result
                        new Play("1D The Musical", LocalDate.of(2022, 1, 8), LocalDate.of(2022, 1, 9), slot, capacity))), new ArrayList<>(List.of( // OK
                        // Pre scheduled
                        new Concert("1Da", LocalDate.of(2022, 1, 3), slot, capacity), new Concert("1Db", LocalDate.of(2022, 1, 8), slot, capacity), new Concert("1Dc", LocalDate.of(2022, 1, 9), slot, capacity),

                        // Trying to add that
                        new Play("1D The Musical", LocalDate.of(2022, 1, 7), LocalDate.of(2022, 1, 9), slot, capacity))), new ArrayList<>(List.of( // OK
                        // Pre scheduled
                        new Concert("1Da", LocalDate.of(2022, 1, 3), slot, capacity), new Concert("1Db", LocalDate.of(2022, 1, 8), slot, capacity), new Concert("1Dc", LocalDate.of(2022, 1, 9), slot, capacity), new Play("Eminem The Musical", LocalDate.of(2022, 1, 6), LocalDate.of(2022, 1, 7), slot, capacity),

                        // Trying to add that
                        new Play("1D The Musical", LocalDate.of(2022, 1, 7), LocalDate.of(2022, 1, 9), slot, capacity))), new ArrayList<>(List.of(
                        // Pre scheduled
                        new Play("1D The Musical 1", LocalDate.of(2022, 1, 3), LocalDate.of(2022, 1, 9), slot, capacity), new Play("1D The Musical 2", LocalDate.of(2022, 1, 3), LocalDate.of(2022, 1, 9), slot, capacity), new Play("1D The Musical 3", LocalDate.of(2022, 1, 3), LocalDate.of(2022, 1, 9), slot, capacity),

                        // Trying to add that
                        new Concert("1D", LocalDate.of(2022, 1, 3), slot, capacity))), new ArrayList<>(List.of(
                        // Pre scheduled
                        new Concert("1Da", LocalDate.of(2022, 1, 3), slot, capacity), // 1
                        new Concert("1Db1", LocalDate.of(2022, 1, 4), slot, capacity), // 1
                        new Concert("1Db2", LocalDate.of(2022, 1, 4), slot, capacity), // 2
                        new Concert("1Dc", LocalDate.of(2022, 1, 5), slot, capacity), // 1

                        // Trying to add that
                        new Play("1D The Musical", LocalDate.of(2022, 1, 3), LocalDate.of(2022, 1, 6), slot, capacity)))));
        return list.get(index);
    }

    public static void main(String[] args) {
        // Launch and initialize the venues and events
        AppUI.launch();
        List<Venue> venueList = VenueUI.retrieveAllVenues();
        if (venueList.isEmpty()) venueList = defaultVenues();
        List<Event> eventList = EventUI.retrieveAllEvents();
        // List<Event> eventList = defaultEvents(9); // Test

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
                    final int week = d.get(WeekFields.ISO.weekOfWeekBasedYear());
                    if (!datesPerWeek.containsKey(week)) datesPerWeek.put(week, new ArrayList<>(List.of(d)));
                    else datesPerWeek.get(week).add(d);
                }
                if (datesPerWeek.size() != 1) {
                    for (Map.Entry<Integer, List<LocalDate>> entry : datesPerWeek.entrySet()) {
                        Event e;
                        if (event instanceof Concert)
                            e = new Concert(((Concert) event).getArtist(), event.getDates().get(0), event.getSlot(), event.getCapacity());
                        else if (event instanceof Play)
                            e = new Play(((Play) event).getTitle(), event.getDates().get(0), event.getDates().get(event.getDates().size() - 1), event.getSlot(), event.getCapacity());
                        else continue;
                        e.setDates(entry.getValue());
                        newEvents.add(e);
                    }
                    continue;
                }

                final int week = event.getDates().get(0).get(WeekFields.ISO.weekOfWeekBasedYear());
                final Program program = weekList.contains(week) ? MEMORY_REPO.findProgramById(week) : new Program(week, venueList);
                if (!weekList.contains(week)) weekList.add(week);

                final List<LocalDate> weekDates = event.getDates();
                if (weekDates.removeIf(d -> d.get(WeekFields.ISO.weekOfWeekBasedYear()) != week)) {
                    if (weekDates.isEmpty()) continue;
                    event.setDates(weekDates);
                }

                boolean ok = program.add(event);
                if (ok) AppUI.displayProgram(program);
                else newEvents.addAll(EventUI.reloadEvents(new ArrayList<>(List.of(event))));

                final List<Event> removedEventList = program.getRemovedEvents();
                if (!removedEventList.isEmpty()) {
                    AppUI.displayRemovedEvents(removedEventList);
                    newEvents.addAll(EventUI.reloadEvents(removedEventList));
                    program.clearRemovedEventList();
                }

                MEMORY_REPO.saveProgram(program);
                // FILE_REPO.saveProgram(program);
            }

            eventList.clear();
            eventList.addAll(newEvents);
            newEvents.clear();
        }

        // Display the programs and quit
        final List<Program> programs = new ArrayList<>(weekList.size());
        for (final int week : weekList) programs.add(MEMORY_REPO.findProgramById(week));
        AppUI.recap(programs);
        AppUI.quit();
    }
}
