package ui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import domain.Concert;
import domain.Event;
import domain.Play;
import domain.TimeSlot;

public class EventUI extends AbstractUI {
    
    public static Event retrieveEvent() {
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

        LocalDate date = LocalDate.parse(YEAR + "-" + retrieveInfo());

        LocalDate endDate = null;
        if (eventType == 1) {
            System.out.print("Enter last date of representation (yyyy-mm-dd): ");
            endDate = LocalDate.parse(retrieveInfo());
        }
        
        
        System.out.print("Enter start time (HH:mm): ");
        LocalTime startTime = LocalTime.parse(retrieveInfo(), tf);

        System.out.print("Enter end time (HH:mm): ");
        LocalTime endTime = LocalTime.parse(retrieveInfo(), tf);

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


    public static List<Event> retrieveAllEvents() {
        System.out.println("*** EVENT ADDER ***");
        // System.out.println("Enter number of events: ");
        // int n = Integer.parseInt(retrieveInfo());

        // List<Event> eventList = new ArrayList<>();
        // for (int i = 0; i < n; i++)
        //     eventList.add(retrieveEvent());

        List<Event> eventList = new ArrayList<>();
        String c = "y";
        while (c.charAt(0) == 'y') {
            eventList.add(retrieveEvent());

            System.out.print("Do you want to add another event ? ('y' or 'n'): ");
            c = retrieveInfo();
        }
        
        return eventList;
    }
}
