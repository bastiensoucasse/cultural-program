package ui;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class AbstractUI {
    protected static final int YEAR = 2022;

    protected static final DateTimeFormatter tf = DateTimeFormatter.ofPattern("H:mm");
    protected static Scanner scanner = new Scanner(System.in);

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
