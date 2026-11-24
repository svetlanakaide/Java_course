package org.example;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public final class IncidentAnalytics {

    private IncidentAnalytics() {
    }

    public static Duration totalDowntime(List<Incident> incidents) {
        if (incidents == null) {
            throw new IllegalArgumentException("incidents не может быть нулл");
        }
        long totalNanos = 0L;
        for (Incident incident : incidents) {
            totalNanos = Math.addExact(totalNanos,
                    incident.duration().toNanos());
        }
        return Duration.ofNanos(totalNanos);
    }

    public static Duration averageDowntime(List<Incident> incidents) {
        if (incidents == null) {
            throw new IllegalArgumentException("incidents не может быть нулл");
        }
        if (incidents.isEmpty()) {
            return Duration.ZERO;
        }
        BigInteger totalNanos = BigInteger.ZERO;
        for (Incident incident : incidents) {
            totalNanos = totalNanos.add(BigInteger.valueOf(incident.duration().toNanos()));
        }
        BigInteger average = totalNanos.divide(
                BigInteger.valueOf(incidents.size()));
        return Duration.ofNanos(average.longValueExact());
    }

    public static Optional<Incident> longestIncident(
            List<Incident> incidents) {
        if (incidents == null) {
            throw new IllegalArgumentException("incidents не может быть нулл");
        }
        if (incidents.isEmpty()) {
            return Optional.empty();
        }
        Comparator<Incident> comparator =
                Comparator.comparing((Incident i) -> i.duration().toNanos())
                        .thenComparing(Incident::startedAt, Comparator.reverseOrder())
                        .thenComparing(Incident::id, Comparator.reverseOrder());
        return incidents.stream().max(comparator);
    }

    public static Map<LocalDate, List<Incident>> groupByStartDate(List<Incident> incidents, ZoneId zone) {
        if (incidents == null) {
            throw new IllegalArgumentException("incidents не может быть нулл");
        }
        if (zone == null) {
            throw new IllegalArgumentException("zone не может быть нулл");
        }
        Map<LocalDate, List<Incident>> result = new TreeMap<>();
        for (Incident incident : incidents) {
            LocalDate date = incident.startedAt().atZone(zone).toLocalDate();
            result.computeIfAbsent(date, d -> new ArrayList<>()).add(incident);
        }
        Comparator<Incident> order =
                Comparator.comparing(Incident::startedAt).thenComparing(Incident::id);
        for (List<Incident> group : result.values()) {
            group.sort(order);
        }
        return result;
    }
}