package org.example;
import java.time.Instant;
import java.time.format.DateTimeParseException;

public final class IncidentParser {

    private IncidentParser() {}

    public static Incident parse(String line) {
        if (line == null) {
            throw new IllegalArgumentException("line must not be null");
        }
        String[] parts = line.split(";", -1);
        if (parts.length != 4) {
            throw new IllegalArgumentException(
                    "Expected 4 fields separated by ';', got " + parts.length);
        }
        String id = parts[0];
        String service = parts[1];
        String startedRaw = parts[2];
        String resolvedRaw = parts[3];

        if (id.isBlank()) {
            throw new IllegalArgumentException("id не может быть пустым");
        }
        if (service.isBlank()) {
            throw new IllegalArgumentException("service не может быть пустым");
        }
        if (startedRaw.isBlank()) {
            throw new IllegalArgumentException("startedAt не может быть пустым");
        }
        if (resolvedRaw.isBlank()) {
            throw new IllegalArgumentException("resolvedAt не может быть пустым");
        }

        Instant startedAt;
        Instant resolvedAt;
        try {
            startedAt = Instant.parse(startedRaw);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Неверный startedAt Instant: " + startedRaw, e);
        }
        try {
            resolvedAt = Instant.parse(resolvedRaw);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Неверный resolvedAt Instant: " + resolvedRaw, e);
        }

        return new Incident(id, service, startedAt, resolvedAt);
    }
}