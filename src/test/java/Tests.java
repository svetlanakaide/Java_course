import org.example.*;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaTimeApiHomeworkTest {

    private static final ZoneId BERLIN = ZoneId.of("Europe/Berlin");

    @Test
    void parseValidLineAndDurationIs150Minutes() {
        Incident incident = IncidentParser.parse("INC-1;billing;2024-03-30T22:30:00Z;2024-03-31T01:00:00Z");
        assertEquals("INC-1", incident.id());
        assertEquals("billing", incident.service());
        assertEquals(Instant.parse("2024-03-30T22:30:00Z"), incident.startedAt());
        assertEquals(Instant.parse("2024-03-31T01:00:00Z"), incident.resolvedAt());
        assertEquals(Duration.ofMinutes(150), incident.duration());
    }

    @Test
    void parseRejectsInvalidTimestamp() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> IncidentParser.parse("INC-1;billing;not-a-timestamp;" + "2024-03-31T01:00:00Z"));
        assertTrue(ex.getMessage().contains("startedAt"));
    }

    @Test
    void parseRejectsBlankId() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> IncidentParser.parse(";billing;2024-03-30T22:30:00Z;" + "2024-03-31T01:00:00Z"));
        assertTrue(ex.getMessage().toLowerCase().contains("id"));
    }

    @Test
    void parseRejectsResolvedBeforeStarted() {
        assertThrows(IllegalArgumentException.class, () -> IncidentParser.parse("INC-1;billing;2024-03-31T01:00:00Z;" + "2024-03-30T22:30:00Z"));
    }

    private static Incident incident(String id, String start, String end) {
        return new Incident(id, "svc", Instant.parse(start), Instant.parse(end));
    }

    @Test
    void analyticsOnEmptyList() {
        List<Incident> empty = List.of();
        assertEquals(Duration.ZERO, IncidentAnalytics.totalDowntime(empty));
        assertEquals(Duration.ZERO, IncidentAnalytics.averageDowntime(empty));
        assertEquals(Optional.empty(), IncidentAnalytics.longestIncident(empty));
    }

    @Test
    void averageRoundsDownWhenNotDivisible() {
        Incident a = new Incident("INC-A", "svc", Instant.ofEpochSecond(0, 0), Instant.ofEpochSecond(0, 1));
        Incident b = new Incident("INC-B", "svc", Instant.ofEpochSecond(0, 0), Instant.ofEpochSecond(0, 2));
        Incident c = new Incident("INC-C", "svc", Instant.ofEpochSecond(0, 0), Instant.ofEpochSecond(0, 2));
        List<Incident> incidents = List.of(a, b, c);
        assertEquals(Duration.ofNanos(5), IncidentAnalytics.totalDowntime(incidents));
        assertEquals(Duration.ofNanos(1), IncidentAnalytics.averageDowntime(incidents));
    }

    @Test
    void longestBreaksTiesByEarlierStartThenSmallerId() {
        Incident a = incident("INC-A", "2024-01-01T00:00:00Z", "2024-01-01T00:10:00Z");
        Incident b = incident("INC-B", "2024-01-01T00:00:00Z", "2024-01-01T00:10:00Z");
        Incident c = incident("INC-C", "2024-01-02T00:00:00Z", "2024-01-02T00:10:00Z");

        Optional<Incident> longest = IncidentAnalytics.longestIncident(List.of(c, b, a));
        assertTrue(longest.isPresent());
        assertEquals("INC-A", longest.get().id());
    }

    @Test
    void groupsByLocalDateDifferentFromUtcDate() {
        Incident lateUtc = incident("INC-LATE", "2024-03-30T23:30:00Z", "2024-03-31T00:15:00Z");
        Incident earlyUtc = incident("INC-EARLY", "2024-03-30T10:00:00Z", "2024-03-30T11:00:00Z");

        Map<LocalDate, List<Incident>> grouped = IncidentAnalytics.groupByStartDate(List.of(lateUtc, earlyUtc), BERLIN);

        assertEquals(2, grouped.size());
        assertEquals(List.of(earlyUtc), grouped.get(LocalDate.of(2024, 3, 30)));
        assertEquals(List.of(lateUtc), grouped.get(LocalDate.of(2024, 3, 31)));
    }

    @Test
    void rejectsLocalTimeInDstGap() {
        MaintenanceScheduler scheduler = new MaintenanceScheduler(Clock.fixed(Instant.parse("2024-03-30T00:00:00Z"), ZoneOffset.UTC));
        assertThrows(DateTimeException.class, () ->
                scheduler.nextWindow(
                        LocalDate.of(2024, 3, 31),
                        LocalTime.of(2, 30),
                        Duration.ofMinutes(30),
                        BERLIN,
                        OverlapPolicy.EARLIER));
    }

    @Test
    void overlapEarlierAndLaterProduceDifferentOffsets() {
        MaintenanceScheduler scheduler = new MaintenanceScheduler(Clock.fixed(Instant.parse("2024-10-27T00:00:00Z"), ZoneOffset.UTC));

        MaintenanceWindow earlier = scheduler.nextWindow(
                LocalDate.of(2024, 10, 27),
                LocalTime.of(2, 30),
                Duration.ofMinutes(30),
                BERLIN,
                OverlapPolicy.EARLIER);
        MaintenanceWindow later = scheduler.nextWindow(
                LocalDate.of(2024, 10, 27),
                LocalTime.of(2, 30),
                Duration.ofMinutes(30),
                BERLIN,
                OverlapPolicy.LATER);

        assertEquals("+02:00", earlier.start().atZone(BERLIN).getOffset().getId());
        assertEquals("+01:00", later.start().atZone(BERLIN).getOffset().getId());
        assertEquals(Duration.ofHours(1), Duration.between(earlier.start(), later.start()));
        assertEquals(Instant.parse("2024-10-27T00:30:00Z"), earlier.start());
        assertEquals(Instant.parse("2024-10-27T01:30:00Z"), later.start());
    }

    @Test
    void upcomingWindowsUseCalendarDaysAcrossDstAndAreRepeatable() {
        MaintenanceScheduler scheduler = new MaintenanceScheduler(Clock.fixed(Instant.parse("2024-03-30T00:00:00Z"), ZoneOffset.UTC));

        List<MaintenanceWindow> windows = scheduler.upcomingWindows(
                2,
                LocalTime.of(3, 30),
                Duration.ofMinutes(30),
                BERLIN,
                OverlapPolicy.EARLIER);

        assertEquals(2, windows.size());
        assertEquals(LocalTime.of(3, 30), windows.get(0).start().atZone(BERLIN).toLocalTime());
        assertEquals(LocalTime.of(3, 30), windows.get(1).start().atZone(BERLIN).toLocalTime());

        assertEquals(Duration.ofHours(23), Duration.between(windows.get(0).start(), windows.get(1).start()));
        List<MaintenanceWindow> again = scheduler.upcomingWindows(
                2,
                LocalTime.of(3, 30),
                Duration.ofMinutes(30),
                BERLIN,
                OverlapPolicy.EARLIER);
        assertEquals(windows.get(0).start(), again.get(0).start());
        assertEquals(windows.get(0).end(), again.get(0).end());
        assertEquals(windows.get(1).start(), again.get(1).start());
        assertEquals(windows.get(1).end(), again.get(1).end());
    }


    @Test
    void reportFormatsWindowExactly() {
        MaintenanceWindow window = new MaintenanceWindow(Instant.parse("2024-03-31T01:00:00Z"), Instant.parse("2024-03-31T02:00:00Z"));
        String formatted = ReportFormatter.formatWindow(window, BERLIN, Locale.GERMANY);

        assertEquals("31.03.2024 03:00 +02:00 (Europe/Berlin) – " + "31.03.2024 04:00 +02:00 (Europe/Berlin)", formatted);
    }

    @Test
    void dateAndInstantRoundTripPreservesMoment() {
        Instant instant = Instant.parse("2024-03-30T22:30:00Z");
        Date date = LegacyDateAdapter.toDate(instant);
        assertEquals(instant, LegacyDateAdapter.toInstant(date));
    }

    @Test
    void gregorianCalendarRoundTripPreservesMomentAndZone() {
        ZonedDateTime original = ZonedDateTime.ofInstant(Instant.parse("2024-03-30T22:30:00Z"), BERLIN);

        GregorianCalendar calendar = LegacyDateAdapter.toGregorianCalendar(original);
        ZonedDateTime restored = LegacyDateAdapter.toZonedDateTime(calendar);

        assertEquals(original.toInstant(), restored.toInstant());
        assertEquals(original.getZone(), restored.getZone());
    }
}