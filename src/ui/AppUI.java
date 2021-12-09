package ui;

import domain.Event;
import domain.Program;

import java.util.List;

/**
 * UI application.
 *
 * @author Bastien Soucasse
 * @author Iantsa Provost
 */
public class AppUI {
    public static void launch() {
        System.out.println("\n*** WELCOME TO BISH (stands for Bastien & Iantsa's Scheduling Helper) ***");
    }

    public static void quit() {
        System.out.println("\nThank you for using our SH!");
        // System.out.println("Find your programs in the files \"Week[week_number].json\"");
    }

    public static void displayProgram(final Program program) {
        System.out.println(program);
    }

    public static void displayRemovedEvents(final List<Event> eventList) {
        System.out.println("\nThe following events had to be removed: ");
        for (final Event e : eventList) System.out.println(e);
    }
}
