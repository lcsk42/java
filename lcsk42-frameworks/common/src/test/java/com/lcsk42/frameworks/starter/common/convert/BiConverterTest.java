package com.lcsk42.frameworks.starter.common.convert;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BiConverterTest {

    @Test
    public void toT() {
        SourceType sourceType = SourceType.builder()
                .stringValue("stringValue")
                .integerValue(123)
                .longValue(123456789L)
                .localDateValue(LocalDate.now())
                .localTimeValue(LocalTime.now())
                .localDateTimeValue(LocalDateTime.now())
                .build();

        TargetType t = SourceToTargetConverter.INSTANCE.toT(sourceType);
        System.out.println("Converted TargetType: " + t);
    }
}
