package ui;

import java.util.List;

import domain.Event;
import domain.Program;

public class AppUI {
    public static void launch() {
        System.out.println("*** WELCOME TO BISH (stands for Bastien & Iantsa's Scheduling Helper) ***");
    }

    public static void quit() {
        System.out.println("Thank you for using our SH!");
        System.out.println("Find your programs in the files \"Week[week_number].json\"");
        System.out.println("See you soon *smack* <3");
    }
    
    public static void displayProgram(Program program) {
        System.out.println(program);
    }

    public static void displayRemovedEvents(List<Event> eventList) {
        System.out.println("The following events had to be removed: ");
        for (Event e : eventList)
            System.out.println(e);
        System.out.println("Do not forget to add them again!");
    }
}
