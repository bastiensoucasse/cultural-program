package ui;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import domain.*;

/**
 * UI of the app.
 * 
 * @author Iantsa Provost
 * @author Bastien Soucasse
 */

public class TmpFilename {
    private List<Venue> venuList = new ArrayList<>();
    private List<Event> eventList = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public String retrieveInfo() {
        String info = null;
        try {
            info = scanner.nextLine();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return info;
            
    }

    public Event retrieveEvent() {
        System.out.println("*** Schedule an event *** ");

        System.out.println("What type of event do you want to schedule ?");
        System.out.println("0. Concert");
        System.out.println("1. Play");
        System.out.print("Enter an option: ");
        int eventType = Integer.parseInt(retrieveInfo());

        
        if (eventType == 0) // should not be -1
            System.out.print("Enter artist/group name: ");
        else
            System.out.print("Enter title: ");
        String artist_title = retrieveInfo();


        if (eventType == 0) // should not be -1
            System.out.print("Enter date (yyyy-mm-dd): ");
        else 
            System.out.print("Enter first date of representation (yyyy-mm-dd): ");

        LocalDate date = LocalDate.parse(retrieveInfo());

        LocalDate endDate = null;
        if (eventType == 1) {
            System.out.print("Enter last date of representation (yyyy-mm-dd): ");
            endDate = LocalDate.parse(retrieveInfo());
        }
        
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("H:mm[:ss]");
        System.out.print("Enter start time (HH:mm): ");
        LocalTime startTime = LocalTime.parse(retrieveInfo(), dtf);

        System.out.print("Enter end time (HH:mm): ");
        LocalTime endTime = LocalTime.parse(retrieveInfo(), dtf);

        TimeSlot slot = new TimeSlot(startTime, endTime);

        
        System.out.print("Enter the audience capacity expected: ");
        int capacity = Integer.parseInt(retrieveInfo());

        System.out.println("Event successfully added!");

        if (eventType == 0)
            return new Concert(artist_title, date, slot, capacity);
        return new Play(artist_title, date, endDate, slot, capacity);
    }
    
    public Venue retrieveVenue() {
        System.out.println("*** Schedule an event ***");

        System.out.print("Enter capacity: ");
        final int capacity = Integer.parseInt(retrieveInfo());

        for (DayOfWeek d : DayOfWeek.values()) {
            System.out.printf("Enter open time for %s: ", d.name().toLowerCase());


            System.out.printf("Enter open time for %s: ", d.name().toLowerCase());
        }

        return null;
  }
}
