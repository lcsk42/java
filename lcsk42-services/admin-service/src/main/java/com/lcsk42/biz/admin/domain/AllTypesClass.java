package com.lcsk42.biz.admin.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class AllTypesClass {

    // 基本数据类型
    private boolean boolValue;
    private byte byteValue;
    private short shortValue;
    private int intValue;
    private long longValue;
    private float floatValue;
    private double doubleValue;
    private char charValue;


    // 包装类
    private Boolean boolObj;
    private Byte byteObj;
    private Short shortObj;
    private Integer intObj;
    private Long longObj;
    private Float floatObj;
    private Double doubleObj;
    private Character charObj;

    // 字符串与数字
    private String stringValue;
    private BigDecimal bigDecimalValue;
    private BigInteger bigIntegerValue;

    // 日期与时间
    private Date utilDate;
    private java.sql.Date sqlDate;
    private java.sql.Time sqlTime;
    private java.sql.Timestamp sqlTimestamp;
    private LocalDate localDate;
    private LocalTime localTime;
    private LocalDateTime localDateTime;
    private OffsetDateTime offsetDateTime;
    private ZonedDateTime zonedDateTime;
    private Instant instant;

    // 集合类型
    private List<String> stringList;
    private Set<Integer> intSet;
    private Map<String, Object> map;

    // 枚举类型
    private Status status;

    // 嵌套对象
    private Nested nested;

    public enum Status {
        ACTIVE, INACTIVE, PENDING
    }

    @Data
    @Builder
    public static class Nested {
        private String nestedField;
        private int nestedNumber;
    }
}
