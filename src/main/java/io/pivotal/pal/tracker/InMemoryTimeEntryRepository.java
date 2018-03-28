package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    Map<Long, TimeEntry> timeEntryMap;

    public InMemoryTimeEntryRepository() {
        timeEntryMap = new HashMap<>();
    }

    public TimeEntry create(TimeEntry timeEntry) {
        long incrementedId = timeEntryMap.size() + 1;
        timeEntry.setId(incrementedId);
        timeEntryMap.put(incrementedId, timeEntry);
        return timeEntry;
    }

    public TimeEntry find(long id) {
        return timeEntryMap.get(id);
    }

    public List<TimeEntry> list() {
        List<TimeEntry> entries = new ArrayList<TimeEntry>(timeEntryMap.values());
        return entries;
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        timeEntryMap.put(id, timeEntry);
        timeEntry.setId(id);
        return timeEntry;
    }

    public TimeEntry delete(long id) {
        TimeEntry entry = timeEntryMap.remove(id);
        return entry;
    }
}
