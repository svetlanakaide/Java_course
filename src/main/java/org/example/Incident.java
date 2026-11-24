package org.example;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public record Incident(
        String id,
        String service,
        Instant startedAt,
        Instant resolvedAt) {

    public Incident {
        Objects.requireNonNull(id, "id не нулл");
        Objects.requireNonNull(service, "service не нулл");
        Objects.requireNonNull(startedAt, "startedAt не нулл");
        Objects.requireNonNull(resolvedAt, "resolvedAt не нулл");
        if (id.isBlank()) {
            throw new IllegalArgumentException("id не пустой");
        }
        if (service.isBlank()) {
            throw new IllegalArgumentException("service не пустой");
        }
        if (resolvedAt.isBefore(startedAt)) {
            throw new IllegalArgumentException("resolvedAt не может быть до startedAt");
        }
    }

    public Duration duration() {
        return Duration.between(startedAt, resolvedAt);
    }
}