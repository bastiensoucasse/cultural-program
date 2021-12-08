import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;

import domain.Concert;
import domain.Event;
import domain.Play;
import domain.Program;
import domain.ProgramRepository;
import domain.TimeSlot;
import domain.Venue;
import infrastructure.ProgramRepositoryInFile;
import infrastructure.ProgramRepositoryInMemory;
import ui.AppUI;
import ui.EventUI;
import ui.VenueUI;

public class App {
    private static final ProgramRepository MEMORY_REPO = new ProgramRepositoryInMemory();
    private static final ProgramRepository FILE_REPO = new ProgramRepositoryInFile();
    private static List<Integer> weeks = new ArrayList<>();
    private static List<Venue> venueList;
    private static List<Event> eventList;

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
        AppUI.launch();
        venueList = VenueUI.retrieveAllVenues();
        if (venueList.isEmpty())
            venueList = defaultVenues();
        eventList = EventUI.retrieveAllEvents();

        for (Event event : eventList) {
            final int week = event.getDates().get(0).getDayOfYear() / 7 + 1;
            final Program program = weeks.contains(week) ? MEMORY_REPO.findProgramById(week) : new Program(week, venueList);
            if (!weeks.contains(week)) weeks.add(week);

            if (!program.add(event)) {
                // TODO: ça marche pas
            }

            final List<Event> removedEventList = program.getRemovedEvents();
            if (!removedEventList.isEmpty()) {
                AppUI.displayRemovedEvents(removedEventList);
                program.clearRemovedEvents();
            }

            MEMORY_REPO.saveProgram(program);
            FILE_REPO.saveProgram(program);
        }

        // for (int week : weeks) AppUI.displayProgram(MEMORY_REPO.findProgramById(week));
        // for (int week : weeks) AppUI.displayProgram(FILE_REPO.findProgramById(week));
        AppUI.quit();
    }
}

// events.add(new Concert("Eminem", LocalDate.of(YEAR, 1, 1),
// new TimeSlot(LocalTime.of(21, 0), LocalTime.of(23, 00)), 1500));
// events.add(new Concert("Jack & Jack", LocalDate.of(YEAR, 1, 1),
// new TimeSlot(LocalTime.of(21, 0), LocalTime.of(23, 0)), 1000));
// events.add(new Concert("One Direction", LocalDate.of(YEAR, 1, 1),
// new TimeSlot(LocalTime.of(18, 0), LocalTime.of(21, 0)), 1500));
// events.add(new Concert("René La Taupe", LocalDate.of(YEAR, 1, 1),
// new TimeSlot(LocalTime.of(18, 0), LocalTime.of(18, 10)), 12));
// events.add(new Concert("Jack & Jack", LocalDate.of(YEAR, 1, 2),
// new TimeSlot(LocalTime.of(21, 0), LocalTime.of(23, 0)), 1000));
// events.add(new Concert("One Direction", LocalDate.of(YEAR, 1, 3),
// new TimeSlot(LocalTime.of(18, 0), LocalTime.of(21, 0)), 1500));
// events.add(new Play("High School Musical", LocalDate.of(YEAR, 1, 1),
// LocalDate.of(YEAR, 1, 5),
// new TimeSlot(LocalTime.of(19, 30), LocalTime.of(21, 30)), 800));
// events.add(new Concert("Eminem", LocalDate.of(YEAR, 1, 2),
// new TimeSlot(LocalTime.of(21, 0), LocalTime.of(23, 00)), 1500));
