package domain;

import java.util.ArrayList;
import java.util.List;

public class Venue {
    private static int numVenues = 0;

    private final int id;
    private final int capacity;
    private final List<OpeningHours> openingHours;
    private final List<Event> events = new ArrayList<>();

    public Venue(final int capacity, final List<OpeningHours> openingHours) {
        this.id = numVenues;
        this.capacity = capacity;
        this.openingHours = openingHours;
        numVenues++;
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }
}
