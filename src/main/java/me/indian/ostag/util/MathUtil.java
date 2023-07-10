package me.indian.ostag.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class MathUtil {

    public static int dayDifference(final long lastPlayed) {
        final Instant instant = Instant.ofEpochSecond(lastPlayed);
        final LocalDate lastPlayedDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
        final LocalDate currentDate = LocalDate.now();
        return Math.toIntExact(ChronoUnit.DAYS.between(currentDate, lastPlayedDate));
    }

}
