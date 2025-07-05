package com.lcsk42.frameworks.starter.common.convert;

import com.lcsk42.frameworks.starter.common.util.LocalDateTimeUtil;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Interface for bidirectional conversion between two types (S and T).
 * Provides default methods for common date/time conversions.
 *
 * @param <S> Source type
 * @param <T> Target type
 */
public interface IStructConverter<S, T> {
    /**
     * Converts target type (T) to source type (S).
     *
     * @param t instance of target type
     * @return converted source type instance
     */
    S toS(T t);

    /**
     * Converts source type (S) to target type (T).
     *
     * @param s instance of source type
     * @return converted target type instance
     */
    T toT(S s);

    /**
     * Converts a list of target type (T) to source type (S).
     *
     * @param tList list of target type instances
     * @return list of converted source type instances
     */
    List<S> convertS(List<T> tList);

    /**
     * Converts a list of source type (S) to target type (T).
     *
     * @param sList list of source type instances
     * @return list of converted target type instances
     */
    List<T> convertT(List<S> sList);

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
