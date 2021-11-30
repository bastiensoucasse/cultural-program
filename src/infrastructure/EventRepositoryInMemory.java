package infrastructure;

import domain.Event;
import domain.EventRepository;

public class EventRepositoryInMemory implements EventRepository {
    // @Override
    public boolean saveEvent(final Event event) {
        return false;
    }

    // @Override
    public Event findEventById(final int eventId) {
        return null;
    }
}
