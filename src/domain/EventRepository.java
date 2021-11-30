package domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public interface EventRepository {
    final Map<Date, Event> events = new HashMap<>();

    public boolean saveEvent(final Event event);

    public Event findEventById(final int eventId);
}
