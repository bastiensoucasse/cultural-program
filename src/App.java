import domain.*;
import infrastructure.ProgramRepositoryInMemory;

import java.time.LocalDate;
import java.time.LocalTime;

public class App {
    private static final int YEAR = 2022;

    public static void main(String[] args) {
        Event c1 = new Concert("Eminem", LocalDate.of(YEAR, 1, 1), new TimeSlot(LocalTime.of(20, 0), LocalTime.of(22, 30)), 2000);
        Event c2 = new Concert("Jack & Jack", LocalDate.of(YEAR, 1, 3), new TimeSlot(LocalTime.of(21, 0), LocalTime.of(23, 0)), 1000);
        Event p1 = new Play("High School Musical", LocalDate.of(YEAR, 1, 2), LocalDate.of(YEAR, 1, 5), new TimeSlot(LocalTime.of(19, 30), LocalTime.of(21, 0)), 800);

        Program week1 = new Program(1);
        week1.add(c1);
        week1.add(c2);
        week1.add(p1);

        ProgramRepository pr = new ProgramRepositoryInMemory();
        pr.saveProgram(week1);
        System.out.println(pr.findProgramById(1));
    }
}
