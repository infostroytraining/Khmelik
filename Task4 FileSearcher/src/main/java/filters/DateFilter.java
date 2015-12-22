package filters;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateFilter extends Filter {

    private LocalDateTime start;
    private LocalDateTime end;

    public DateFilter(Filter nextFilter, LocalDateTime start, LocalDateTime end) {
        super(nextFilter);
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean currentFilter(File file) {
        Instant instant = Instant.ofEpochMilli(file.lastModified());
        LocalDateTime lastModify = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return lastModify.isAfter(start) && lastModify.isBefore(end);
    }
}
