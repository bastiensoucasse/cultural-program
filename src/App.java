import domain.*;
import infrastructure.ProgramRepositoryInMemory;

import java.time.LocalDate;
import java.time.LocalTime;

public class App {
    private static final int YEAR = 2022;

    public static void main(String[] args) {
        Event c1 = new Concert("Eminem", LocalDate.of(YEAR, 1, 1), new TimeSlot(LocalTime.of(21, 0), LocalTime.of(23, 00)), 1500);
        Event c2 = new Concert("Jack & Jack", LocalDate.of(YEAR, 1, 1), new TimeSlot(LocalTime.of(21, 0), LocalTime.of(23, 0)), 1000);
        Event p1 = new Play("High School Musical", LocalDate.of(YEAR, 1, 1), LocalDate.of(YEAR, 1, 5), new TimeSlot(LocalTime.of(19, 30), LocalTime.of(21, 30)), 800);
        Event c3 = new Concert("One Direction", LocalDate.of(YEAR, 1, 1), new TimeSlot(LocalTime.of(18, 0), LocalTime.of(21, 0)), 1500);

        Program week1 = new Program(1);
        week1.add(c1);
        week1.add(c2);
        week1.add(p1);
        week1.add(c3);

        ProgramRepository pr = new ProgramRepositoryInMemory();
        pr.saveProgram(week1);
        System.out.println(pr.findProgramById(1));
    }
}
