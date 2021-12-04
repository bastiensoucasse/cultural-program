package domain;

public class Venue {
    private final int capacity;
    private final TimeSlot[] slots;

    public Venue(final int capacity, final TimeSlot[] slots) {
        this.capacity = capacity;
        this.slots = slots;
    }

    public int getCapacity() {
        return capacity;
    }

    public TimeSlot getSlot(final int day) {
        return slots[day];
    }
}
