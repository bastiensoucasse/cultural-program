package ui;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import domain.TimeSlot;
import domain.Venue;

public class VenueUI extends AbstractUI {

    public static Venue retrieveVenue() {
        System.out.println("*** Add a venue ***");

        System.out.print("Enter capacity: ");
        final int capacity = Integer.parseInt(retrieveInfo());

        final Map<DayOfWeek, TimeSlot> openingHours = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek d : DayOfWeek.values()) {
            System.out.printf("Enter open time for %s (HH:mm): ", d.name().toLowerCase());
            final LocalTime openTime = LocalTime.parse(retrieveInfo(), tf);

            System.out.printf("Enter close time for %s (HH:mm): ", d.name().toLowerCase());
            final LocalTime closeTime = LocalTime.parse(retrieveInfo(), tf);

            final TimeSlot slot = new TimeSlot(openTime, closeTime);
            openingHours.put(d, slot);
        }

        final Venue venue = new Venue(capacity, openingHours);
        
        System.out.println(venue + " successfully added!");
        return venue;
    }

    
    public static List<Venue> retrieveAllVenues() {
        System.out.println("*** VENUE INITIALIZER *** ");
        System.out.println("Reminder: There are 4 venues in JolieCité.");

        System.out.println("How do you want to initialize the venues ?");
        System.out.println("0. Default settings");
        System.out.println("1. Choose myself");
        System.out.print("Enter an option: ");
        int option = Integer.parseInt(retrieveInfo());

        List<Venue> venueList = new ArrayList<>();
        if (option == 1) {
            for (int i = 0; i < 4; i++)
                venueList.add(retrieveVenue());
        }

        return venueList;
    }
}
