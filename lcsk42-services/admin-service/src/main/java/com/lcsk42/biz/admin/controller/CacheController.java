package com.lcsk42.biz.admin.controller;

import com.lcsk42.biz.admin.domain.AllTypesClass;
import com.lcsk42.biz.admin.domain.CustomCacheDTO;
import com.lcsk42.frameworks.starter.cache.DistributedCache;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
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
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/cache")
@RequiredArgsConstructor
@Tag(description = "Cache Management", name = "CacheController")
public class CacheController {

    private final DistributedCache distributedCache;

    @GetMapping
    @Operation(
            summary = "Get a cache entry",
            description = "Retrieves a value from the distributed cache by its key."
    )
    public Object getCache(@RequestParam("key") String key) {
        return distributedCache.get(key, Object.class);
    }

    @PostMapping
    @Operation(
            summary = "Add a custom cache entry",
            description = "Adds a key-value pair to the distributed cache with a default expiration time of 100 seconds."
    )
    public void addCache(@RequestBody CustomCacheDTO customCacheDTO) {
        distributedCache.put(customCacheDTO.getKey(), customCacheDTO.getValue(), 100, TimeUnit.SECONDS);
    }

    @PostMapping("/all-type")
    @Operation(
            summary = "Add all types of data to cache",
            description = "Adds a complex object containing various data types to the distributed cache."
    )
    public void addAllTypesCache(@RequestBody CustomCacheDTO customCacheDTO) {
        AllTypesClass value = AllTypesClass.builder()
                // 基本类型
                .boolValue(true)
                .byteValue((byte) 1)
                .shortValue((short) 2)
                .intValue(3)
                .longValue(4L)
                .floatValue(5.5f)
                .doubleValue(6.6d)
                .charValue('A')

                // 包装类型
                .boolObj(Boolean.FALSE)
                .byteObj(Byte.valueOf("10"))
                .shortObj(Short.valueOf("20"))
                .intObj(30)
                .longObj(40L)
                .floatObj(50.5f)
                .doubleObj(60.6)
                .charObj('Z')

                // 字符串和数字
                .stringValue("Hello Builder")
                .bigDecimalValue(new BigDecimal("12345.6789"))
                .bigIntegerValue(new BigInteger("9876543210"))

                // 日期时间
                .utilDate(new Date())
                .sqlDate(new java.sql.Date(System.currentTimeMillis()))
                .sqlTime(new Time(System.currentTimeMillis()))
                .sqlTimestamp(new Timestamp(System.currentTimeMillis()))
                .localDate(LocalDate.now())
                .localTime(LocalTime.now())
                .localDateTime(LocalDateTime.now())
                .offsetDateTime(OffsetDateTime.now())
                .zonedDateTime(ZonedDateTime.now())
                .instant(Instant.now())

                // 集合

                .stringList(List.of("one", "two", "three"))
                .intSet(Set.of(1, 2, 3))
                .map(Map.of("key1", "value1", "key2", 42))

                // 枚举
                .status(AllTypesClass.Status.ACTIVE)

                // 嵌套对象
                .nested(AllTypesClass.Nested.builder()
                        .nestedField("nested value")
                        .nestedNumber(99)
                        .build())

                .build();

        distributedCache.put(customCacheDTO.getKey(), value, 100, TimeUnit.SECONDS);
    }
}
