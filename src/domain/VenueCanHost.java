package domain;

public class VenueCanHost {
    public boolean canHost;
    public Event removedEvent = null;

    public VenueCanHost(boolean canHost, Event removedEvent) {
        this.canHost = canHost;
        this.removedEvent = removedEvent;
    }

    public VenueCanHost(boolean canHost) {
        this.canHost = canHost;
    }
}
