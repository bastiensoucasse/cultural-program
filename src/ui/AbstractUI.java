package ui;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Element of the UI.
 *
 * @author Bastien Soucasse
 * @author Iantsa Provost
 */
public class AbstractUI {
    protected static final DateTimeFormatter tf = DateTimeFormatter.ofPattern("H[:mm]");
    protected static Scanner scanner = new Scanner(System.in);

    /**
     * Retrieves information entered by user, as a string.
     *
     * @return The input string.
     */
    protected static String retrieveInfo() {
        String info = null;

        try {
            info = scanner.nextLine();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return info;
    }
}
