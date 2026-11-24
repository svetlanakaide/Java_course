package org.example;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.zone.ZoneOffsetTransition;
import java.time.zone.ZoneRules;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MaintenanceScheduler {

    private final Clock clock;

    public MaintenanceScheduler(Clock clock) {
        this.clock = Objects.requireNonNull(clock, "clock не может быть нулл");
    }

    public MaintenanceWindow nextWindow(
            LocalDate date, LocalTime localStart, Duration duration,
            ZoneId zone, OverlapPolicy policy) {
        Objects.requireNonNull(date, "date не может быть нулл");
        Objects.requireNonNull(localStart, "localStart не может быть нулл");
        Objects.requireNonNull(duration, "duration не может быть нулл");
        Objects.requireNonNull(zone, "zone не может быть нулл");
        Objects.requireNonNull(policy, "policy не может быть нулл");

        if (duration.isZero() || duration.isNegative()) {
            throw new IllegalArgumentException("duration должна быть больше нуля");
        }

        ZoneRules rules = zone.getRules();
        ZoneOffsetTransition transition = rules.getTransition(
                java.time.LocalDateTime.of(date, localStart));
        if (transition != null && transition.isGap()) {
            throw new DateTimeException(
                    "Локальное время " + localStart + " " + date + " не существует в зоне " + zone + ": попадает в пропуск при переходе на летнее время");
        }

        ZonedDateTime zoned = java.time.LocalDateTime.of(date, localStart).atZone(zone);
        if (policy == OverlapPolicy.EARLIER) {
            zoned = zoned.withEarlierOffsetAtOverlap();
        } else {
            zoned = zoned.withLaterOffsetAtOverlap();
        }

        Instant start = zoned.toInstant();
        Instant end;
        try {
            end = start.plus(duration);
        } catch (java.time.DateTimeException | ArithmeticException e) {
            throw new IllegalArgumentException(
                    "Неверный duration", e);
        }
        return new MaintenanceWindow(start, end);
    }

    public List<MaintenanceWindow> upcomingWindows(
            int days, LocalTime localStart, Duration duration,
            ZoneId zone, OverlapPolicy policy) {
        if (days < 0) {
            throw new IllegalArgumentException("днй должно быть >= 0");
        }
        Objects.requireNonNull(localStart, "localStart не может быть нулл");
        Objects.requireNonNull(duration, "duration не может быть нулл");
        Objects.requireNonNull(zone, "zone не может быть нулл");
        Objects.requireNonNull(policy, "policy не может быть нулл");

        List<MaintenanceWindow> result = new ArrayList<>();
        LocalDate current = LocalDate.now(clock.withZone(zone));
        for (int i = 0; i < days; i++) {
            result.add(nextWindow(current, localStart, duration, zone, policy));
            current = current.plusDays(1);
        }
        return result;
    }
}