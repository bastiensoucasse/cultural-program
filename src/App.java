import domain.*;
import infrastructure.ProgramRepositoryInMemory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import ui.TmpFilename;

public class App {
    private static final int YEAR = 2022;

    public static void main(String[] args) {
        final List<Venue> venues = new ArrayList<>();
        final List<Event> events = new ArrayList<>();

        // Creates 4 default venues (will be overwritten by UI)
        for (int i = 0; i < 4; i++) {
            Map<DayOfWeek, TimeSlot> slots = new EnumMap<>(DayOfWeek.class);
            for (DayOfWeek day : DayOfWeek.values())
                slots.put(day, new TimeSlot(LocalTime.of(18, 0), LocalTime.of(23, 0)));
            venues.add(new Venue(1500, slots));
        }

        // events.add(new Concert("Eminem", LocalDate.of(YEAR, 1, 1),
        //         new TimeSlot(LocalTime.of(21, 0), LocalTime.of(23, 00)), 1500));
        // events.add(new Concert("Jack & Jack", LocalDate.of(YEAR, 1, 1),
        //         new TimeSlot(LocalTime.of(21, 0), LocalTime.of(23, 0)), 1000));
        // events.add(new Concert("One Direction", LocalDate.of(YEAR, 1, 1),
        //         new TimeSlot(LocalTime.of(18, 0), LocalTime.of(21, 0)), 1500));
        // events.add(new Concert("René La Taupe", LocalDate.of(YEAR, 1, 1),
        //         new TimeSlot(LocalTime.of(18, 0), LocalTime.of(18, 10)), 12));
        // events.add(new Concert("Jack & Jack", LocalDate.of(YEAR, 1, 2),
        //         new TimeSlot(LocalTime.of(21, 0), LocalTime.of(23, 0)), 1000));
        // events.add(new Concert("One Direction", LocalDate.of(YEAR, 1, 3),
        //         new TimeSlot(LocalTime.of(18, 0), LocalTime.of(21, 0)), 1500));
        // events.add(new Play("High School Musical", LocalDate.of(YEAR, 1, 1), LocalDate.of(YEAR, 1, 5),
        //         new TimeSlot(LocalTime.of(19, 30), LocalTime.of(21, 30)), 800));
        // events.add(new Concert("Eminem", LocalDate.of(YEAR, 1, 2),
        //         new TimeSlot(LocalTime.of(21, 0), LocalTime.of(23, 00)), 1500));

        // Program week1 = new Program(1, venues);
        // for (Event e : events)
        //     week1.add(e);

        // ProgramRepository pr = new ProgramRepositoryInMemory();
        // pr.saveProgram(week1);
        // System.out.println(pr.findProgramById(1));

        TmpFilename tmpfn = new TmpFilename();
        events.add(tmpfn.retrieveEvent());


        
        Program week1 = new Program(1, venues);
        for (Event e : events)
            week1.add(e);

        ProgramRepository pr = new ProgramRepositoryInMemory();
        pr.saveProgram(week1);
        System.out.println(pr.findProgramById(1));
    }
}
