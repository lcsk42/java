package com.lcsk42.frameworks.starter.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Utility class for LocalDateTime conversions and operations.
 * Provides methods for working with timestamps across different time zones.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LocalDateTimeUtil {
    /**
     * Gets the current date-time in the system default time-zone.
     *
     * @return current LocalDateTime
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * Converts epoch milliseconds to LocalDateTime in system default time-zone.
     *
     * @param epochMilli milliseconds since 1970-01-01T00:00:00Z
     * @return corresponding LocalDateTime
     */
    public static LocalDateTime of(long epochMilli) {
        return of(Instant.ofEpochMilli(epochMilli));
    }

    /**
     * Converts Instant to LocalDateTime in system default time-zone.
     *
     * @param instant the Instant to convert
     * @return corresponding LocalDateTime or null if input is null
     */
    public static LocalDateTime of(Instant instant) {
        return of(instant, ZoneId.systemDefault());
    }

    /**
     * Converts Instant to LocalDateTime in UTC time-zone.
     *
     * @param instant the Instant to convert
     * @return corresponding LocalDateTime in UTC or null if input is null
     */
    public static LocalDateTime ofUTC(Instant instant) {
        return of(instant, ZoneId.of("UTC"));
    }

    /**
     * Converts Instant to LocalDateTime in specified time-zone.
     * Uses system default time-zone if zoneId is null.
     *
     * @param instant the Instant to convert
     * @param zoneId  the target time-zone
     * @return corresponding LocalDateTime or null if instant is null
     */
    public static LocalDateTime of(Instant instant, ZoneId zoneId) {
        if (null == instant) {
            return null;
        }

        return LocalDateTime.ofInstant(instant, ObjectUtils.defaultIfNull(zoneId,
                ZoneId.systemDefault()));
    }

    /**
     * Converts LocalDateTime to epoch seconds in system default time-zone.
     *
     * @param localDateTime the date-time to convert
     * @return seconds since 1970-01-01T00:00:00Z or null if input is null
     */
    public static Long toEpochMilli(LocalDateTime localDateTime) {
        if (null == localDateTime) {
            return null;
        }
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }
}
