package org.example;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public final class ReportFormatter {

    private ReportFormatter() {
    }

    public static String formatWindow(
            MaintenanceWindow window, ZoneId zone, Locale locale) {
        Objects.requireNonNull(window, "window не может быть нулл");
        Objects.requireNonNull(zone, "zone не может быть нулл");
        Objects.requireNonNull(locale, "locale не может быть нулл");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm XXX (VV)", locale);

        String start = formatter.format(window.start().atZone(zone));
        String end = formatter.format(window.end().atZone(zone));
        return start + " – " + end;
    }
}