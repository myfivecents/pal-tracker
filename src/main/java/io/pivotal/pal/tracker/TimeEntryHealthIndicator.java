package io.pivotal.pal.tracker;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class TimeEntryHealthIndicator implements HealthIndicator {
    private TimeEntryRepository timeEntryRepository;
    private final int MAX_TIME_ENTRIES = 5;

    public TimeEntryHealthIndicator (TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }
    @Override
    public Health health() {
        int size = timeEntryRepository.list().size();
        Health.Builder builder = new Health.Builder();
        if(size < MAX_TIME_ENTRIES) {
            // up
            builder.up();
        } else {
            // down
            builder.down();
        }
        return builder.build();
    }
}
