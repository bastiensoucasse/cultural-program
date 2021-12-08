package ui;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import domain.*;

/**
 * UI of the app.
 * 
 * @author Iantsa Provost
 * @author Bastien Soucasse
 */

public class TmpFilename {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("H:mm[:ss]");
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
    
    public Venue retrieveVenue() {
        System.out.println("*** Add a venue ***");

        System.out.print("Enter capacity: ");
        final int capacity = Integer.parseInt(retrieveInfo());

        final Map<DayOfWeek, TimeSlot> openingHours = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek d : DayOfWeek.values()) {
            System.out.printf("Enter open time for %s (HH:mm): ", d.name().toLowerCase());
            final LocalTime openTime = LocalTime.parse(retrieveInfo(), dtf);

            System.out.printf("Enter close time for %s (HH:mm): ", d.name().toLowerCase());
            final LocalTime closeTime = LocalTime.parse(retrieveInfo(), dtf);

            final TimeSlot slot = new TimeSlot(openTime, closeTime);
            openingHours.put(d, slot);
        }

        final Venue venue = new Venue(capacity, openingHours);
        
        System.out.println(venue + " successfully added!");
        return venue;
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
        
        
        System.out.print("Enter start time (HH:mm): ");
        LocalTime startTime = LocalTime.parse(retrieveInfo(), dtf);

        System.out.print("Enter end time (HH:mm): ");
        LocalTime endTime = LocalTime.parse(retrieveInfo(), dtf);

        TimeSlot slot = new TimeSlot(startTime, endTime);

        
        System.out.print("Enter the audience capacity expected: ");
        int capacity = Integer.parseInt(retrieveInfo());

        Event event;
        if (eventType == 0)
            event = new Concert(artist_title, date, slot, capacity);
        else 
            event = new Play(artist_title, date, endDate, slot, capacity);

        System.out.println(event + " successfully added!");
        return event;
    }

    public void launch() {
        System.out.println("*** WELCOME TO BISH (stands for Bastien & Iantsa's Scheduling Helper) ***");
    }


    public List<Venue> venueInitializer() {
        System.out.println("*** VENUE INITIALIZER *** ");
        System.out.println("Reminder: There are 4 venues in JolieCité.");

        System.out.println("How do you want to initialize the venues ?");
        System.out.println("0. Default settings");
        System.out.println("1. Choose myself");
        System.out.println("Enter an option: ");
        int option = Integer.parseInt(retrieveInfo());

        List<Venue> venueList = new ArrayList<>();
        if (option == 0) {
            for (int i = 0; i < 4; i++)
                venueList.add(retrieveVenue());
        }

        return venueList;
    }    

    
    public List<Event> eventAdder() {
        System.out.println("*** EVENT ADDER ***");
        System.out.println("Enter number of events: ");
        int n = Integer.parseInt(retrieveInfo());

        List<Event> eventList = new ArrayList<>();
        for (int i = 0; i < n; i++)
            eventList.add(retrieveEvent());

        return eventList;
    }
    
}
