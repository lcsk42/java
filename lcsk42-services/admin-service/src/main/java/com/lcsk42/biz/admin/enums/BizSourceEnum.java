package com.lcsk42.biz.admin.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum BizSourceEnum {

    DEFAULT("default"),
    ADMIN("admin"),
    USER("user"),
    ;

    @EnumValue
    @JsonValue
    private final String name;

    private static final Map<String, BizSourceEnum> MAP;

    static {
        MAP = Stream.of(values())
                .collect(Collectors.toMap(BizSourceEnum::getName, Function.identity()));
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static BizSourceEnum fromName(String name) {
        return MAP.getOrDefault(name, DEFAULT);
    }
}
