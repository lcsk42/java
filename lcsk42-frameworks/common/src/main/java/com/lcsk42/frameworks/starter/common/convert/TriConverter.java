package com.lcsk42.frameworks.starter.common.convert;

import com.lcsk42.frameworks.starter.common.util.LocalDateTimeUtil;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Interface for tri-directional conversion between three types (P, T, V).
 * Provides default methods for common date/time conversions.
 *
 * @param <P> PO type
 * @param <T> DTO type
 * @param <V> VO type
 */
public interface TriConverter<P, T, V> {
    /**
     * Converts target type (T) to primary type (P).
     *
     * @param t instance of DTO type
     * @return converted PO type instance
     */
    P toP(T t);

    /**
     * Converts primary type (P) to target type (T).
     *
     * @param p instance of PO type
     * @return converted DTO type instance
     */
    V toV(P p);

    /**
     * Converts a list of DTO type (T) to primary type (P).
     *
     * @param tList list of DTO type instances
     * @return list of converted PO type instances
     */
    List<P> convertP(List<T> tList);

    /**
     * Converts a list of primary type (P) to target type (T).
     *
     * @param pList list of PO type instances
     * @return list of converted DTO type instances
     */
    List<V> convertV(List<P> pList);

    /**
     * Default method to convert LocalDateTime to epoch milliseconds.
     *
     * @param localDateTime the LocalDateTime to convert
     * @return epoch milliseconds or null if input is null
     */
    default Long toLong(LocalDateTime localDateTime) {
        return Optional.ofNullable(localDateTime)
                .map(LocalDateTimeUtil::toEpochMilli)
                .orElse(null);
    }

    /**
     * Default method to convert epoch milliseconds to LocalDateTime.
     *
     * @param localDateTime epoch milliseconds
     * @return LocalDateTime instance or null if input is null
     */
    default LocalDateTime toLocalDateTime(Long localDateTime) {
        return Optional.ofNullable(localDateTime)
                .map(LocalDateTimeUtil::of)
                .orElse(null);
    }

    /**
     * Default method to format LocalDateTime as String.
     *
     * @param localDateTime     the LocalDateTime to format
     * @param dateTimeFormatter the formatter to use (defaults to ISO_LOCAL_DATE_TIME if null)
     * @return formatted string or null if input is null
     */
    default String toString(LocalDateTime localDateTime, DateTimeFormatter dateTimeFormatter) {
        if (null == localDateTime) {
            return null;
        }
        return Objects.requireNonNullElse(dateTimeFormatter, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .format(localDateTime);
    }

    /**
     * Default method to parse String to LocalDateTime.
     *
     * @param localDateTime     the string to parse
     * @param dateTimeFormatter the formatter to use (defaults to ISO_LOCAL_DATE_TIME if null)
     * @return parsed LocalDateTime or null if input is blank
     */
    default LocalDateTime toLocalDateTime(String localDateTime, DateTimeFormatter dateTimeFormatter) {
        if (StringUtils.isBlank(localDateTime)) {
            return null;
        }
        return LocalDateTime.parse(localDateTime,
                Objects.requireNonNullElse(dateTimeFormatter, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
