package org.example;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public final class MaintenanceWindow {

    private final Instant start;
    private final Instant end;

    public MaintenanceWindow(Instant start, Instant end) {
        this.start = Objects.requireNonNull(start, "start не можеть быть нулл");
        this.end = Objects.requireNonNull(end, "end не может быть нулл");
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("end не может быть до start");
        }
    }

    public Instant start() {
        return start;
    }

    public Instant end() {
        return end;
    }

    public Duration duration() {
        return Duration.between(start, end);
    }
}