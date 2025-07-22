package com.lcsk42.frameworks.starter.rocketmq.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum DelayLevelEnum {
    LEVEL_0S(0, "0s"),
    LEVEL_1S(1, "1s"),
    LEVEL_5S(2, "5s"),
    LEVEL_10S(3, "10s"),
    LEVEL_30S(4, "30s"),
    LEVEL_1M(5, "1m"),
    LEVEL_2M(6, "2m"),
    LEVEL_3M(7, "3m"),
    LEVEL_4M(8, "4m"),
    LEVEL_5M(9, "5m"),
    LEVEL_6M(10, "6m"),
    LEVEL_7M(11, "7m"),
    LEVEL_8M(12, "8m"),
    LEVEL_9M(13, "9m"),
    LEVEL_10M(14, "10m"),
    LEVEL_20M(15, "20m"),
    LEVEL_30M(16, "30m"),
    LEVEL_1H(17, "1h"),
    LEVEL_2H(18, "2h"),
    ;

    private final int level;
    private final String description;

    private static final Map<Integer, DelayLevelEnum> VALUE_MAP =
            Stream.of(values()).collect(Collectors.toMap(DelayLevelEnum::getLevel, Function.identity()));

    @JsonCreator
    public static DelayLevelEnum from(String value) {
        if (value == null) {
            return LEVEL_0S;
        }
        for (DelayLevelEnum e : values()) {
            if (e.name().equalsIgnoreCase(value)) {
                return e;
            }
        }
        try {
            int intValue = Integer.parseInt(value);
            return VALUE_MAP.getOrDefault(intValue, LEVEL_0S);
        } catch (NumberFormatException ignored) {
        }
        return LEVEL_0S;
    }
}
