package org.example;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public final class LegacyDateAdapter {

    private LegacyDateAdapter() {}

    public static Instant toInstant(Date date) {
        Objects.requireNonNull(date, "date не может быть нулл");
        return date.toInstant();
    }

    public static Date toDate(Instant instant) {
        Objects.requireNonNull(instant, "instant не может быть нулл");
        return Date.from(instant);
    }

    public static ZonedDateTime toZonedDateTime(GregorianCalendar calendar) {
        Objects.requireNonNull(calendar, "calendar не может быть нулл");
        return calendar.toZonedDateTime();
    }

    public static GregorianCalendar toGregorianCalendar(ZonedDateTime zonedDateTime) {
        Objects.requireNonNull(zonedDateTime, "zonedDateTime не может быть нулл");
        return GregorianCalendar.from(zonedDateTime);
    }
}