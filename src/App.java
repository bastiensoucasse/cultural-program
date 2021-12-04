import java.time.LocalDate;
import java.time.LocalTime;

import domain.*;
import infrastructure.ProgramRepositoryInMemory;

public class App {
    private static int year = 2022;

    public static void main(String[] args) throws Exception {
        Event c1 = new Concert("Eminem", LocalDate.of(year, 1, 1), new TimeSlot(LocalTime.of(20, 0), LocalTime.of(22, 30)), 2000);
        Event c2 = new Concert("Jack & Jack", LocalDate.of(year, 1, 3), new TimeSlot(LocalTime.of(21, 0), LocalTime.of(23, 0)), 1000);
        Event p1 = new Play("High School Musical", LocalDate.of(year, 1, 2), LocalDate.of(year, 1, 5), new TimeSlot(LocalTime.of(19, 30), LocalTime.of(21, 0)), 800);

        Program program = new Program(1);
        program.add(c1);
        program.add(c2);
        program.add(p1);
        System.out.println(program);

        ProgramRepository week1 = new ProgramRepositoryInMemory();
        week1.saveProgram(program);

        System.out.println(week1.findProgramById(1));
    }
}
