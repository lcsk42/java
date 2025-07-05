package com.lcsk42.frameworks.starter.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.lcsk42.frameworks.starter.convention.exception.ServiceException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Utility class for JSON serialization/deserialization using Jackson.
 * Provides thread-safe operations with pre-configured JsonMapper.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JacksonUtil {
    // Pre-configured JsonMapper instance with custom settings
    private static final ObjectMapper jsonMapper;

    static {
        jsonMapper = JsonMapper.builder()
                // Ignore unknown properties during deserialization
                // ({"id":null,"field":"value"} -> Object.builder.field(value).build())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                // Exclude null fields from serialization
                // ({"id":null,"field":"value"} -> {"field":"value"})
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                // Allow single quotes in JSON
                // ({'field':"value"})
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
                // Allow unquoted field names in JSON
                // ({field:"value"})
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
                // Disable writing dates as timestamps (use ISO-8601 format)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .build();

        // Add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310"
        // Configure Java 8 date/time module with ISO format serializers/deserializers
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        javaTimeModule.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ISO_DATE));
        javaTimeModule.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ISO_DATE));
        javaTimeModule.addDeserializer(LocalTime.class,
                new LocalTimeDeserializer(DateTimeFormatter.ISO_TIME));
        javaTimeModule.addSerializer(LocalTime.class,
                new LocalTimeSerializer(DateTimeFormatter.ISO_TIME));

        // Convert Long and long values to strings to avoid JavaScript number precision issues
        SimpleModule longModule = new SimpleModule();
        longModule.addSerializer(Long.class, ToStringSerializer.instance);
        longModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

        jsonMapper.registerModule(javaTimeModule);
        jsonMapper.registerModule(longModule);
    }


    /**
     * Serializes an object to JSON string.
     *
     * @param object the object to serialize
     * @return JSON string or null if input is null
     * @throws ServiceException if serialization fails
     */
    public static <T> String toJSON(T object) {
        if (Objects.isNull(object)) {
            return null;
        }
        try {
            if (object instanceof String string) {
                return string;
            } else {
                return jsonMapper.writeValueAsString(object);
            }
        } catch (JsonProcessingException e) {
            throw new ServiceException(e.toString());
        }
    }

    /**
     * Deserializes JSON string to specified class.
     *
     * @param json  the JSON string to deserialize
     * @param clazz the target class
     * @return deserialized object or null if input is blank
     * @throws ServiceException if deserialization fails
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json) || Objects.isNull(clazz)) {
            return null;
        }
        try {
            return jsonMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new ServiceException(e.toString());
        }
    }

    /**
     * Deserializes JSON string using TypeReference for complex types.
     *
     * @param json          the JSON string to deserialize
     * @param typeReference the type reference for target type
     * @return deserialized object or null if input is blank
     * @throws ServiceException if deserialization fails
     */
    public static <T> T fromJson(String json,
                                 TypeReference<T> typeReference) {
        if (StringUtils.isBlank(json) || Objects.isNull(typeReference)) {
            return null;
        }
        try {
            return jsonMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new ServiceException(e.toString());
        }
    }

    /**
     * Checks if a string is valid JSON.
     *
     * @param json the string to validate
     * @return true if valid JSON, false otherwise
     */
    public static boolean isJson(String json) {
        if (StringUtils.isBlank(json)) {
            return false;
        }
        try {
            jsonMapper.readTree(json);
            return true;
        } catch (JsonProcessingException ignore) {
            // ignore exception
            return false;
        }
    }

    /**
     * Serializes an object to pretty-printed JSON string.
     *
     * @param object the object to serialize
     * @return formatted JSON string or null if input is null
     * @throws ServiceException if serialization fails
     */
    public static <T> String toPrettyJson(T object) {
        if (Objects.isNull(object)) {
            return null;
        }
        try {
            if (object instanceof String) {
                return jsonMapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(jsonMapper.readTree(object.toString()));
            } else {
                return jsonMapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(object);
            }
        } catch (JsonProcessingException e) {
            throw new ServiceException(e.toString());
        }
    }

    /**
     * Finds all text values for a given field name in JSON.
     *
     * @param json      the JSON string to search
     * @param fieldName the field name to find
     * @return list of text values or empty list if not found
     * @throws ServiceException if JSON processing fails
     */
    public static List<String> findText(String json, String fieldName) {
        if (StringUtils.isBlank(json) || StringUtils.isBlank(fieldName)) {
            return Collections.emptyList();
        }
        try {
            return jsonMapper.readTree(json).findValuesAsText(fieldName);
        } catch (JsonProcessingException e) {
            throw new ServiceException(e.toString());
        }
    }

    /**
     * Finds the first text value for a given field name in JSON.
     *
     * @param json      the JSON string to search
     * @param fieldName the field name to find
     * @return first text value or null if not found
     */
    public static String find(String json, String fieldName) {
        return findText(json, fieldName).stream().findFirst().orElse(null);
    }

    /**
     * Parses JSON string to JsonNode tree.
     *
     * @param json the JSON string to parse
     * @return JsonNode tree or null if input is blank
     * @throws ServiceException if JSON processing fails
     */
    public static JsonNode toTree(String json) {
        if (StringUtils.isNoneBlank(json)) {
            try {
                return jsonMapper.readTree(json);
            } catch (JsonProcessingException e) {
                throw new ServiceException(e.toString());
            }
        }
        return null;
    }

    /**
     * Converts an object to another type using Jackson's conversion.
     *
     * @param object the object to convert
     * @param clazz  the target class
     * @return converted object or null if input is null
     */
    public static <T> T convert(Object object, Class<T> clazz) {
        if (Objects.isNull(object) || Objects.isNull(clazz)) {
            return null;
        }
        return jsonMapper.convertValue(object, clazz);
    }

    /**
     * Converts an object to another type using TypeReference.
     *
     * @param object        the object to convert
     * @param typeReference the type reference for target type
     * @return converted object or null if input is null
     */
    public static <T> T convert(Object object, TypeReference<T> typeReference) {
        if (Objects.isNull(object) || Objects.isNull(typeReference)) {
            return null;
        }
        return jsonMapper.convertValue(object, typeReference);
    }
}